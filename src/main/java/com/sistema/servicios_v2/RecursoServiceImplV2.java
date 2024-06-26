package com.sistema.servicios_v2;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.anterior.modelo.HorarioEspecial;
import com.sistema.anterior.modelo.Recurso;
import com.sistema.anterior.modelo.Reserva;
import com.sistema.anterior.repositorios.RecursoRepository;
import com.sistema.modelo.usuario.Usuario;

@Service
public class RecursoServiceImplV2 implements RecursoServiceV2{

	@Autowired
	private RecursoRepository recursoRepo;
	
	@Autowired
	private ReservaServiceV2 reservaService;
	
	@Autowired
	private AsignacionServiceV2 asignacionService;
	
	@Autowired
	private HorarioServiceV2 horarioService;
	
	@Autowired
	private HorarioEspecialServiceV2 horarioEspService;

	private final Validator validator;
	
    public RecursoServiceImplV2(Validator validator) {
        this.validator = validator;
    }
    
	@Override
	public Recurso obtenerRecursoPorId(Long id) throws Exception{
		Recurso r = recursoRepo.getById(id);
		if(r!=null) {
			return r;
		}
		throw new Exception("Recurso no encontrado");
	}

	@Override
	public List<Recurso> listarRecursos(Usuario u) {
		return recursoRepo.findByUsuario(u);
	}

	@Override
	public void eliminarRecurso(Long id, Usuario u) throws Exception {
		Recurso r = obtenerRecursoPorId(id);
		if(r.getUsuario().equals(u)) {
			eliminarReservas(r.obtenerReservas(), u);
			eliminarAsignaciones(r.getRecursosTipoTurno(),u);
			r.setEliminado(true);
			recursoRepo.save(r);
		}else {
			throw new Exception("Usuario no autorizado");
		}
	}

	@Override
	public Recurso nuevoRecurso(Recurso r) throws Exception {
		existeRecursoConEseNombre(r);
		validar(r);
		return recursoRepo.save(r);
	}

	@Override
	public Recurso editarRecurso(Recurso r, Usuario u) throws Exception {
		Recurso guardado = obtenerRecursoPorId(r.getId());
		if(guardado.getUsuario().equals(u)) {
			existeRecursoConEseNombre(r);
			validar(r);
			Recurso editado = guardado.editarRecurso(r);
			Recurso conHorariosGuadados = guardarHorariosRecurso(editado);
			return recursoRepo.save(conHorariosGuadados);
		}else {
			throw new Exception("Usuario no autorizado");
		}
	}


	@Override
	public void borrarRecurso(Long idRecurso) throws Exception {
		try {
			recursoRepo.deleteById(idRecurso);	
		}catch(Exception e) {
			throw new Exception("Error al eliminar el Recurso");
		}
	}
	
	private void validar(Recurso r) throws Exception{
		Errors errors = new BeanPropertyBindingResult(r, "recurso");
        ValidationUtils.invokeValidator(validator, r, errors);
        if (errors.hasErrors()) {
        	throw new Exception(errors.getFieldError().getDefaultMessage().toString());
        }
	}
	
	
	private void eliminarReservas(List<Reserva> reservas, Usuario u) throws Exception {
		for(Reserva r : reservas) {
			if(!r.getEstado().isEsEstadoFinal()) {
				reservaService.eliminarReserva(r, u);
			}
		}
	}
	
	private void eliminarAsignaciones(List<AsignacionRecursoTipoTurno> asignaciones, Usuario u) throws Exception {
		for(AsignacionRecursoTipoTurno asig : asignaciones) {
			asignacionService.eliminarAsignacion(asig.getId(), u);
		}
	}
	
	private Recurso guardarHorariosRecurso(Recurso r) throws Exception {
		horarioService.validarLista(r.getHorarios());
		horarioEspService.validarLista(r.getHorariosEspeciales());
		r = horarioService.guardarHorariosRecurso(r);
		r = horarioEspService.guardarHorariosEspecialesRecurso(r);
		actualizarReservas(r);
		return r;
	}
	
	private void actualizarReservas(Recurso r) throws Exception {
		for(Reserva reserva : r.obtenerReservas()) {
			if(!reserva.estaEnHorario()) {
				//solamente verificamos si esta en horario, ya que las verificaciones de concurrencia no deberian
				//tener nada que ver en el cambio de horario, solo se eliminan las reservas cuyo horario quedo fuera
				//del nuevo rango
				reservaService.eliminarReserva(reserva, r.getUsuario());
			}
		}
	}

	private void existeRecursoConEseNombre(Recurso r) throws Exception {
		//Boolean b = !listarTipoTurno(t.getUsuario()).stream().filter( tipoTurno -> tipoTurno.getNombre().equals(t.getNombre())).toList().isEmpty();
		if(!listarRecursos(r.getUsuario()).stream().filter( recurso -> 
		recurso.getNombre().equals(r.getNombre()) && !recurso.getId().equals(r.getId()) ).toList().isEmpty() ){
			throw new Exception("Ya existe un Recurso con ese nombre");
		}
	}
}
