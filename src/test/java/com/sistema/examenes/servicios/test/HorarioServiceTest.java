package com.sistema.examenes.servicios.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sistema.examenes.anterior.modelo.Dias;
import com.sistema.examenes.anterior.modelo.Horario;
import com.sistema.examenes.servicios_v2.HorarioServiceImplV2;

@SpringBootTest
public class HorarioServiceTest {

	@Autowired
    private HorarioServiceImplV2 horarioService;
	
	@Test
    public void testValidacionFallida() throws NoSuchMethodException, SecurityException {
        // Crea un objeto de modelo con datos inválidos
        Horario objetoDesdeMayorAHasta = new Horario();
        objetoDesdeMayorAHasta.setDia(Dias.LUNES);
        objetoDesdeMayorAHasta.setDesde(LocalTime.of(16, 00));
        objetoDesdeMayorAHasta.setHasta(LocalTime.of(15, 00));
        
        Horario objetoDiferenciaDesdeHastaMenorA60Min = new Horario();
        objetoDiferenciaDesdeHastaMenorA60Min.setDia(Dias.LUNES);
        objetoDiferenciaDesdeHastaMenorA60Min.setDesde(LocalTime.of(16, 00));
        objetoDiferenciaDesdeHastaMenorA60Min.setHasta(LocalTime.of(16, 59));
        
        // Usa reflexión para acceder al método privado
        Method method = HorarioServiceImplV2.class.getDeclaredMethod("validar", Horario.class);
        method.setAccessible(true);
        try {
            // Llama al método del servicio que realiza la validación
        	method.invoke(horarioService, objetoDesdeMayorAHasta);
        	method.invoke(horarioService, objetoDiferenciaDesdeHastaMenorA60Min);
            fail("La validación debería haber fallado y lanzado una excepción.");
        } catch (Exception e) {
        	 // La excepción fue lanzada, lo que es correcto
        }
    }

	/*
    @Test
    public void testValidacionValida() {
        // Crea un objeto de modelo con datos válidos
        Modelo modeloInvalido = new Modelo();
        modeloInvalido.setAtributo(null); // Datos inválidos
        try {
        	// Llama al método del servicio que realiza la validación
            Modelo resultado1 = miClaseService.validarYRetornarModelo(modeloValido);
            // Asegúrate de que el resultado sea igual al objeto original
            assertEquals(modeloValido, resultado);
        } catch (Exception e) {
        	fail("La validación debería ser exitosa, no debería lanzar una excepción.");
        }
    }
    */
}
