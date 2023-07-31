package com.mms.appmockito.repositories.impl;

import com.mms.appmockito.repositories.PreguntaRepository;
import com.mms.appmockito.utils.Datos;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PreguntasRepositoryImpl implements PreguntaRepository {

    @Override
    public List<String> findPreguntasPorExamenId(Long id) {
        System.out.println("PreguntasRepositoryImpl.findPreguntasPorExamenId");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.out);
        }
        return Datos.PREGUNTAS;
    }

    @Override
    public void guardarVarias(List<String> preguntas) {
        System.out.println("PreguntasRepositoryImpl.guardarVarias");
    }
}
