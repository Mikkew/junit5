package com.mms.appmockito.services.impl;

import com.mms.appmockito.models.Examen;
import com.mms.appmockito.repositories.ExamenRepository;
import com.mms.appmockito.repositories.PreguntaRepository;
import com.mms.appmockito.services.IExamenService;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements IExamenService {

    private final ExamenRepository examenRepository;
    private final PreguntaRepository preguntaRepository;

    public ExamenServiceImpl(
            ExamenRepository examenRepository,
            PreguntaRepository preguntaRepository) {
        this.examenRepository = examenRepository;
        this.preguntaRepository = preguntaRepository;
    }

    @Override
    public Optional<Examen> findExamenByNombre(String nombre) {
        return examenRepository.findAll().stream()
                .filter(e -> e.getNombre().equals(nombre))
                .findFirst();
    }

    @Override
    public Examen findExamenPorNombreConPreguntas(String nombre) {
        Examen examen = null;
        Optional<Examen> examenOptional = findExamenByNombre(nombre);
        if(examenOptional.isPresent()) {
            examen = examenOptional.orElseThrow();
            List<String> preguntas = preguntaRepository.findPreguntasPorExamenId(examen.getId());
            preguntaRepository.findPreguntasPorExamenId(examen.getId());
            examen.setPreguntas(preguntas);
        }
        return examen;
    }

    @Override
    public Examen guardarExamen(Examen examen) {
        if(!examen.getPreguntas().isEmpty()){
            preguntaRepository.guardarVarias(examen.getPreguntas());
        }
        return examenRepository.guardar(examen);
    }

}

