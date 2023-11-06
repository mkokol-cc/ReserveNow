package com.sistema.examenes.servicios.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.servicios_v2.RecursoServiceImplV2;
import com.sistema.examenes.servicios_v2.RecursoServiceV2;

@SpringBootTest
public class RecursoServiceTest {

	@Autowired
    private RecursoServiceV2 recursoService;
	
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
	//usuario no nulo
	@Test
	public void testValidacionFallida() throws NoSuchMethodException, SecurityException {
		
		//usuario no nulo
		Recurso recurso = new Recurso();
		recurso.setNombre("asd");
        
        // Usa reflexión para acceder al método privado
        Method method = RecursoServiceImplV2.class.getDeclaredMethod("validar", Recurso.class);
        method.setAccessible(true);
        try {
        	method.invoke(recursoService, recurso);
            fail("Prueba 1 : La validación debería haber fallado y lanzado una excepción.");
        } catch (Exception e) {
        	//nombre entre 2 y 30 caracteres
        	recurso.setNombre("a");
        	recurso.setUsuario(usuario());
        	try {
        		method.invoke(recursoService, recurso);
        		fail("Prueba 2 : La validación debería haber fallado y lanzado una excepción.");
        	}catch(Exception e2) {
        		//paso el test
        	}
        }
    }
	
    @Test
    public void testValidacionValida() throws NoSuchMethodException, SecurityException {
		Recurso recurso = new Recurso();
		recurso.setNombre("asd");
		recurso.setUsuario(usuario());
        
        // Usa reflexión para acceder al método privado
        Method method = RecursoServiceImplV2.class.getDeclaredMethod("validar", Recurso.class);
        method.setAccessible(true);
        
        try {
        	method.invoke(recursoService, recurso);
        } catch (Exception e) {
	        Throwable cause = e.getCause(); // Obtiene la excepción real lanzada dentro del método
	        cause.printStackTrace(); // Imprime la causa de la excepción
        	fail("Prueba 1 : La validación debería ser exitosa, no debería lanzar una excepción."+e.getMessage());
        }
    }
}
