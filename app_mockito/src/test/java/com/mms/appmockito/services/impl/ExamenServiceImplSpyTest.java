package com.mms.appmockito.services.impl;

import com.mms.appmockito.models.Examen;
import com.mms.appmockito.repositories.ExamenRepository;
import com.mms.appmockito.repositories.PreguntaRepository;
import com.mms.appmockito.repositories.impl.ExamenRepositoryImpl;
import com.mms.appmockito.repositories.impl.PreguntasRepositoryImpl;
import com.mms.appmockito.services.IExamenService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExamenServiceImplSpyTest {

    @Spy
    private ExamenRepositoryImpl examenRepository;

    @Spy
    private PreguntasRepositoryImpl preguntasRepository;

    @InjectMocks
    private ExamenServiceImpl service;

    @Test
    public void testSpy() {
//        ExamenRepository examRepository = spy(ExamenRepositoryImpl.class);
//        PreguntaRepository preguntaRepository = spy(PreguntasRepositoryImpl.class);
//        IExamenService examenService = new ExamenServiceImpl(examRepository, preguntaRepository);

        List<String> preguntas = Arrays.asList("Aritmetica");
//        when(preguntasRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
        doReturn(preguntas).when(preguntasRepository).findPreguntasPorExamenId(anyLong());
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Aritmetica"));

        verify(examenRepository).findAll();
        verify(preguntasRepository).findPreguntasPorExamenId(anyLong());
    }
}