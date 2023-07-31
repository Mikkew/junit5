package com.mms.appmockito.repositories.impl;

import com.mms.appmockito.models.Examen;
import com.mms.appmockito.repositories.ExamenRepository;
import com.mms.appmockito.utils.Datos;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryImpl implements ExamenRepository {

    @Override
    public List<Examen> findAll() {
        System.out.println("ExamenRepositoryImpl.findAll");
        try {
            System.out.println(this.getClass().getSimpleName());
            TimeUnit.SECONDS.sleep(5);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }

        return Datos.EXAMENES;
    }

    @Override
    public Examen guardar(Examen examen) {
        System.out.println("ExamenRepositoryImpl.guardar");
        return Datos.EXAMEN;
    }
}
