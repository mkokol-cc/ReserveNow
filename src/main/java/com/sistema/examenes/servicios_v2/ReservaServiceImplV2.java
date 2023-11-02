package com.sistema.examenes.servicios_v2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.anterior.repositorios.ReservaRepository;
import com.sistema.examenes.modelo.usuario.Usuario;

public class ReservaServiceImplV2 implements ReservaServiceV2{
	
	@Autowired
	private ReservaRepository reservaRepo;
	
	private final Validator validator;
	
    public ReservaServiceImplV2(Validator validator) {
        this.validator = validator;
    }
	
	@Override
	public Reserva eliminarReserva(Reserva r, Usuario u) {
		// TODO Auto-generated method stub
		return null;
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
			return reservaRepo.save(guardada.editarReserva(r));
		}
		throw new Exception("Usuario no autorizado");
	}

	@Override
	public Reserva nuevaReserva(Reserva r) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reserva> listarReservas(Usuario u) {
		// findByUsuario en reservaRepo
		return null;
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
		Errors errors = new BeanPropertyBindingResult(r, "tipoTurno");
        ValidationUtils.invokeValidator(validator, r, errors);
        if (errors.hasErrors()) {
        	throw new Exception(errors.getFieldError().getDefaultMessage().toString());
        }
	}
	

}
