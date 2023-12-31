package com.mms.appmockito.utils;

import com.mms.appmockito.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {

    public static final List<Examen> EXAMENES = Arrays.asList(
            new Examen(5L,"Matematicas"),
            new Examen(6L,"Lenguaje"),
            new Examen(7L,"Historia")
    );

    public static final List<Examen> EXAMENES_ID_NULL = Arrays.asList(
            new Examen("Matematicas"),
            new Examen("Lenguaje"),
            new Examen("Historia")
    );

    public static final List<Examen> EXAMENES_ID_NEGATIVOS = Arrays.asList(
            new Examen(-5L,"Matematicas"),
            new Examen(-6L,"Lenguaje"),
            new Examen(null,"Historia")
    );

    public static final List<String> PREGUNTAS = Arrays.asList(
            "Aritmetica", "Trigonometria", "Calculo Diferencial", "Calculo Integral",  "Geometria"
    );

    public final static Examen EXAMEN = new Examen(8L, "Fisica");
}
