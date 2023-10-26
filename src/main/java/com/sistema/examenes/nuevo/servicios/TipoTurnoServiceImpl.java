package com.sistema.examenes.nuevo.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Estado;
import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.anterior.repositorios.TipoTurnoRepository;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios_interfaces.AsignacionRecursoTipoTurnoService;
import com.sistema.examenes.nuevo.servicios_interfaces.TipoTurnoService;

@Service
public class TipoTurnoServiceImpl implements TipoTurnoService{
	
	@Autowired
	private TipoTurnoRepository tipoTurnoRepo;
	@Autowired
	private AsignacionRecursoTipoTurnoService asignacionService;
	
	private final Validator validator;
	
    public TipoTurnoServiceImpl(Validator validator) {
        this.validator = validator;
    }
    
    
    public TipoTurno validar(TipoTurno tipoTurno) throws Exception {
        Errors errors = new BeanPropertyBindingResult(tipoTurno, "tipoTurno");
        ValidationUtils.invokeValidator(validator, tipoTurno, errors);
        if (errors.hasErrors()) {
        	throw new Exception(errors.getFieldError().getDefaultMessage().toString());
        } else {
        	return tipoTurno;
        }
    }
    
    private TipoTurno save(TipoTurno t) throws Exception {
    	TipoTurno guardado = tipoTurnoRepo.save(t);
    	if(guardado!=null) {
    		return guardado;
    	}
    	throw new Exception("Error al guardar el Tipo de Turno");
	}
     
    
    

	@Override
	public TipoTurno guardarTipoTurno(TipoTurno tipoTurno) throws Exception {
		return save(validar(tipoTurno));
	}

	@Override
	public TipoTurno editarTipoTurno(TipoTurno tipoTurno) throws Exception {
		TipoTurno guardado = obtenerTipoTurnoPorId(tipoTurno.getId());
		if(guardado.getUsuario() == tipoTurno.getUsuario()) {
			tipoTurno.setRecursosTipoTurno(guardado.getRecursosTipoTurno());
			return guardarTipoTurno(tipoTurno);//save
		}
		throw new Exception("Usuario no autorizado a editar el Tipo de Turno.");
	}

	@Override
	public List<TipoTurno> listarTipoTurnoDeUsuario(Usuario usuario) {
		return tipoTurnoRepo.findByUsuario(usuario);
	}

	@Override
	public TipoTurno obtenerTipoTurnoPorId(long idTipoTurno) throws Exception {
		TipoTurno t = tipoTurnoRepo.getById(idTipoTurno);
		if(t!=null) {
			return t;
		}
		throw new Exception("Error al obtener el Tipo de Turno");
	}
	
	
	
	
	@Override
	public void eliminarTipoTurno(Long idTipoTurno,Usuario u) throws Exception {
		String mensajeError="";
		TipoTurno t = tipoTurnoRepo.getById(idTipoTurno);
		for(AsignacionRecursoTipoTurno asig : t.getRecursosTipoTurno()) {
			try {
				asignacionService.eliminarAsignacion(asig, u);
			}catch(Exception ex) {
				mensajeError+=ex.getMessage();
			}
		}
		t.setEliminado(true);
		editarTipoTurno(t);
		if(!mensajeError.equals("")) {
			throw new Exception(mensajeError);
		}
	}
	
	

}
