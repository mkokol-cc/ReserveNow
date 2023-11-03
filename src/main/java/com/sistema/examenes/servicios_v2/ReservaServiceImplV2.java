package com.sistema.examenes.servicios_v2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.anterior.repositorios.ReservaRepository;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios_interfaces.EstadoService;

public class ReservaServiceImplV2 implements ReservaServiceV2{
	
	@Autowired
	private ReservaRepository reservaRepo;
	@Autowired
	private ReservanteServiceV2 reservanteService;
	@Autowired
	private EstadoService estadoService;
	@Autowired
	private AsignacionServiceV2 asignacionService;
	
	private final Validator validator;
	
    public ReservaServiceImplV2(Validator validator) {
        this.validator = validator;
    }
	
	@Override
	public Reserva eliminarReserva(Reserva r, Usuario u) throws Exception {
		return reservaRepo.save(estadoService.eliminarReserva(r));
	}

	@Override
	public void borrarReserva(Long idReserva) throws Exception {
		Reserva r = obtenerReservaPorId(idReserva);
		reservaRepo.delete(r);
	}

	@Override
	public Reserva editarReserva(Reserva r, Usuario u) throws Exception {
		Reserva guardada = obtenerReservaPorId(r.getId());
		if(r.getAsignacionTipoTurno().getRecurso().getUsuario().equals(u) && r.getAsignacionTipoTurno().getTipoTurno().getUsuario().equals(u)) {
			validar(r);
			guardada.editarReserva(r);
			return reservaRepo.save(estadoService.nuevoCambioEstadoReserva(guardada, guardada.obtenerUltimoCambioEstado().getEstadoNuevo()));
		}
		throw new Exception("Usuario no autorizado");
	}

	@Override
	public Reserva nuevaReserva(Reserva r) throws Exception {
		validar(estadoService.estadoReservaNueva(r));
		Reservante reservante = reservanteService.nuevoReservante(r.getReservante());
		r.setReservante(reservante);
		validar(r);
		return reservaRepo.save(r);
	}

	@Override
	public List<Reserva> listarReservas(Usuario u) {
		List<Reserva> reservas = new ArrayList<>();
		for(AsignacionRecursoTipoTurno a : asignacionService.listarAsignaciones(u)) {
			List<Reserva> aux = reservaRepo.findByAsignacionTipoTurno(a);
			reservas.addAll(aux);
		}
		return reservas;
	}

	@Override
	public Reserva obtenerReservaPorId(Long id) throws Exception {
		Reserva r = reservaRepo.getById(id);
		if(r!=null) {
			return r;
		}
		throw new Exception("Reserva no encontrada");
	}
	
	
	
	private void validar(Reserva r) throws Exception{
		Errors errors = new BeanPropertyBindingResult(r, "reserva");
        ValidationUtils.invokeValidator(validator, r, errors);
        if (errors.hasErrors()) {
        	throw new Exception(errors.getFieldError().getDefaultMessage().toString());
        }
	}
	

}
