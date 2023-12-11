package com.sistema.examenes.servicios.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sistema.anterior.modelo.Reservante;
import com.sistema.modelo.usuario.Usuario;
import com.sistema.servicios_v2.ReservanteServiceImplV2;
import com.sistema.servicios_v2.ReservanteServiceV2;

@SpringBootTest
public class ReservanteServiceTest {

	@Autowired
    private ReservanteServiceV2 reservanteService;
	
	
	private Usuario usuario(){
		Usuario u = new Usuario();
		u.setApellido("Ariel");
		u.setNombre("David");
		u.setEmail("matias-oggero@hotmail.com");
		u.setTelefono("3533403876");
		u.setPassword("asdasdasdas");
		u.setDni("42511692");
		u.setRequiereReservanteConDni(true);
		u.setRequiereReservanteConNombreYApellido(true);
		u.setRequiereReservanteConTelefono(true);
		return u;
	}
	
	@Test
    public void testValidacionFallida() throws NoSuchMethodException, SecurityException {
	
        // Crea un objeto de modelo con datos inválidos
		Reservante reservanteSinDni = new Reservante();
		reservanteSinDni.setApellido("asd");
		reservanteSinDni.setNombre("asdd");
		reservanteSinDni.setTelefono("123123123123");
		reservanteSinDni.setHabilitado(true);
		reservanteSinDni.setUsuario(usuario());
        
		Reservante reservanteSinTelefono = new Reservante();
		reservanteSinTelefono.setApellido("asd");
		reservanteSinTelefono.setNombre("asdd");
		reservanteSinTelefono.setDni("123123123123");
		reservanteSinTelefono.setHabilitado(true);
		reservanteSinTelefono.setUsuario(usuario());
        
		Reservante reservanteSinNombreApellido = new Reservante();
		reservanteSinNombreApellido.setApellido("asd");
		reservanteSinNombreApellido.setDni("81898787");
		reservanteSinNombreApellido.setTelefono("123123123123");
		reservanteSinNombreApellido.setHabilitado(true);
		reservanteSinNombreApellido.setUsuario(usuario());
        
        // Usa reflexión para acceder al método privado
        Method method = ReservanteServiceImplV2.class.getDeclaredMethod("validar", Reservante.class);
        method.setAccessible(true);
        try {
            // Llama al método del servicio que realiza la validación
        	method.invoke(reservanteService, reservanteSinDni);
            fail("Prueba 1 : La validación debería haber fallado y lanzado una excepción.");
        } catch (Exception e) {
        	 // La excepción fue lanzada, lo que es correcto
        	try {
        		method.invoke(reservanteService, reservanteSinTelefono);
        		fail("Prueba 2 : La validación debería haber fallado y lanzado una excepción.");
        	}catch(Exception e2) {
        		// La excepción fue lanzada, lo que es correcto
            	try {
            		method.invoke(reservanteService, reservanteSinNombreApellido);
            		fail("Prueba 3 : La validación debería haber fallado y lanzado una excepción.");
            	}catch(Exception e3) {
            		// La excepción fue lanzada, lo que es correcto
            	}	
        	}
        }
    }
	
	
    @Test
    public void testValidacionValida() throws NoSuchMethodException, SecurityException {
    	// Crea un objeto de modelo con datos inválidos
    	Usuario usuario = usuario();
    	
		Reservante reservanteValido = new Reservante();
		reservanteValido.setApellido("asd");
		reservanteValido.setNombre("asdd");
		reservanteValido.setTelefono("123123123123");
		reservanteValido.setDni("41242442");
		reservanteValido.setHabilitado(true);
		reservanteValido.setUsuario(usuario);
        
        // Usa reflexión para acceder al método privado
		Method method = ReservanteServiceImplV2.class.getDeclaredMethod("validar", Reservante.class);
        method.setAccessible(true);
        
        try {
        	method.invoke(reservanteService, reservanteValido);
        	try {
        		
        		usuario.setRequiereReservanteConDni(false);
        		reservanteValido.setDni(null);
        		
        		method.invoke(reservanteService, reservanteValido);
        		/*
        		 * ESTO NO PORQUE POR AHORA MINIMAMENTE SE REQUIERE TELEFONO Y NOMBRE-APELLIDO
            	try {
            		
            		usuario.setTelefono(null);
            		reservanteValido.setTelefono(null);
            		
            		method.invoke(reservanteService, reservanteValido);
                	try {
                		
                		usuario.setRequiereReservanteConNombreYApellido(false);
                		reservanteValido.setNombre(null);
                		reservanteValido.setApellido(null);
                		
                		method.invoke(reservanteService, reservanteValido);
                	}catch(Exception e) {
                		fail("Prueba 4 : La validación debería ser exitosa, no debería lanzar una excepción."+e.getMessage());
                	}	
            	}catch(Exception e) {
            		fail("Prueba 3 : La validación debería ser exitosa, no debería lanzar una excepción."+e.getMessage());
            	}
            	*/	
        	}catch(InvocationTargetException e) {
        		Throwable cause = e.getCause(); // Obtiene la excepción real lanzada dentro del método
        	    cause.printStackTrace(); // Imprime la causa de la excepción
        		fail("Prueba 2 : La validación debería ser exitosa, no debería lanzar una excepción."+e.getMessage());
        	}
        } catch (Exception e) {
        	fail("Prueba 1 : La validación debería ser exitosa, no debería lanzar una excepción."+e.getMessage());
        }
    }
}
