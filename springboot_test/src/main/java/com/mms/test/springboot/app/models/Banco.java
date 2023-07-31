package com.mms.test.springboot.app.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BANCOS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Banco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "TOTAL_TRANSFERENCIAS")
    private Integer totalTransferencias;
}
