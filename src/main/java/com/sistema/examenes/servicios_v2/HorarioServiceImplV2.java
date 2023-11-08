package com.sistema.examenes.servicios_v2;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.examenes.anterior.modelo.Horario;
import com.sistema.examenes.anterior.modelo.HorarioEspecial;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.repositorios.HorarioRepository;

@Service
public class HorarioServiceImplV2 implements HorarioServiceV2{

	@Autowired
	private HorarioRepository horarioRepo;
	
	private final Validator validator;
	
    public HorarioServiceImplV2(Validator validator) {
        this.validator = validator;
    }
	
	@Override
	public Recurso guardarHorariosRecurso(Recurso r) throws Exception {
		Set<Horario> horariosGuardados = new HashSet<>();
		r.setHorarios(setRecurso(r,r.getHorarios()));
		validarLista(r.getHorarios());
		for(Horario h : r.getHorarios()) {
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
	
	@Override
	public void validarLista(Set<Horario> horarios) throws Exception {
		for(Horario h : horarios) {
			validar(h);
		}
		noSeSuperponen(horarios);
	}
	
    private boolean noSeSuperponen(Set<Horario> horarios) throws Exception {
        for (Horario horario1 : horarios) {
            for (Horario horario2 : horarios) {
                if (horario1 != horario2) {
                    // Comprueba si los horarios se superponen
                    if (horariosSeSuperponen(horario1, horario2) && horario1.getDia().equals(horario2.getDia()) ) {
                        throw new Exception("Hay horarios que se superponen"); // Si se superponen, devuelve false
                    }
                }
            }
        }
        return true; // Si no se superponen, devuelve true
    }

    private static boolean horariosSeSuperponen(Horario horario1, Horario horario2) {
        return horario1.getDesde().isBefore(horario2.getHasta()) &&
               horario1.getHasta().isAfter(horario2.getDesde());
    }
    
    private Set<Horario> setRecurso(Recurso r, Set<Horario> horarios) {
    	for(Horario h : horarios) {
    		h.setRecurso(r);
    	}
    	return horarios;
    }
}
