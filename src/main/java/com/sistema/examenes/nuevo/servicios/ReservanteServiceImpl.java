package com.sistema.examenes.nuevo.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.anterior.repositorios.ReservanteRepository;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios_interfaces.ReservanteService;

@Service
public class ReservanteServiceImpl implements ReservanteService{
	
	@Autowired
	private ReservanteRepository reservanteRepo;

	private final Validator validator;
	
    public ReservanteServiceImpl(Validator validator) {
        this.validator = validator;
    }
    public Reservante validar(Reservante reservante) throws Exception {
        Errors errors = new BeanPropertyBindingResult(reservante, "reservante");
        ValidationUtils.invokeValidator(validator, reservante, errors);
        if (errors.hasErrors()) {
        	throw new Exception(errors.getFieldError().getDefaultMessage().toString());
        } else {
        	return reservante;
        }
    }
    private Reservante save(Reservante reservante) throws Exception{
    	Reservante guardado = reservanteRepo.save(reservante);
		if(guardado!=null) {
			return guardado;
		}
		throw new Exception("Error al guardar el Reservante en la base de datos");
	}	
	
	
	
	
	
	
	@Override
	public Reservante guardarReservante(Reservante reservante) throws Exception {
		Reservante reservanteGuardado = obtenerPorTelefonoYUsuario(reservante);
		if(reservanteGuardado==null) {
			return save(validar(reservante));
		}
		return reservanteGuardado;
	}

	@Override
	public Reservante obtenerPorTelefonoYUsuario(Reservante reservante) {
		Reservante r = reservanteRepo.findByTelefonoAndUsuario(reservante.getTelefono(), reservante.getUsuario());
		return r;
	}

	@Override
	public Reservante editarReservante(Reservante reservante, long idUsuario) throws Exception {
		Reservante r = reservanteRepo.getById(reservante.getId());
		if(r.getUsuario().getId()==idUsuario) {
			reservante.setReservas(r.getReservas());
			reservante.setUsuario(r.getUsuario());
			return guardarReservante(reservante);
		}
		throw new Exception("Usuario no autorizado");	
	}

	@Override
	public List<Reservante> listarReservanteDeUsuario(Usuario usuario) {
		return reservanteRepo.findByUsuario(usuario);
	}

}
