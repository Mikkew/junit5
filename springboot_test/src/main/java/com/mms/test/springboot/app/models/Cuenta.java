package com.mms.test.springboot.app.models;

import com.mms.test.springboot.app.exceptions.DineroInsuficienteException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "CUENTAS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PERSONA")
    private String persona;

    @Column(name = "SALDO")
    private BigDecimal saldo;

    public Cuenta(String persona, BigDecimal saldo) {
        this.persona = persona;
        this.saldo = saldo;
    }

    public void debito(BigDecimal monto) {
        BigDecimal nuevoSaldo = saldo.subtract(monto);
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new DineroInsuficienteException("Dinero Insuficiente en la cuenta.");
        }
        saldo = nuevoSaldo;
    }

    public void credito(BigDecimal monto) {
        saldo = saldo.add(monto);
    }
}
