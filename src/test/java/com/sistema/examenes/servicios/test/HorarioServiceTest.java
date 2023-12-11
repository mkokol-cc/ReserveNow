package com.sistema.examenes.servicios.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sistema.anterior.modelo.Dias;
import com.sistema.anterior.modelo.Horario;
import com.sistema.servicios_v2.HorarioServiceImplV2;
import com.sistema.servicios_v2.HorarioServiceV2;

@SpringBootTest
public class HorarioServiceTest {

	@Autowired
    private HorarioServiceV2 horarioService;
	
	@Test
    public void testValidacionIndividualFallida() throws NoSuchMethodException, SecurityException {
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
            fail("Prueba 1 : La validación debería haber fallado y lanzado una excepción.");
        } catch (Exception e) {
        	 // La excepción fue lanzada, lo que es correcto
        	try {
        		method.invoke(horarioService, objetoDiferenciaDesdeHastaMenorA60Min);
        		fail("Prueba 2 : La validación debería haber fallado y lanzado una excepción.");
        	}catch(Exception e2) {
        		// La excepción fue lanzada, lo que es correcto
        	}
        }
    }
	
	@Test
    public void testValidacionListaFallida() throws NoSuchMethodException, SecurityException {
        // Crea un objeto de modelo con datos inválidos
		Set<Horario> horariosQueSePisan = new HashSet<>();
		
        Horario horario1 = new Horario();
        horario1.setDia(Dias.LUNES);
        horario1.setDesde(LocalTime.of(16, 00));
        horario1.setHasta(LocalTime.of(20, 00));
        
        Horario horario2 = new Horario();
        horario2.setDia(Dias.LUNES);
        horario2.setDesde(LocalTime.of(19, 59));
        horario2.setHasta(LocalTime.of(23, 00));
        
        horariosQueSePisan.add(horario1);
        horariosQueSePisan.add(horario2);

        
        // Usa reflexión para acceder al método privado
        //Method method = HorarioServiceImplV2.class.getDeclaredMethod("validarLista", Horario.class);
        //method.setAccessible(true);
        try {
            // Llama al método del servicio que realiza la validación
        	horarioService.validarLista(horariosQueSePisan);
            fail("Prueba 1 : La validación debería haber fallado y lanzado una excepción.");
        } catch (Exception e) {
        	 // La excepción fue lanzada, lo que es correcto
        	Set<Horario> horariosQueSePisan2 = new HashSet<>();
        	
            Horario horario3 = new Horario();
            horario3.setDia(Dias.MARTES);
            horario3.setDesde(LocalTime.of(10, 00));
            horario3.setHasta(LocalTime.of(20, 00));
            
            Horario horario4 = new Horario();
            horario4.setDia(Dias.MARTES);
            horario4.setDesde(LocalTime.of(11, 00));
            horario4.setHasta(LocalTime.of(19, 00));
            
            horariosQueSePisan2.add(horario3);
            horariosQueSePisan2.add(horario4);
            
            try {
            	horarioService.validarLista(horariosQueSePisan2);
                fail("Prueba 2 : La validación debería haber fallado y lanzado una excepción.");
            }catch(Exception e2) {
            	// La excepción fue lanzada, lo que es correcto
            }
        }
    }

	
    @Test
    public void testValidacionValida() throws NoSuchMethodException, SecurityException {
    	// Crea un objeto de modelo con datos inválidos
        Horario objetoValido = new Horario();
        objetoValido.setDia(Dias.MARTES);
        objetoValido.setDesde(LocalTime.of(16, 00));
        objetoValido.setHasta(LocalTime.of(19, 00));
        
        // Usa reflexión para acceder al método privado
        Method method = HorarioServiceImplV2.class.getDeclaredMethod("validar", Horario.class);
        method.setAccessible(true);
        
        try {
        	method.invoke(horarioService, objetoValido);
        } catch (Exception e) {
        	fail("La validación debería ser exitosa, no debería lanzar una excepción.");
        }
    }
    
	@Test
    public void testValidacionListaValida() throws NoSuchMethodException, SecurityException {
        // Crea un objeto de modelo con datos válidos
		Set<Horario> horariosQueNoSePisan = new HashSet<>();
		
        Horario horario1 = new Horario();
        horario1.setDia(Dias.LUNES);
        horario1.setDesde(LocalTime.of(16, 00));
        horario1.setHasta(LocalTime.of(20, 00));
        
        Horario horario2 = new Horario();
        horario2.setDia(Dias.LUNES);
        horario2.setDesde(LocalTime.of(20, 00));
        horario2.setHasta(LocalTime.of(23, 00));
        
        horariosQueNoSePisan.add(horario1);
        horariosQueNoSePisan.add(horario2);
        
        try {
        	horarioService.validarLista(horariosQueNoSePisan);
        	
        	Set<Horario> horariosQueNoSePisan2 = new HashSet<>();
        	
            Horario horario3 = new Horario();
            horario3.setDia(Dias.JUEVES);
            horario3.setDesde(LocalTime.of(16, 00));
            horario3.setHasta(LocalTime.of(23, 00));
            
            Horario horario4 = new Horario();
            horario4.setDia(Dias.MARTES);
            horario4.setDesde(LocalTime.of(16, 00));
            horario4.setHasta(LocalTime.of(23, 00));
            
            horariosQueNoSePisan.add(horario3);
            horariosQueNoSePisan.add(horario4);
            
            try {
            	horarioService.validarLista(horariosQueNoSePisan2);
            }catch(Exception e) {
            	fail("Prueba 2 : La validación debería ser exitosa, no debería lanzar una excepción. Mensaje de Excepcion: "+e.getMessage());
            }
        	
        } catch (Exception e) {
        	fail("Prueba 1 : La validación debería ser exitosa, no debería lanzar una excepción. Mensaje de Excepcion: "+e.getMessage());
        }
    }
    
}
