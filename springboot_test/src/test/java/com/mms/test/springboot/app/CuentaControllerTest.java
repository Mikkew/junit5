package com.mms.test.springboot.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mms.test.springboot.app.controllers.CuentaController;
import com.mms.test.springboot.app.models.Cuenta;
import com.mms.test.springboot.app.models.TransaccionDto;
import com.mms.test.springboot.app.services.CuentaService;
import com.mms.test.springboot.app.utils.Datos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)
public class CuentaControllerTest {

    @MockBean
    private CuentaService service;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDetalle() throws Exception {
        // Given
        when(service.findById(1L)).thenReturn(Datos.crearCuenta001().orElseThrow());

        // When
        mockMvc.perform(get("/api/cuentas/1").contentType(MediaType.APPLICATION_JSON))

        //Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.persona").value("Andrés"))
                .andExpect(jsonPath("$.saldo").value("1000"));

        verify(service).findById(1L);
    }

    @Test
    public void testTransferir() throws Exception, JsonProcessingException {
        // Given
        TransaccionDto dto = TransaccionDto.builder()
                .cuentaOrigenId(1L).cuentaDestinoId(2L)
                .monto(new BigDecimal("100"))
                .bancoId(1L).build();

        System.out.println(objectMapper.writeValueAsString(dto));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Transaccion Realizada con Exito!");
        response.put("transaction", dto);

        System.out.println(objectMapper.writeValueAsString(response));

        // When
        mockMvc.perform(post("/api/cuentas/transfer").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))

        // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.message").value("Transaccion Realizada con Exito!"))
                .andExpect(jsonPath("$.transaction.cuentaOrigenId").value(dto.getCuentaOrigenId()))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

    }

    @Test
    public void testListar() throws Exception {
        // Given
        List<Cuenta> cuentas = Arrays.asList(
                Datos.crearCuenta001().get(),
                Datos.crearCuenta002().get());
        when(service.findAll()).thenReturn(cuentas);

        // When
        mockMvc.perform(get("/api/cuentas").contentType(MediaType.APPLICATION_JSON))

        // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].persona").value(Datos.crearCuenta001().orElseThrow().getPersona()))
                .andExpect(jsonPath("$[1].persona").value(Datos.crearCuenta002().orElseThrow().getPersona()))
                .andExpect(jsonPath("$[0].saldo").value(Datos.crearCuenta001().orElseThrow().getSaldo()))
                .andExpect(jsonPath("$[1].saldo").value(Datos.crearCuenta002().orElseThrow().getSaldo()))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(cuentas)));

        verify(service).findAll();
    }

    @Test
    public void testGuardar() throws Exception, JsonProcessingException {
        // Given
        Cuenta cuentaNueva = Cuenta.builder().persona("Mario").saldo(new BigDecimal("3000")).build();

        when(service.save(any())).then(invocation -> {
            Cuenta c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });

        // When
        mockMvc.perform(post("/api/cuentas").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuentaNueva)))
        // Then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.persona").value(cuentaNueva.getPersona()))
                .andExpect(jsonPath("$.saldo", is(3000)));
    }
}