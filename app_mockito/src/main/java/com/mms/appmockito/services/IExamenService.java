package com.mms.appmockito.services;

import com.mms.appmockito.models.Examen;

import java.util.Optional;

public interface IExamenService {

    public Optional<Examen> findExamenByNombre(String nombre);

    public Examen findExamenPorNombreConPreguntas(String nombre);

    public Examen guardarExamen(Examen examen);
}
