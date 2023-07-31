package com.mms.test.springboot.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mms.test.springboot.app.models.Cuenta;
import com.mms.test.springboot.app.models.TransaccionDto;
import com.mms.test.springboot.app.utils.Datos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.bytebuddy.implementation.FixedValue.value;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(value = OrderAnnotation.class)
public class CuentaControllerWebTestClientTests {

    @Autowired
    private WebTestClient client;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    public void testTransfer() throws JsonProcessingException {
        // Given
        TransaccionDto dto = TransaccionDto.builder()
                .cuentaOrigenId(1L).cuentaDestinoId(2L)
                .monto(new BigDecimal("100"))
                .bancoId(1L).build();

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Transaccion Realizada con Exito!");
        response.put("transaction", dto);

        // When
        client.post().uri("/api/cuentas/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(resp -> {
                    try {
                        JsonNode json = objectMapper.readTree(resp.getResponseBody());
                        System.out.println("response: " + json.toString());

                        assertEquals("Transaccion Realizada con Exito!", json.path("message").asText());
                        assertEquals(1L, json.path("transaction").path("cuentaOrigenId").asLong());
                        assertEquals(LocalDate.now().toString(), json.path("date").asText());
                        assertEquals("100", json.path("transaction").path("monto").asText());
                    } catch (IOException ex) {
                        ex.printStackTrace(System.out);
                    }
                })
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.message").value(is("Transaccion Realizada con Exito!"))
                .jsonPath("$.message").value(valor -> assertEquals("Transaccion Realizada con Exito!", valor))
                .jsonPath("$.message").isEqualTo("Transaccion Realizada con Exito!")
                .jsonPath("$.transaction.cuentaOrigenId").isEqualTo(dto.getCuentaOrigenId())
                .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
                .json(objectMapper.writeValueAsString(response));
    }

    @Test
    @Order(2)
    public void testDetalle() {
       client.get().uri("/api/cuentas/1").exchange()
               .expectStatus().isOk()
               .expectHeader().contentType(MediaType.APPLICATION_JSON)
               .expectBody()
               .jsonPath("$.persona").value(is(Datos.crearCuenta001().get().getPersona()))
               .jsonPath("$.saldo").isEqualTo(900);
    }

    @Test
    @Order(3)
    public void testDetalle2() {
       client.get().uri("/api/cuentas/2").exchange()
               .expectStatus().isOk()
               .expectHeader().contentType(MediaType.APPLICATION_JSON)
               .expectBody(Cuenta.class)
               .consumeWith(response -> {
                  Cuenta cuenta = response.getResponseBody();
                  assertEquals(Datos.crearCuenta002().get().getPersona(), cuenta.getPersona());
                  assertEquals(2100.0, cuenta.getSaldo().doubleValue());
               });
    }

    @Test
    @Order(4)
    public void testListar() {
       client.get().uri("/api/cuentas").exchange()
               .expectStatus().isOk()
               .expectHeader().contentType(MediaType.APPLICATION_JSON)
               .expectBody()
               .jsonPath("$[0].id").isEqualTo(Datos.crearCuenta001().get().getId())
               .jsonPath("$[0].persona").isEqualTo(Datos.crearCuenta001().get().getPersona())
               .jsonPath("$[0].saldo").isEqualTo(900)
               .jsonPath("$[1].id").isEqualTo(Datos.crearCuenta002().get().getId())
               .jsonPath("$[1].persona").isEqualTo(Datos.crearCuenta002().get().getPersona())
               .jsonPath("$[1].saldo").isEqualTo(2100)
               .jsonPath("$").isArray()
               .jsonPath("$").value(hasSize(2));
    }

    @Test
    @Order(5)
    public void testListar2() {
        client.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .consumeWith(response -> {
                    List<Cuenta> cuentas = response.getResponseBody();
                    assertEquals(Datos.crearCuenta001().get().getId(), cuentas.get(0).getId());
                    assertEquals(Datos.crearCuenta001().get().getPersona(), cuentas.get(0).getPersona());
                    assertEquals(Double.valueOf("900"), cuentas.get(0).getSaldo().doubleValue());
                    assertEquals(Datos.crearCuenta002().get().getId(), cuentas.get(1).getId());
                    assertEquals(Datos.crearCuenta002().get().getPersona(), cuentas.get(1).getPersona());
                    assertEquals(Double.valueOf(2100), cuentas.get(1).getSaldo().doubleValue());
                    assertEquals(2, cuentas.size());
                });
    }

    @Test
    @Order(6)
    public void testGuardar() {
        Cuenta cuenta = Cuenta.builder().persona("Mario").saldo(new BigDecimal("3000")).build();

        client.post().uri("/api/cuentas").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cuenta)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(3)
                .jsonPath("$.persona").value(is(cuenta.getPersona()))
                .jsonPath("$.saldo").value(is(cuenta.getSaldo().intValue()));
    }

    @Test
    @Order(7)
    void testGuardar2() {
        // given
        Cuenta cuenta = new Cuenta(null, "Pepa", new BigDecimal("3500"));
        // when
        client.post().uri("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cuenta)
                .exchange()
                // then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cuenta.class)
                .consumeWith(response -> {
                    Cuenta c = response.getResponseBody();
                    assertNotNull(c);
                    assertEquals(4L, c.getId());
                    assertEquals("Pepa", c.getPersona());
                    assertEquals("3500", c.getSaldo().toPlainString());
                });
    }

    @Test
    @Order(8)
    void testEliminar() {
        client.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .hasSize(4);

        client.delete().uri("/api/cuentas/3")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        client.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .hasSize(3);

        client.get().uri("/api/cuentas/3").exchange()
//                .expectStatus().is5xxServerError();
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }
}