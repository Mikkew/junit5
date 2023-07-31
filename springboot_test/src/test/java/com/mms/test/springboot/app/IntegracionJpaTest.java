package com.mms.test.springboot.app;

import com.mms.test.springboot.app.models.Cuenta;
import com.mms.test.springboot.app.repositories.CuentaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IntegracionJpaTest {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Test
    public void testFindById() {
        Optional<Cuenta> cuenta = cuentaRepository.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("Andrés", cuenta.orElseThrow().getPersona());
    }

    @Test
    public void testFindByPerson() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Andrés");
        assertTrue(cuenta.isPresent());
        assertEquals("Andrés", cuenta.orElseThrow().getPersona());
        assertEquals("1000.00", cuenta.orElseThrow().getSaldo().toPlainString());
    }

    @Test
    public void testFindByPersonThrowException() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Mario");
        assertThrows(NoSuchElementException.class, cuenta::orElseThrow);
        assertFalse(cuenta.isPresent());
    }

    @Test
    public void testFindAll() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        assertFalse(cuentas.isEmpty());
        assertEquals(2, cuentas.size());
    }

    @Test
    public void testSave() {
        //Given
        Cuenta cuentaNueva = Cuenta.builder().persona("Mario").saldo(new BigDecimal("3000")).build();

        //When
        Cuenta cuenta = cuentaRepository.save(cuentaNueva);
//        Cuenta cuenta = cuentaRepository.findByPersona(cuentaNueva.getPersona()).orElseThrow();

        //Then
        assertEquals(cuentaNueva.getPersona(), cuenta.getPersona());
        assertEquals(cuentaNueva.getSaldo(), cuenta.getSaldo());
        assertEquals(3, cuenta.getId());
    }

    @Test
    public void testUpdate() {
        // Given
        Cuenta cuentaNueva = Cuenta.builder().persona("Mario").saldo(new BigDecimal("3000")).build();

        // When
        Cuenta cuenta = cuentaRepository.save(cuentaNueva);
//        Cuenta cuenta = cuentaRepository.findByPersona(cuentaNueva.getPersona()).orElseThrow();

        // Then
        assertEquals(cuentaNueva.getPersona(), cuenta.getPersona());
        assertEquals(cuentaNueva.getSaldo(), cuenta.getSaldo());

        // When
        cuenta.setSaldo(new BigDecimal("3800"));
        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);

        // Then
        assertEquals(cuenta.getPersona(), cuentaActualizada.getPersona());
        assertEquals(cuenta.getSaldo(), cuentaActualizada.getSaldo());
    }

    @Test
    public void testDelete() {
        Cuenta cuenta = cuentaRepository.findById(2L).orElseThrow();
        assertEquals("John", cuenta.getPersona());
        cuentaRepository.delete(cuenta);

//        boolean cuentaExiste = cuentaRepository.existsById(2L);
//        assertFalse(cuentaExiste);

        assertThrows(NoSuchElementException.class, () -> {
            cuentaRepository.findById(2L).orElseThrow();
        });

        assertEquals(1, cuentaRepository.findAll().size());
    }
}
