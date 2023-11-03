package com.sistema.examenes.servicios_v2;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.examenes.anterior.modelo.Horario;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.repositorios.HorarioRepository;

public class HorarioServiceImplV2 implements HorarioServiceV2{

	@Autowired
	private HorarioRepository horarioRepo;
	
	private final Validator validator;
	
    public HorarioServiceImplV2(Validator validator) {
        this.validator = validator;
    }
	
	@Override
	public Recurso guardarHorarioRecurso(Recurso r) throws Exception {
		Set<Horario> horariosGuardados = new HashSet<>();
		for(Horario h : r.getHorarios()) {
			validar(h);
			horariosGuardados.add(horarioRepo.save(h));
		}
		r.setHorarios(horariosGuardados);
		return r;
	}

	private void validar(Horario h) throws Exception{
		Errors errors = new BeanPropertyBindingResult(h, "horario");
        ValidationUtils.invokeValidator(validator, h, errors);
        if (errors.hasErrors()) {
        	throw new Exception(errors.getFieldError().getDefaultMessage().toString());
        }
	}
}
