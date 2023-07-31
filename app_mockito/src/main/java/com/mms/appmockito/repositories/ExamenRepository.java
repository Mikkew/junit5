package com.mms.appmockito.repositories;

import com.mms.appmockito.models.Examen;

import java.util.List;

public interface ExamenRepository {

    public List<Examen> findAll();

    public Examen guardar(Examen examen);
}
