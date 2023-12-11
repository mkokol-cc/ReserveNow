package com.sistema.examenes.servicios.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sistema.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.anterior.modelo.Recurso;
import com.sistema.anterior.modelo.TipoTurno;
import com.sistema.modelo.usuario.Usuario;
import com.sistema.servicios_v2.AsignacionServiceImplV2;
import com.sistema.servicios_v2.AsignacionServiceV2;

@SpringBootTest
public class AsignacionServiceTest {

	@Autowired
    private AsignacionServiceV2 asignacionService;
	
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
	
	private Recurso recurso(boolean eliminado) {
		Recurso r = new Recurso();
		r.setEliminado(eliminado);
		r.setNombre("Recurso");
		r.setUsuario(usuario());
		return r;
	}
	
	private TipoTurno tipoTurno(boolean eliminado) {
		TipoTurno t = new TipoTurno();
		t.setEliminado(eliminado);
		t.setUsuario(usuario());
		t.setNombre("Recurso");
		return t;
	}
	
	//precio desde menor o igual a precio hasta
	//precio seña, precio desde, precio hasta no negativo
	//duracion en minutos no menor a 10 minutos
	//cantidad concurrencia no menor a 1
	//habilitado = true, siempre que su recurso y su tipo turno sean habilitado = true.
	@Test
    public void testValidacionFallida() throws NoSuchMethodException, SecurityException {
	
		//precio desde menor o igual a precio hasta
		AsignacionRecursoTipoTurno asignacion = new AsignacionRecursoTipoTurno();
		asignacion.setCantidadConcurrencia(1);
		asignacion.setDuracionEnMinutos(30);
		asignacion.setEliminado(false);
		asignacion.setPrecioEstimadoDesdeCtvos(10000);
		asignacion.setPrecioEstimadoHastaCtvos(5000);
		asignacion.setSeniaCtvos(0);
		asignacion.setTipoTurno(tipoTurno(false));
		asignacion.setRecurso(recurso(false));
        
        
        // Usa reflexión para acceder al método privado
        Method method = AsignacionServiceImplV2.class.getDeclaredMethod("validar", AsignacionRecursoTipoTurno.class);
        method.setAccessible(true);
        try {
        	method.invoke(asignacionService, asignacion);
            fail("Prueba 1 : La validación debería haber fallado y lanzado una excepción.");
        } catch (Exception e) {
        	//precio seña, precio desde, precio hasta no negativo
    		asignacion.setPrecioEstimadoDesdeCtvos(10000);
    		asignacion.setPrecioEstimadoHastaCtvos(20000);
    		asignacion.setSeniaCtvos(-10);
        	try {
        		method.invoke(asignacionService, asignacion);
        		fail("Prueba 2 : La validación debería haber fallado y lanzado una excepción.");
        	}catch(Exception e2) {
        		//duracion en minutos no menor a 10 minutos
        		asignacion.setSeniaCtvos(0);
        		asignacion.setDuracionEnMinutos(9);
            	try {
            		method.invoke(asignacionService, asignacion);
            		fail("Prueba 3 : La validación debería haber fallado y lanzado una excepción.");
            	}catch(Exception e3) {
            		//cantidad concurrencia no menor a 1
            		asignacion.setCantidadConcurrencia(0);
            		asignacion.setDuracionEnMinutos(10);
                	try {
                		method.invoke(asignacionService, asignacion);
                		fail("Prueba 4 : La validación debería haber fallado y lanzado una excepción.");
                	}catch(Exception e4) {
                		//habilitado = true, siempre que su recurso y su tipo turno sean habilitado = true.
                		asignacion.setCantidadConcurrencia(1);
                		asignacion.setTipoTurno(tipoTurno(true));
                    	try {
                    		method.invoke(asignacionService, asignacion);
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
    	// Crea un objeto de modelo con datos válidos
		AsignacionRecursoTipoTurno asignacion = new AsignacionRecursoTipoTurno();
		asignacion.setCantidadConcurrencia(1);
		asignacion.setDuracionEnMinutos(10);
		asignacion.setEliminado(false);
		asignacion.setPrecioEstimadoDesdeCtvos(10000);
		asignacion.setPrecioEstimadoHastaCtvos(10000);
		asignacion.setSeniaCtvos(0);
		asignacion.setTipoTurno(tipoTurno(false));
		asignacion.setRecurso(recurso(false));
        
        // Usa reflexión para acceder al método privado
        Method method = AsignacionServiceImplV2.class.getDeclaredMethod("validar", AsignacionRecursoTipoTurno.class);
        method.setAccessible(true);
        
        try {
        	method.invoke(asignacionService, asignacion);
        } catch (Exception e) {
        	fail("Prueba 1 : La validación debería ser exitosa, no debería lanzar una excepción."+e.getMessage());
        }
    }
}
