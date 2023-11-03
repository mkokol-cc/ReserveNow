package com.sistema.examenes.servicios_v2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.repositorios.RecursoRepository;
import com.sistema.examenes.modelo.usuario.Usuario;

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
		validar(r);
		return recursoRepo.save(r);
	}

	@Override
	public Recurso editarRecurso(Recurso r, Usuario u) throws Exception {
		Recurso guardado = obtenerRecursoPorId(r.getId());
		if(r.getUsuario().equals(u)) {
			validar(r);
			Recurso recursoEditado = recursoRepo.save(guardado.editarRecurso(r));
			return guardarHorariosRecurso(recursoEditado);
		}else {
			throw new Exception("Usuario no autorizado");
		}
	}


	@Override
	public void borrarRecurso(Long idRecurso) throws Exception {
		// TODO Auto-generated method stub
		
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

}
