package com.sistema.examenes.servicios.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.servicios_v2.TipoTurnoServiceImplV2;
import com.sistema.examenes.servicios_v2.TipoTurnoServiceV2;

@SpringBootTest
public class TipoTurnoServiceTest {

	@Autowired
    private TipoTurnoServiceV2 tipoTurnoService;
	
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
	
	//nombre entre 2 y 30 caracteres
	//duracion minima 10 minutos
	//precio desde menor o igual a precio hasta
	//precio seña, precio desde, precio hasta no negativo
	@Test
	public void testValidacionFallida() throws NoSuchMethodException, SecurityException {
		
		//precio desde menor o igual a precio hasta
		TipoTurno tipoTurno = new TipoTurno();
		tipoTurno.setNombre("anombre");
		tipoTurno.setDuracionEnMinutos(30);
		tipoTurno.setEliminado(false);
		tipoTurno.setPrecioEstimadoDesdeCtvos(10000);
		tipoTurno.setPrecioEstimadoHastaCtvos(5000);
		tipoTurno.setSeniaCtvos(0);
		tipoTurno.setUsuario(usuario());
        
        // Usa reflexión para acceder al método privado
        Method method = TipoTurnoServiceImplV2.class.getDeclaredMethod("validar", TipoTurno.class);
        method.setAccessible(true);
        try {
        	method.invoke(tipoTurnoService, tipoTurno);
            fail("Prueba 1 : La validación debería haber fallado y lanzado una excepción.");
        } catch (Exception e) {
        	//precio seña, precio desde, precio hasta no negativo
        	tipoTurno.setPrecioEstimadoDesdeCtvos(10000);
        	tipoTurno.setPrecioEstimadoHastaCtvos(20000);
        	tipoTurno.setSeniaCtvos(-10);
        	try {
        		method.invoke(tipoTurnoService, tipoTurno);
        		fail("Prueba 2 : La validación debería haber fallado y lanzado una excepción.");
        	}catch(Exception e2) {
        		//duracion en minutos no menor a 10 minutos
        		tipoTurno.setSeniaCtvos(0);
        		tipoTurno.setDuracionEnMinutos(9);
            	try {
            		method.invoke(tipoTurnoService, tipoTurno);
            		fail("Prueba 3 : La validación debería haber fallado y lanzado una excepción.");
            	}catch(Exception e3) {
            		//nombre entre 2 y 30 caracteres
            		tipoTurno.setDuracionEnMinutos(10);
            		tipoTurno.setNombre("s");
                	try {
                		method.invoke(tipoTurnoService, tipoTurno);
                		fail("Prueba 4 : La validación debería haber fallado y lanzado una excepción.");
                	}catch(Exception e4) {
                		//usuario nulo
                		tipoTurno.setNombre("sss");
                		tipoTurno.setUsuario(null);
                    	try {
                    		method.invoke(tipoTurnoService, tipoTurno);
                    		fail("Prueba 5 : La validación debería haber fallado y lanzado una excepción.");
                    	}catch(Exception e5) {
                    		// La excepción fue lanzada, lo que es correcto
                    	}
                	}
            	}	
        	}
        }
    }
	
    @Test
    public void testValidacionValida() throws NoSuchMethodException, SecurityException {
		TipoTurno tipoTurno = new TipoTurno();
		tipoTurno.setNombre("ab");
		tipoTurno.setDuracionEnMinutos(10);
		tipoTurno.setEliminado(true);
		tipoTurno.setPrecioEstimadoDesdeCtvos(10000);
		tipoTurno.setPrecioEstimadoHastaCtvos(10000);
		tipoTurno.setSeniaCtvos(100000);
		tipoTurno.setUsuario(usuario());
        
        // Usa reflexión para acceder al método privado
        Method method = TipoTurnoServiceImplV2.class.getDeclaredMethod("validar", TipoTurno.class);
        method.setAccessible(true);
        
        try {
        	method.invoke(tipoTurnoService, tipoTurno);
        } catch (Exception e) {
	        Throwable cause = e.getCause(); // Obtiene la excepción real lanzada dentro del método
	        cause.printStackTrace(); // Imprime la causa de la excepción
        	fail("Prueba 1 : La validación debería ser exitosa, no debería lanzar una excepción."+e.getMessage());
        }
    }
}
