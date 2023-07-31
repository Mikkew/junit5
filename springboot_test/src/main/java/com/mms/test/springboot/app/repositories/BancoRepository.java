package com.mms.test.springboot.app.repositories;

import com.mms.test.springboot.app.models.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

//import java.util.List;

public interface BancoRepository extends JpaRepository<Banco, Long> {

//    public List<Banco> findAll();
//
//    public Banco findById(Long id);
//
//    public void update(Banco banco);
}