package com.sistema.examenes.servicios_v2;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.examenes.anterior.modelo.HorarioEspecial;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.repositorios.HorarioEspecialRepository;

public class HorarioEspecialServiceImplV2 implements HorarioEspecialServiceV2{

	@Autowired
	private HorarioEspecialRepository horarioEspRepo;
	
	private final Validator validator;
	
    public HorarioEspecialServiceImplV2(Validator validator) {
        this.validator = validator;
    }
	
	@Override
	public Recurso guardarHorarioEspecialRecurso(Recurso r) throws Exception {
		Set<HorarioEspecial> horariosGuardados = new HashSet<>();
		for(HorarioEspecial h : r.getHorariosEspeciales()) {
			validar(h);
			horariosGuardados.add(horarioEspRepo.save(h));
		}
		r.setHorariosEspeciales(horariosGuardados);
		return r;
	}
	
	private void validar(HorarioEspecial h) throws Exception{
		Errors errors = new BeanPropertyBindingResult(h, "horarioEspecial");
        ValidationUtils.invokeValidator(validator, h, errors);
        if (errors.hasErrors()) {
        	throw new Exception(errors.getFieldError().getDefaultMessage().toString());
        }
	}

}
