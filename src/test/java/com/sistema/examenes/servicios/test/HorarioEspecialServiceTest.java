package com.sistema.examenes.servicios.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sistema.anterior.modelo.HorarioEspecial;
import com.sistema.servicios_v2.HorarioEspecialServiceImplV2;
import com.sistema.servicios_v2.HorarioEspecialServiceV2;

@SpringBootTest
public class HorarioEspecialServiceTest {

	@Autowired
    private HorarioEspecialServiceV2 horarioEspService;
	
	//fecha menor a la actual
	//hora desde mayor a hora hasta
	//hora desde y hora hasta menor a 60 minutos
	@Test
    public void testValidacionIndividualFallida() throws NoSuchMethodException, SecurityException {
        // Crea un objeto de modelo con datos inválidos
		HorarioEspecial objetoDesdeMayorAHasta = new HorarioEspecial();
        objetoDesdeMayorAHasta.setFecha(LocalDate.of(2025, 1, 1));
        objetoDesdeMayorAHasta.setDesde(LocalTime.of(16, 00));
        objetoDesdeMayorAHasta.setHasta(LocalTime.of(15, 00));
        objetoDesdeMayorAHasta.setCerrado(false);
        
        HorarioEspecial objetoDiferenciaDesdeHastaMenorA60Min = new HorarioEspecial();
        objetoDesdeMayorAHasta.setFecha(LocalDate.of(2025, 1, 1));
        objetoDiferenciaDesdeHastaMenorA60Min.setDesde(LocalTime.of(16, 00));
        objetoDiferenciaDesdeHastaMenorA60Min.setHasta(LocalTime.of(16, 59));
        objetoDiferenciaDesdeHastaMenorA60Min.setCerrado(false);
        
        HorarioEspecial fechaMenorAActual = new HorarioEspecial();
        fechaMenorAActual.setFecha(LocalDate.of(2000, 1, 1));
        fechaMenorAActual.setDesde(LocalTime.of(16, 00));
        fechaMenorAActual.setHasta(LocalTime.of(16, 59));
        fechaMenorAActual.setCerrado(false);
        
        // Usa reflexión para acceder al método privado
        Method method = HorarioEspecialServiceImplV2.class.getDeclaredMethod("validar", HorarioEspecial.class);
        method.setAccessible(true);
        try {
            // Llama al método del servicio que realiza la validación
        	method.invoke(horarioEspService, objetoDesdeMayorAHasta);
            fail("Prueba 1 : La validación debería haber fallado y lanzado una excepción.");
        } catch (Exception e) {
        	 // La excepción fue lanzada, lo que es correcto
        	try {
        		method.invoke(horarioEspService, objetoDiferenciaDesdeHastaMenorA60Min);
        		fail("Prueba 2 : La validación debería haber fallado y lanzado una excepción.");
        	}catch(Exception e2) {
        		// La excepción fue lanzada, lo que es correcto
            	try {
            		method.invoke(horarioEspService, fechaMenorAActual);
            		fail("Prueba 3 : La validación debería haber fallado y lanzado una excepción.");
            	}catch(Exception e3) {
            		// La excepción fue lanzada, lo que es correcto
            	}	
        	}
        }
    }
	
	//horarioEspecial cerrado y horarioEspecial no cerrado para una misma fecha
	//horarioEspecial se superponen para la misma fecha
	@Test
    public void testValidacionListaFallida() throws NoSuchMethodException, SecurityException {
        // Crea un objeto de modelo con datos inválidos
		Set<HorarioEspecial> horariosQueSePisan = new HashSet<>();
		
		HorarioEspecial horario1 = new HorarioEspecial();
        horario1.setFecha(LocalDate.of(2025, 10, 10));
        horario1.setDesde(LocalTime.of(16, 00));
        horario1.setHasta(LocalTime.of(20, 00));
        horario1.setCerrado(false);
        
        HorarioEspecial horario2 = new HorarioEspecial();
        horario2.setFecha(LocalDate.of(2025, 10, 10));
        horario2.setDesde(LocalTime.of(19, 59));
        horario2.setHasta(LocalTime.of(23, 00));
        horario2.setCerrado(false);
        
        horariosQueSePisan.add(horario1);
        horariosQueSePisan.add(horario2);

        
        // Usa reflexión para acceder al método privado
        //Method method = HorarioServiceImplV2.class.getDeclaredMethod("validarLista", Horario.class);
        //method.setAccessible(true);
        try {
            // Llama al método del servicio que realiza la validación
        	horarioEspService.validarLista(horariosQueSePisan);
            fail("Prueba 1 : La validación debería haber fallado y lanzado una excepción.");
        } catch (Exception e) {
        	 // La excepción fue lanzada, lo que es correcto
        	Set<HorarioEspecial> horariosQueSePisan2 = new HashSet<>();
        	
        	HorarioEspecial horario3 = new HorarioEspecial();
        	horario3.setFecha(LocalDate.of(2025, 10, 10));
            horario3.setCerrado(true);
            
            HorarioEspecial horario4 = new HorarioEspecial();
            horario4.setFecha(LocalDate.of(2025, 10, 10));
            horario4.setDesde(LocalTime.of(11, 00));
            horario4.setHasta(LocalTime.of(19, 00));
            horario4.setCerrado(false);
            
            horariosQueSePisan2.add(horario3);
            horariosQueSePisan2.add(horario4);
            
            try {
            	horarioEspService.validarLista(horariosQueSePisan2);
                fail("Prueba 2 : La validación debería haber fallado y lanzado una excepción.");
            }catch(Exception e2) {
            	// La excepción fue lanzada, lo que es correcto
            }
        }
    }

	
    @Test
    public void testValidacionValida() throws NoSuchMethodException, SecurityException {
    	// Crea un objeto de modelo con datos inválidos
    	HorarioEspecial objetoValido = new HorarioEspecial();
        objetoValido.setFecha(LocalDate.of(2025, 10, 10));
        objetoValido.setDesde(LocalTime.of(16, 00));
        objetoValido.setHasta(LocalTime.of(19, 00));
        objetoValido.setCerrado(false);
        
        // Usa reflexión para acceder al método privado
        Method method = HorarioEspecialServiceImplV2.class.getDeclaredMethod("validar", HorarioEspecial.class);
        method.setAccessible(true);
        
        try {
        	method.invoke(horarioEspService, objetoValido);
        } catch (Exception e) {
        	fail("La validación debería ser exitosa, no debería lanzar una excepción.");
        }
    }
    
	@Test
    public void testValidacionListaValida() throws NoSuchMethodException, SecurityException {
        // Crea un objeto de modelo con datos válidos
		Set<HorarioEspecial> horariosQueNoSePisan = new HashSet<>();
		
		HorarioEspecial horario1 = new HorarioEspecial();
        horario1.setFecha(LocalDate.of(2025, 10, 10));
        horario1.setDesde(LocalTime.of(16, 00));
        horario1.setHasta(LocalTime.of(20, 00));
        horario1.setCerrado(false);
        
		HorarioEspecial horario2 = new HorarioEspecial();
        horario2.setFecha(LocalDate.of(2025, 10, 10));
        horario2.setDesde(LocalTime.of(20, 00));
        horario2.setHasta(LocalTime.of(23, 00));
        horario2.setCerrado(false);
        
        HorarioEspecial horario3 = new HorarioEspecial();
        horario3.setFecha(LocalDate.of(2025, 11, 10));
        horario3.setCerrado(true);
        
        horariosQueNoSePisan.add(horario1);
        horariosQueNoSePisan.add(horario2);
        horariosQueNoSePisan.add(horario3);
        
        try {
        	horarioEspService.validarLista(horariosQueNoSePisan);
            try {
            	Set<HorarioEspecial> horariosQueNoSePisan2 = new HashSet<>();
            	
            	HorarioEspecial horario5 = new HorarioEspecial();
                horario5.setFecha(LocalDate.of(2025, 10, 10));
                horario5.setDesde(LocalTime.of(16, 00));
                horario5.setHasta(LocalTime.of(23, 00));
                horario5.setCerrado(false);
                
                HorarioEspecial horario4 = new HorarioEspecial();
                horario4.setFecha(LocalDate.of(2026, 10, 10));
                horario4.setDesde(LocalTime.of(16, 00));
                horario4.setHasta(LocalTime.of(23, 00));
                horario4.setCerrado(false);
                
                horariosQueNoSePisan2.add(horario5);
                horariosQueNoSePisan2.add(horario4);
                
            	horarioEspService.validarLista(horariosQueNoSePisan2);
            }catch(Exception e) {
            	fail("Prueba 2 : La validación debería ser exitosa, no debería lanzar una excepción. Mensaje de Excepcion: "+e.getMessage());
            }
        	
        } catch (Exception e) {
        	fail("Prueba 1 : La validación debería ser exitosa, no debería lanzar una excepción. Mensaje de Excepcion: "+e.getMessage());
        }
    }
    
}
