package com.sistema.servicios_v2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.anterior.modelo.Recurso;
import com.sistema.anterior.modelo.Reserva;
import com.sistema.anterior.modelo.TipoTurno;
import com.sistema.anterior.repositorios.TipoTurnoRepository;
import com.sistema.modelo.usuario.Usuario;

@Service
public class TipoTurnoServiceImplV2 implements TipoTurnoServiceV2{

	@Autowired
	private TipoTurnoRepository tipoTurnoRepo;
	@Autowired
	private ReservaServiceV2 reservaService;
	@Autowired
	private AsignacionServiceV2 asignacionService;
	
	private final Validator validator;
	
    public TipoTurnoServiceImplV2(Validator validator) {
        this.validator = validator;
    }
	
	@Override
	public TipoTurno obtenerTipoTurnoPorId(Long id) throws Exception{
		TipoTurno t = tipoTurnoRepo.getById(id);
		if(t!=null) {
			return t;
		}
		throw new Exception("Tipo Turno no encontrado");
	}

	@Override
	public List<TipoTurno> listarTipoTurno(Usuario u) {
		return tipoTurnoRepo.findByUsuario(u);
	}

	@Override
	public void eliminarTipoTurno(Long id, Usuario u) throws Exception{
		TipoTurno t = obtenerTipoTurnoPorId(id);
		if(t.getUsuario().equals(u)) {
			eliminarReservas(t.obtenerReservas(), u);
			eliminarAsignaciones(t.getRecursosTipoTurno(), u);
			t.setEliminado(true);
			tipoTurnoRepo.save(t);
		}else {
			throw new Exception("Usuario no autorizado");
		}
	}

	@Override
	public TipoTurno nuevoTipoTurno(TipoTurno t) throws Exception {
		existeTipoDeTurnoConEseNombre(t);
		validar(t);
		return tipoTurnoRepo.save(t);
	}

	@Override
	public TipoTurno editarTipoTurno(TipoTurno t, Usuario u) throws Exception{
		TipoTurno guardado = obtenerTipoTurnoPorId(t.getId());
		if(guardado.getUsuario().equals(u)) {
			existeTipoDeTurnoConEseNombre(t);
			validar(t);
			TipoTurno editado = guardado.editarTipoTurno(t);
			return tipoTurnoRepo.save(editado);
		}else {
			throw new Exception("Usuario no autorizado");
		}
	}
	
	@Override
	public void borrarTipoTurno(Long idTipoTurno) throws Exception {
		try {
			tipoTurnoRepo.deleteById(idTipoTurno);	
		}catch(Exception e) {
			throw new Exception("Error al eliminar el Tipo de Turno");
		}
	}
	
	private void validar(TipoTurno t) throws Exception{
		Errors errors = new BeanPropertyBindingResult(t, "tipoTurno");
        ValidationUtils.invokeValidator(validator, t, errors);
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
	
	private void existeTipoDeTurnoConEseNombre(TipoTurno t) throws Exception {
		//Boolean b = !listarTipoTurno(t.getUsuario()).stream().filter( tipoTurno -> tipoTurno.getNombre().equals(t.getNombre())).toList().isEmpty();
		if(!listarTipoTurno(t.getUsuario()).stream().filter( tipoTurno -> 
		tipoTurno.getNombre().equals(t.getNombre()) && !tipoTurno.getId().equals(t.getId()) ).toList().isEmpty()){
			throw new Exception("Ya existe un Tipo de Turno con ese nombre");
		}
	}

}
