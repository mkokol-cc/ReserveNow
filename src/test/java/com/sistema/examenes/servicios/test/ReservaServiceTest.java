package com.sistema.examenes.servicios.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Dias;
import com.sistema.examenes.anterior.modelo.Horario;
import com.sistema.examenes.anterior.modelo.HorarioEspecial;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.servicios_v2.ReservaServiceImplV2;
import com.sistema.examenes.servicios_v2.ReservaServiceV2;

@SpringBootTest
public class ReservaServiceTest {

	@Autowired
    private ReservaServiceV2 reservaService;
	
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
	
	private AsignacionRecursoTipoTurno asignacion(){
		AsignacionRecursoTipoTurno asig = new AsignacionRecursoTipoTurno();
		asig.setDuracionEnMinutos(30);
		asig.setCantidadConcurrencia(2);
		asig.setEliminado(false);
		asig.setSeniaCtvos(0);
		return asig;
	}
	
	private Recurso recurso() {
		Recurso r = new Recurso();
		r.setEliminado(false);
		r.setNombre("Recurso");
		r.setUsuario(usuario());;
		return r;
	}
	
	private TipoTurno tipoTurno() {
		TipoTurno t = new TipoTurno();
		t.setEliminado(false);
		t.setUsuario(usuario());
		t.setNombre("Recurso");
		return t;
	}
	
	private Horario horario(LocalTime desde, LocalTime hasta, Dias d) {
		Horario horario = new Horario();
		horario.setDesde(desde);
		horario.setHasta(hasta);
		horario.setDia(d);
		return horario;
	}
	
	private HorarioEspecial horarioEspecial(Boolean cerrado, LocalTime desde, LocalTime hasta, LocalDate dia) {
		HorarioEspecial horario = new HorarioEspecial();
		horario.setDesde(desde);
		horario.setHasta(hasta);
		horario.setFecha(dia);
		horario.setCerrado(cerrado);
		return horario;
	}
	
	private Reservante reservante(Boolean habilitado) {
		Reservante r = new Reservante();
		r.setApellido("Oggero");
		r.setNombre("Matias");
		r.setDni("42511692");
		r.setHabilitado(habilitado);
		r.setTelefono("3533403876");
		r.setUsuario(usuario());
		return r;
	}
	
	//fecha y hora mayor a la actual
	//la reserva debe tener un horario de acuerdo a los horarios del recurso de la asignacion a reservar
	//el recurso ademas debe estar LIBRE para el horario seleccionado
	//tanto la asignacion como el tipo de turno y recurso asociado a ella deben estar habilitados
	//el reservante de la reserva debe estar habilitado para reservar
	@Test
    public void testValidacionFallida() throws NoSuchMethodException, SecurityException {
	
		Set<Horario> horarios = new HashSet<>();
		Set<HorarioEspecial> horariosEspeciales = new HashSet<>();
		horarios.add(horario(LocalTime.of(10, 0),LocalTime.of(20, 0),Dias.LUNES));
		horariosEspeciales.add(horarioEspecial(false,LocalTime.of(10, 0),LocalTime.of(20, 0),LocalDate.of(2025, 5, 5)));
		horariosEspeciales.add(horarioEspecial(true,null,null,LocalDate.of(2025, 10, 10)));
		
		Recurso r = recurso();
		r.setHorarios(horarios);
		r.setHorariosEspeciales(horariosEspeciales);
		
		AsignacionRecursoTipoTurno asignacion = asignacion();
		asignacion.setRecurso(r);
		asignacion.setTipoTurno(tipoTurno());
		
		Set<Reserva> reservasDeOtraAsignacion = new HashSet<>();
		Reserva r1 = new Reserva();
		r1.setHora(LocalTime.of(10,0));
		r1.setHoraFin(LocalTime.of(20, 0));
		r1.setFecha(LocalDate.of(2024, 3, 4));
		reservasDeOtraAsignacion.add(r1);
		
		AsignacionRecursoTipoTurno otraAsignacionDelRecurso = asignacion();
		otraAsignacionDelRecurso.setRecurso(r);
		otraAsignacionDelRecurso.setReservas(reservasDeOtraAsignacion);
		
        // Crea un objeto de modelo con datos inválidos
		
		//fecha y hora NO mayor a la actual
		Reserva reservaConFechaHoraInvalida = new Reserva();
		reservaConFechaHoraInvalida.setAsignacionTipoTurno(asignacion);
		reservaConFechaHoraInvalida.setReservante(reservante(true));;
		reservaConFechaHoraInvalida.setFecha(LocalDate.of(2023, 1, 2));;
		reservaConFechaHoraInvalida.setHora(LocalTime.of(10, 0));
		reservaConFechaHoraInvalida.setHoraFin(reservaConFechaHoraInvalida.getHora().plusMinutes(reservaConFechaHoraInvalida.getAsignacionTipoTurno().getDuracionEnMinutos()));
		
        // Usa reflexión para acceder al método privado
        Method method = ReservaServiceImplV2.class.getDeclaredMethod("validar", Reserva.class);
        method.setAccessible(true);
        try {
            // Llama al método del servicio que realiza la validación
        	method.invoke(reservaService, reservaConFechaHoraInvalida);
            fail("Prueba 1 : La validación debería haber fallado y lanzado una excepción.");
        } catch (Exception e) {
        	// La excepción fue lanzada, lo que es correcto
        	System.out.println("Prueba 1 : Se lanzo correctamente una excepcion con el mensaje: "+e.getMessage());
        	
        	//la reserva debe tener un horario de acuerdo a los horarios del recurso de la asignacion a reservar
    		reservaConFechaHoraInvalida.setFecha(LocalDate.of(2025, 5, 5));
    		reservaConFechaHoraInvalida.setHora(LocalTime.of(10, 5));
    		reservaConFechaHoraInvalida.setHoraFin(reservaConFechaHoraInvalida.getHora().plusMinutes(reservaConFechaHoraInvalida.getAsignacionTipoTurno().getDuracionEnMinutos()));
    		
        	try {
        		method.invoke(reservaService, reservaConFechaHoraInvalida);
        		fail("Prueba 2 : La validación debería haber fallado y lanzado una excepción.");
        	}catch(Exception e2) {
        		// La excepción fue lanzada, lo que es correcto
        		System.out.println("Prueba 2 : Se lanzo correctamente una excepcion con el mensaje: "+e2.getMessage());
        		
        		//el recurso ademas debe estar LIBRE para el horario seleccionado
        		reservaConFechaHoraInvalida.setFecha(LocalDate.of(2024, 3, 4));
        		reservaConFechaHoraInvalida.setHora(LocalTime.of(10, 0));
        		
            	try {
            		method.invoke(reservaService, reservaConFechaHoraInvalida);
            		fail("Prueba 3 : La validación debería haber fallado y lanzado una excepción.");
            	}catch(Exception e3) {
            		// La excepción fue lanzada, lo que es correcto
            		System.out.println("Prueba 3 : Se lanzo correctamente una excepcion con el mensaje: "+e3.getMessage());
            		//tanto la asignacion como el tipo de turno y recurso asociado a ella deben estar habilitados
            		reservaConFechaHoraInvalida.setFecha(LocalDate.of(2024, 3, 11));
            		reservaConFechaHoraInvalida.getAsignacionTipoTurno().getTipoTurno().setEliminado(true);
            		try {
            			method.invoke(reservaService, reservaConFechaHoraInvalida);
                		fail("Prueba 4 : La validación debería haber fallado y lanzado una excepción.");
            		}catch(Exception e4) {
                		// La excepción fue lanzada, lo que es correcto
                		System.out.println("Prueba 4 : Se lanzo correctamente una excepcion con el mensaje: "+e4.getMessage());
                		//el reservante de la reserva debe estar habilitado para reservar
                		reservaConFechaHoraInvalida.getAsignacionTipoTurno().getTipoTurno().setEliminado(false);
                		reservaConFechaHoraInvalida.setReservante(reservante(false));
                		try {
                			method.invoke(reservaService, reservaConFechaHoraInvalida);
                    		fail("Prueba 5 : La validación debería haber fallado y lanzado una excepción.");
                		}catch(Exception e5) {
                			// La excepción fue lanzada, lo que es correcto
                    		System.out.println("Prueba 5 : Se lanzo correctamente una excepcion con el mensaje: "+e5.getMessage());
                		}
            		}
            	}	
        	}
        	
        }
    }

	@Test
	public void testValidacionValida() throws NoSuchMethodException, SecurityException {
	    System.out.println("-HABER SI MENTIS O DECIS LA VERDAD?-");
	    Set<Horario> horarios = new HashSet<>();
	    Set<HorarioEspecial> horariosEspeciales = new HashSet<>();
	    horarios.add(horario(LocalTime.of(10, 0), LocalTime.of(20, 0), Dias.LUNES));
	    horariosEspeciales.add(horarioEspecial(false, LocalTime.of(10, 0), LocalTime.of(20, 0), LocalDate.of(2025, 5, 5)));
	    horariosEspeciales.add(horarioEspecial(true, null, null, LocalDate.of(2025, 10, 10)));

	    Recurso r = recurso();
	    r.setHorarios(horarios);
	    r.setHorariosEspeciales(horariosEspeciales);

	    AsignacionRecursoTipoTurno asignacion = asignacion();

	    List<AsignacionRecursoTipoTurno> asignaciones = new ArrayList<>();
	    asignaciones.add(asignacion);
	    r.setRecursosTipoTurno(asignaciones);
	    asignacion.setRecurso(r);
	    asignacion.setTipoTurno(tipoTurno());

	    Set<Reserva> reservasDeAsignacion = new HashSet<>();
	    Reserva r1 = new Reserva();
	    r1.setHora(LocalTime.of(10, 0));
	    r1.setHoraFin(LocalTime.of(10, 30));
	    r1.setFecha(LocalDate.of(2024, 3, 4));
	    r1.setAsignacionTipoTurno(asignacion);
	    reservasDeAsignacion.add(r1);
	    asignacion.setReservas(reservasDeAsignacion);

	    // Crea un objeto de modelo con datos válidos
	    Reserva reservaValida = new Reserva();
	    reservaValida.setAsignacionTipoTurno(asignacion); // Utiliza la asignación configurada previamente
	    reservaValida.setReservante(reservante(true));
	    reservaValida.setFecha(LocalDate.of(2024, 3, 4));
	    reservaValida.setHora(LocalTime.of(10, 0));
	    reservaValida.setHoraFin(reservaValida.getHora().plusMinutes(asignacion.getDuracionEnMinutos())); // Utiliza la duración de la asignación

	    // Usa reflexión para acceder al método privado
	    Method method = ReservaServiceImplV2.class.getDeclaredMethod("validar", Reserva.class);
	    method.setAccessible(true);
	    try {
	        // Llama al método del servicio que realiza la validación
	        method.invoke(reservaService, reservaValida);
	    } catch (Exception e) {
	        Throwable cause = e.getCause(); // Obtiene la excepción real lanzada dentro del método
	        cause.printStackTrace(); // Imprime la causa de la excepción
	        fail("La validación NO debería haber lanzado una excepción. Mensaje: " + e.getMessage());
	    }
	}
	
}
