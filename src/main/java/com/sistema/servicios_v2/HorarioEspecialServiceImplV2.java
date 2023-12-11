package com.sistema.servicios_v2;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.anterior.modelo.Horario;
import com.sistema.anterior.modelo.HorarioEspecial;
import com.sistema.anterior.modelo.Recurso;
import com.sistema.anterior.repositorios.HorarioEspecialRepository;

@Service
public class HorarioEspecialServiceImplV2 implements HorarioEspecialServiceV2{

	@Autowired
	private HorarioEspecialRepository horarioEspRepo;
	
	private final Validator validator;
	
    public HorarioEspecialServiceImplV2(Validator validator) {
        this.validator = validator;
    }
	
	@Override
	public Recurso guardarHorariosEspecialesRecurso(Recurso r) throws Exception {
		Set<HorarioEspecial> horariosGuardados = new HashSet<>();
		r.setHorariosEspeciales(setRecurso(r,r.getHorariosEspeciales()));
		validarLista(r.getHorariosEspeciales());
		for(HorarioEspecial h : r.getHorariosEspeciales()) {
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
	
	@Override
	public void validarLista(Set<HorarioEspecial> horarios) throws Exception {
		for(HorarioEspecial h : horarios) {
			validar(h);
		}
		noSeSuperponen(horarios);
	}
	
    private void noSeSuperponen(Set<HorarioEspecial> horarios) throws Exception {
        for (HorarioEspecial horario1 : horarios) {
            for (HorarioEspecial horario2 : horarios) {
                if (horario1 != horario2) {
                    // Comprueba si los horarios se superponen
                    if (horariosSeSuperponen(horario1, horario2) && horario1.getFecha().equals(horario2.getFecha())) {
                        throw new Exception("Hay horarios que se superponen"); // Si se superponen, devuelve false
                    }
                }
            }
        }
    }

    private static boolean horariosSeSuperponen(HorarioEspecial horario1, HorarioEspecial horario2) {
        if((horario1.isCerrado() || horario2.isCerrado())) {
        	return true;
        }
    	return horario1.getDesde().isBefore(horario2.getHasta()) &&
               horario1.getHasta().isAfter(horario2.getDesde());
    }
    
    private Set<HorarioEspecial> setRecurso(Recurso r, Set<HorarioEspecial> horarios) {
    	for(HorarioEspecial he : horarios) {
    		he.setRecurso(r);
    	}
    	return horarios;
    }

}
