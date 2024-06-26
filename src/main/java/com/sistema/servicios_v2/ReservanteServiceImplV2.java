package com.sistema.servicios_v2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.anterior.modelo.Recurso;
import com.sistema.anterior.modelo.Reservante;
import com.sistema.anterior.repositorios.ReservanteRepository;
import com.sistema.modelo.usuario.Usuario;

@Service
public class ReservanteServiceImplV2 implements ReservanteServiceV2{

	@Autowired
	private ReservanteRepository reservanteRepo;
	
	private final Validator validator;
	
    public ReservanteServiceImplV2(Validator validator) {
        this.validator = validator;
    }
	
	@Override
	public Reservante nuevoReservante(Reservante r) throws Exception {
		Reservante resGuardado = reservanteRepo.findByTelefonoAndUsuario(r.getTelefono(), r.getUsuario());
		if(resGuardado!=null) {
			return resGuardado;
		}else {
			validar(r);
			return reservanteRepo.save(r);
		}
	}
	
	@Override
	public List<Reservante> listarReservantes(Usuario u) throws Exception {
		return reservanteRepo.findByUsuario(u);
	}
	
	@Override
	public Reservante editarReservante(Reservante r, Usuario u) throws Exception {
		Reservante guardado = reservanteRepo.getById(r.getId());
		if(guardado.getUsuario() == u) {
			r.setUsuario(u);
			validar(r);
			Reservante editado = guardado.editarReservante(r);
			return reservanteRepo.save(editado);
		}else {
			throw new Exception("Usuario no autorizado");
		}
	}

	private void validar(Reservante r) throws Exception{
		Errors errors = new BeanPropertyBindingResult(r, "reservante");
        ValidationUtils.invokeValidator(validator, r, errors);
        if (errors.hasErrors()) {
        	//System.out.println(errors.toString());
        	throw new Exception(errors.getFieldError().getDefaultMessage().toString());
        }
	}
}
