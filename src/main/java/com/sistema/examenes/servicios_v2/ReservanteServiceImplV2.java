package com.sistema.examenes.servicios_v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.anterior.repositorios.ReservanteRepository;

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

	private void validar(Reservante r) throws Exception{
		Errors errors = new BeanPropertyBindingResult(r, "reservante");
        ValidationUtils.invokeValidator(validator, r, errors);
        if (errors.hasErrors()) {
        	throw new Exception(errors.getFieldError().getDefaultMessage().toString());
        }
	}
}
