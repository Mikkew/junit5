package com.mms.appmockito.services.impl;

import com.mms.appmockito.models.Examen;
import com.mms.appmockito.repositories.ExamenRepository;
import com.mms.appmockito.repositories.PreguntaRepository;
import com.mms.appmockito.repositories.impl.ExamenRepositoryImpl;
import com.mms.appmockito.repositories.impl.PreguntasRepositoryImpl;
import com.mms.appmockito.services.IExamenService;
import org.junit.jupiter.api.*;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExamenServiceImplTest {

    @Mock
    private ExamenRepositoryImpl examenRepository;

    @Mock
    private PreguntasRepositoryImpl preguntasRepository;

    @Captor
    private ArgumentCaptor<Long> captor;

    @InjectMocks
    private ExamenServiceImpl service;

//    private IExamenService service;

//    @BeforeEach
//    public void setUp() {
//        examenRepository = mock(ExamenRepository.class);
//        pregruntasRepository = mock(PreguntaRepository.class);
//        service = new ExamenServiceImpl(examenRepository, pregruntasRepository);
//    }


//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    @Order(1)
    public void findExamenByNombre() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        Optional<Examen> examen = service.findExamenByNombre("Matematicas");

        assertNotNull(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.orElseThrow().getNombre());
    }

    @Test
    @Order(2)
    public void findExamenByNombreListaVacia() {
        List<Examen> datos = Collections.emptyList();
        when(examenRepository.findAll()).thenReturn(datos);
        Optional<Examen> examen = service.findExamenByNombre("Matematicas");

        assertFalse(examen.isPresent());
    }

    @Test
    @Order(3)
    public void testPreguntasExamen() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntasRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Trigonometria"));
    }

    @Test
    @Order(4)
    public void testPreguntasExamenVerify() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntasRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Trigonometria"));
        verify(examenRepository).findAll();
        verify(preguntasRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    @Order(5)
    @Disabled
    public void testNoExisteExamenVerify() {
        when(examenRepository.findAll()).thenReturn(Collections.emptyList());
        when(preguntasRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Trigonometria");
        assertNull(examen);
        verify(examenRepository).findAll();
        verify(preguntasRepository).findPreguntasPorExamenId(5L);
    }

    @Test
    @Order(6)
    public void testGuardarExamen() {
        //Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        when(examenRepository.guardar(any(Examen.class))).then(new Answer<Examen>() {

            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });

        //When
        Examen examen = service.guardarExamen(newExamen);

        //Then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());

        verify(examenRepository).guardar(any(Examen.class));
        verify(preguntasRepository).guardarVarias(anyList());
    }

    @Test
    @Order(7)
    public void testManejoException() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
        when(preguntasRepository.findPreguntasPorExamenId(isNull())).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            service.findExamenPorNombreConPreguntas("Matematicas");
        });

        verify(examenRepository).findAll();
        verify(preguntasRepository).findPreguntasPorExamenId(null);
    }

    @Test
    @Order(8)
    public void testArgumentsMatchers() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntasRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(examenRepository).findAll();
//        verify(preguntasRepository).findPreguntasPorExamenId(argThat(arg -> arg != null && arg.equals(5L)));
//        verify(preguntasRepository).findPreguntasPorExamenId(argThat(arg -> arg != null && arg >= 5L));
        verify(preguntasRepository).findPreguntasPorExamenId(eq(5L));
    }

    @Test
    @Order(9)
    @Disabled
    public void testArgumentsMatchers2() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES_ID_NEGATIVOS);
        when(preguntasRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(examenRepository).findAll();
        verify(preguntasRepository).findPreguntasPorExamenId(argThat(new MiArgsMatchers()));
    }

    @Test
    @Order(10)
    @Disabled
    public void testArgumentsMatchers3() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES_ID_NEGATIVOS);
        when(preguntasRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(examenRepository).findAll();
        verify(preguntasRepository).findPreguntasPorExamenId(argThat((argument) -> argument != null && argument > 0));
    }

    public static class MiArgsMatchers implements ArgumentMatcher<Long> {

        private Long argument;

        @Override
        public boolean matches(Long argument) {
            this.argument = argument;
            return argument != null && argument > 0;
        }

        @Override
        public String toString() {
            return "es para un mensaje personalizado de error que imprime " +
                    "mockito en caso de que falle el test " + argument +
                    " debe ser un emtero positivo";
        }
    }

    @Test
    @Order(11)
    public void testArgumentCaptor() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
//        when(preguntasRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matematicas");

//        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(preguntasRepository).findPreguntasPorExamenId(captor.capture());

        assertEquals(5L, captor.getValue());
    }

    @Test
    @Order(12)
    public void testDoThrow() {
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);
        doThrow(IllegalArgumentException.class).when(preguntasRepository).guardarVarias(anyList());
        assertThrows(IllegalArgumentException.class, () -> {
            service.guardarExamen(examen);
        });
    }

    @Test
    @Order(13)
    public void testDoAnswer() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
//        when(preguntasRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id == 5L ? Datos.PREGUNTAS : Collections.emptyList();
        }).when(preguntasRepository).findPreguntasPorExamenId(anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Geometria"));
        assertEquals(5L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
    }

    @Test
    @Order(14)
    public void testDoAnswerGuardarExamen() {
        //Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

//        when(examenRepository.guardar(any(Examen.class))).then(new Answer<Examen>() {
//
//            Long secuencia = 8L;
//
//            @Override
//            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
//                Examen examen = invocationOnMock.getArgument(0);
//                examen.setId(secuencia++);
//                return examen;
//            }
//        });

        doAnswer(new Answer<Examen>() {
            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        }).when(examenRepository).guardar(any(Examen.class));

        //When
        Examen examen = service.guardarExamen(newExamen);

        //Then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());

        verify(examenRepository).guardar(any(Examen.class));
        verify(preguntasRepository).guardarVarias(anyList());
    }

    @Test
    @Order(15)
    public void testDoCallRealMethod() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
//        when(preguntasRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        doCallRealMethod().when(preguntasRepository).findPreguntasPorExamenId(anyLong());
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
    }

    @Test
    @Order(16)
    public void testSpy() {
        ExamenRepository examRepository = spy(ExamenRepositoryImpl.class);
        PreguntaRepository preguntaRepository = spy(PreguntasRepositoryImpl.class);
        IExamenService examenService = new ExamenServiceImpl(examRepository, preguntaRepository);

        List<String> preguntas = Arrays.asList("Aritmetica");
//        when(preguntasRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
        doReturn(preguntas).when(preguntaRepository).findPreguntasPorExamenId(anyLong());
        Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Aritmetica"));

        verify(examRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    @Order(17)
    public void testOrdenDeInvocaciones() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        service.findExamenPorNombreConPreguntas("Matematicas");
        service.findExamenPorNombreConPreguntas("Lenguaje");

        InOrder inOrder = inOrder(preguntasRepository);
        inOrder.verify(preguntasRepository).findPreguntasPorExamenId(5L);
        inOrder.verify(preguntasRepository).findPreguntasPorExamenId(6L);
    }

    @Test
    @Order(18)
    public void testOrdenDeInvocaciones2() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        service.findExamenPorNombreConPreguntas("Matematicas");
        service.findExamenPorNombreConPreguntas("Lenguaje");

        InOrder inOrder = inOrder(examenRepository, preguntasRepository);
        inOrder.verify(examenRepository).findAll();
        inOrder.verify(preguntasRepository).findPreguntasPorExamenId(5L);

        inOrder.verify(examenRepository).findAll();
        inOrder.verify(preguntasRepository).findPreguntasPorExamenId(6L);
    }

    @Test
    @Order(19)
    public void testNumeroDeInvocaciones() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(preguntasRepository).findPreguntasPorExamenId(5L);
        verify(preguntasRepository, times(1)).findPreguntasPorExamenId(5L);
        verify(preguntasRepository, atLeast(1)).findPreguntasPorExamenId(5L);
        verify(preguntasRepository, atLeastOnce()).findPreguntasPorExamenId(5L);
        verify(preguntasRepository, atMost(1)).findPreguntasPorExamenId(5L);
        verify(preguntasRepository, atMostOnce()).findPreguntasPorExamenId(5L);
    }

    @Test
    @Order(20)
    public void testNumeroDeInvocaciones2() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        service.findExamenPorNombreConPreguntas("Matematicas");

//        verify(preguntasRepository).findPreguntasPorExamenId(5L);
        verify(preguntasRepository, times(2)).findPreguntasPorExamenId(5L);
        verify(preguntasRepository, atLeast(2)).findPreguntasPorExamenId(5L);
        verify(preguntasRepository, atLeastOnce()).findPreguntasPorExamenId(5L);
        verify(preguntasRepository, atMost(20)).findPreguntasPorExamenId(5L);
//        verify(preguntasRepository, atMostOnce()).findPreguntasPorExamenId(5L);
    }

    @Test
    @Order(20)
    public void testNumeroDeInvocaciones3() {
        when(examenRepository.findAll()).thenReturn(Collections.emptyList());
        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(preguntasRepository, never()).findPreguntasPorExamenId(5L);
        verifyNoInteractions(preguntasRepository);

        verify(examenRepository).findAll();
        verify(examenRepository, times(1)).findAll();
        verify(examenRepository, atLeast(1)).findAll();
        verify(examenRepository, atMostOnce()).findAll();
        verify(examenRepository, atMost(10)).findAll();
        verify(examenRepository, atMostOnce()).findAll();
    }

}