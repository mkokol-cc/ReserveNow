package com.sistema.examenes.servicios_v2;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.anterior.repositorios.AsignacionRecursoTipoTurnoRepository;
import com.sistema.examenes.modelo.usuario.Usuario;

public class AsignacionServiceImplV2 implements AsignacionServiceV2{
	
	@Autowired
	private RecursoServiceV2 recursoService;
	@Autowired
	private TipoTurnoServiceV2 tipoTurnoService;
	@Autowired
	private ReservaServiceV2 reservaService;
	@Autowired
	private AsignacionRecursoTipoTurnoRepository asignacionRepo;
	
	private final Validator validator;
	
    public AsignacionServiceImplV2(Validator validator) {
        this.validator = validator;
    }
	
	@Override
	public AsignacionRecursoTipoTurno obtenerAsignacionPorId(Long idAsignacion) throws Exception {
		AsignacionRecursoTipoTurno asig = asignacionRepo.getById(idAsignacion);
		if(asig!=null) {
			return asig;
		}
		throw new Exception("Asignacion no encontrada");
	}
    
	@Override
	public AsignacionRecursoTipoTurno nuevaAsignacion(Long idRecurso, Long idTipoTurno, Usuario u) throws Exception {
		Recurso r = recursoService.obtenerRecursoPorId(idRecurso);
		TipoTurno t = tipoTurnoService.obtenerTipoTurnoPorId(idTipoTurno);
		if(r.getUsuario().equals(t.getUsuario()) && r.getUsuario().equals(u)) {
			AsignacionRecursoTipoTurno a = new AsignacionRecursoTipoTurno(r, t);
			validar(a);//validar tambien si el usuario esta habilitado y si el recurso y tipoturno estan habilitados
			return asignacionRepo.save(a);
		}
		throw new Exception("Usuario no autorizado");
	}

	@Override
	public AsignacionRecursoTipoTurno editarAsignacion(AsignacionRecursoTipoTurno asig, Usuario u) throws Exception {
		AsignacionRecursoTipoTurno guardado = obtenerAsignacionPorId(asig.getId());
		if(asig.getRecurso().getUsuario().equals(u) &&  asig.getTipoTurno().getUsuario().equals(u)) {
			validar(asig);
			return asignacionRepo.save(guardado.editarAsignacionRecursoTipoTurno(asig));
		}else {
			throw new Exception("Usuario no autorizado");
		}
	}

	@Override
	public List<AsignacionRecursoTipoTurno> listarAsignaciones(Usuario u) {
		return asignacionRepo.findByRecursoUsuarioId(u.getId());
	}

	@Override
	public void eliminarAsignacion(Long idAsignacion, Usuario u) throws Exception {
		AsignacionRecursoTipoTurno guardado = obtenerAsignacionPorId(idAsignacion);
		if(guardado.getRecurso().getUsuario().equals(u) &&  guardado.getTipoTurno().getUsuario().equals(u)) {
			guardado.setEliminado(true);
			validar(guardado);
			eliminarReservas(guardado.getReservas(),u);
			asignacionRepo.save(guardado);
		}else {
			throw new Exception("Usuario no autorizado");
		}
	}
	
	private void validar(AsignacionRecursoTipoTurno asig) throws Exception{
		Errors errors = new BeanPropertyBindingResult(asig, "asignacion");
        ValidationUtils.invokeValidator(validator, asig, errors);
        if (errors.hasErrors()) {
        	throw new Exception(errors.getFieldError().getDefaultMessage().toString());
        }
	}
	
	private void eliminarReservas(Set<Reserva> reservas, Usuario u) throws Exception {
		for(Reserva r : reservas) {
			if(!r.getEstado().isEsEstadoFinal()) {
				reservaService.eliminarReserva(r, u);
			}
		}
	}


}