package com.mms.test.springboot.app.repositories;

import com.mms.test.springboot.app.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

//    public List<Cuenta> findAll();
//
//    public Cuenta findById(Long id);
//
//    public void update(Cuenta cuenta);

    public Optional<Cuenta> findByPersona(String persona);
}
