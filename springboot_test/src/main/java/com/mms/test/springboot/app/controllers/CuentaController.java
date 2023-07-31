package com.mms.test.springboot.app.controllers;

import com.mms.test.springboot.app.models.Cuenta;
import com.mms.test.springboot.app.models.TransaccionDto;
import com.mms.test.springboot.app.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Cuenta> listar() {
        return cuentaService.findAll();
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Cuenta cuenta = cuentaService.findById(id);
        if (cuenta == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseEntity.ok(cuenta);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Cuenta guardar(@RequestBody Cuenta body) {
        return cuentaService.save(body);
    }

    @PostMapping(value = "/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransaccionDto body) {
        cuentaService.transferir(body.getCuentaOrigenId(), body.getCuentaDestinoId(), body.getMonto(), body.getBancoId());

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Transaccion Realizada con Exito!");
        response.put("transaction", body);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        cuentaService.deteleById(id);
    }
}
