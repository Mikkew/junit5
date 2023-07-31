package com.mms.test.springboot.app;

import com.mms.test.springboot.app.exceptions.DineroInsuficienteException;
import com.mms.test.springboot.app.models.Banco;
import com.mms.test.springboot.app.models.Cuenta;
import com.mms.test.springboot.app.repositories.BancoRepository;
import com.mms.test.springboot.app.repositories.CuentaRepository;
//import com.mms.test.springboot.app.services.CuentaService;
import com.mms.test.springboot.app.services.impl.CuentaServiceImpl;
import com.mms.test.springboot.app.utils.Datos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SpringbootTestApplicationTests {

    @MockBean
    private CuentaRepository cuentaRepository;

    @MockBean
    private BancoRepository bancoRepository;

    @Autowired
    private CuentaServiceImpl service;

    @BeforeEach
    public void setUp() {
//        cuentaRepository = mock(CuentaRepository.class);
//        bancoRepository = mock(BancoRepository.class);
//        service = new CuentaServiceImpl(cuentaRepository, bancoRepository);
//        Datos.CUENTA_001.setSaldo(new BigDecimal("1000"));
//        Datos.CUENTA_002.setSaldo(new BigDecimal("2000"));
//        Datos.BANCO.setTotalTransferencias(0);
    }

    @Test
    public void contextLoads() {
        when(cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta001());
        when(cuentaRepository.findById(2L)).thenReturn(Datos.crearCuenta002());
        when(bancoRepository.findById(1L)).thenReturn(Datos.crearBanco());

        BigDecimal saldoOrigen = service.revisarSaldo(1L);
        BigDecimal saldoDestino = service.revisarSaldo(2L);

        assertEquals("1000", saldoOrigen.toPlainString());
        assertEquals("2000", saldoDestino.toPlainString());

        service.transferir(1L, 2L, new BigDecimal("100"), 1L);

        saldoOrigen = service.revisarSaldo(1L);
        saldoDestino = service.revisarSaldo(2L);

        assertEquals("900", saldoOrigen.toPlainString());
        assertEquals("2100", saldoDestino.toPlainString());

        Integer total = service.revisarTotalTransferencias(1L);
        assertEquals(1, total);

        verify(cuentaRepository, times(3)).findById(1L);
        verify(cuentaRepository, times(3)).findById(2L);
        verify(cuentaRepository, times(2)).save(any(Cuenta.class));

        verify(bancoRepository, times(2)).findById(1L);
        verify(bancoRepository).save(any(Banco.class));

        verify(cuentaRepository, times(6)).findById(anyLong());
        verify(cuentaRepository, never()).findAll();

    }

    @Test
    public void contextLoads2() {
//        when(cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta001());
//        when(cuentaRepository.findById(2L)).thenReturn(Datos.crearCuenta002());
//        when(bancoRepository.findById(1L)).thenReturn(Datos.crearBanco());

        when(cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta001());
        when(cuentaRepository.findById(2L)).thenReturn(Datos.crearCuenta002());

        when(bancoRepository.findById(1L)).thenReturn(Datos.crearBanco());

        BigDecimal saldoOrigen = service.revisarSaldo(1L);
        BigDecimal saldoDestino = service.revisarSaldo(2L);

        assertEquals("1000", saldoOrigen.toPlainString());
        assertEquals("2000", saldoDestino.toPlainString());

        assertThrows(DineroInsuficienteException.class, () -> {
            service.transferir(1L, 2L, new BigDecimal("1200"), 1L);
        });

        saldoOrigen = service.revisarSaldo(1L);
        saldoDestino = service.revisarSaldo(2L);

        assertEquals("1000", saldoOrigen.toPlainString());
        assertEquals("2000", saldoDestino.toPlainString());

        Integer total = service.revisarTotalTransferencias(1L);
        assertEquals(0, total);

        verify(cuentaRepository, times(3)).findById(1L);
        verify(cuentaRepository, times(2)).findById(2L);
        verify(cuentaRepository, never()).save(any(Cuenta.class));

        verify(bancoRepository, times(1)).findById(1L);
        verify(bancoRepository, never()).save(any(Banco.class));

        verify(cuentaRepository, times(5)).findById(anyLong());
        verify(cuentaRepository, never()).findAll();

    }

    @Test
    public void contextLoads3() {
        when(cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta001());

        Cuenta cuenta1 = service.findById(1L);
        Cuenta cuenta2 = service.findById(1L);

        assertSame(cuenta1, cuenta2);
        assertTrue(cuenta1 == cuenta2);
        assertEquals("Andrés", cuenta1.getPersona());
        assertEquals("Andrés", cuenta2.getPersona());

        verify(cuentaRepository, times(2)).findById(1L);
    }

    @Test
    public void testFindAll() {
        //Given
        List<Cuenta> datos= Arrays.asList(
                Datos.crearCuenta001().get(),
                Datos.crearCuenta002().get());
        when(cuentaRepository.findAll()).thenReturn(datos);

        //When
        List<Cuenta> cuentas = cuentaRepository.findAll();

        // Then
        assertFalse(cuentas.isEmpty());
        assertEquals(2, cuentas.size());
        assertEquals(datos.get(0), cuentas.get(0));

        verify(cuentaRepository).findAll();
    }

    @Test
    public void testSave() {
        // Given
        Cuenta cuentaNueva = Cuenta.builder().persona("Mario").saldo(new BigDecimal("3000")).build();
        when(cuentaRepository.save(any(Cuenta.class))).then(invocation -> {
           Cuenta c = invocation.getArgument(0);
           c.setId(3L);
           return c;
        });

        // When
        Cuenta cuenta = cuentaRepository.save(cuentaNueva);

        //Then
        assertEquals(3L, cuenta.getId());
        assertEquals(cuentaNueva.getPersona(), cuenta.getPersona());
        assertEquals(cuentaNueva.getSaldo(), cuenta.getSaldo());

        verify(cuentaRepository).save(any(Cuenta.class));

    }
}
