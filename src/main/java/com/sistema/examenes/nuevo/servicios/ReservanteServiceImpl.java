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
    public ApiResponse<Reservante> validar(Reservante reservante) {
        Errors errors = new BeanPropertyBindingResult(reservante, "reservante");
        ValidationUtils.invokeValidator(validator, reservante, errors);
        if (errors.hasErrors()) {
        	return new ApiResponse<>(false,errors.getAllErrors().toString(),null);
        } else {
        	return new ApiResponse<>(true,"".toString(),reservante);
        }
    }
    private ApiResponse<Reservante> save(Reservante reservante){
    	Reservante guardado = reservanteRepo.save(reservante);
		return (guardado!=null ? new ApiResponse<>(true,"",guardado) 
				: new ApiResponse<>(false,"Error al guardar la Reserva",null));
	}	
	
	
	
	
	
	
	@Override
	public ApiResponse<Reservante> guardarReservante(Reservante reservante) {
		ApiResponse<Reservante> response = obtenerPorTelefonoYUsuario(reservante);
		if(!response.isSuccess()) {
			ApiResponse<Reservante> resp = validar(reservante);
			if(resp.isSuccess()) {
				return save(resp.getData());
			}
			return new ApiResponse<>(false,"Error al guardar el reservante, "+resp.getMessage(),null);	
		}
		return new ApiResponse<>(true,"El reservante ya esta guardado.",response.getData());
		/*
		try {
			ApiResponse<Reservante> existente = obtenerPorTelefonoYUsuario(reservante);// reservanteRepo.findByTelefono(reservaStr.getReservante().getTelefono()); 
			if(existente.isSuccess()) {
				return new ApiResponse<>(true,"",existente.getData());//reservanteRepo.save(reservaStr.getReservante());
			}else {
				Reservante nuevoReservante  = reservanteRepo.save(reservante);
				return new ApiResponse<>(true,"",nuevoReservante);
			}	
		}catch(Exception e){
			return new ApiResponse<>(false,e.getMessage(),null);
		}*/
	}

	@Override
	public ApiResponse<Reservante> obtenerPorTelefonoYUsuario(Reservante reservante) {
		try {
			Reservante r = reservanteRepo.findByTelefonoAndUsuario(reservante.getTelefono(), reservante.getUsuario());
			if(r!=null) {
				return new ApiResponse<>(true,"",r);
			}
			return new ApiResponse<>(false,"No se obtuvo el Reservante",r);	
		}catch(Exception e){
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}

	@Override
	public ApiResponse<Reservante> editarReservante(Reservante reservante, long idUsuario) {
		Reservante r = reservanteRepo.getById(reservante.getId());
		if(r.getUsuario().getId()==idUsuario) {
			reservante.setReservas(r.getReservas());
			reservante.setUsuario(r.getUsuario());
			return guardarReservante(reservante);
		}
		return new ApiResponse<>(false,"Usuario no autorizado",null);	
	}

	@Override
	public ApiResponse<List<Reservante>> listarReservanteDeUsuario(Usuario usuario) {
		try {
			List<Reservante> reservantes = reservanteRepo.findByUsuario(usuario);
			return new ApiResponse<>(true,"",reservantes);
		}catch(Exception e){
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}

}
