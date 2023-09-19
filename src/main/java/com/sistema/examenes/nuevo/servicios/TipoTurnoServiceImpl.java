package com.sistema.examenes.nuevo.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.anterior.repositorios.TipoTurnoRepository;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios_interfaces.TipoTurnoService;

@Service
public class TipoTurnoServiceImpl implements TipoTurnoService{
	
	@Autowired
	private TipoTurnoRepository tipoTurnoRepo;
	
	private final Validator validator;
	
    public TipoTurnoServiceImpl(Validator validator) {
        this.validator = validator;
    }
    
    
    public ApiResponse<TipoTurno> validar(TipoTurno tipoTurno) {
        Errors errors = new BeanPropertyBindingResult(tipoTurno, "tipoTurno");
        ValidationUtils.invokeValidator(validator, tipoTurno, errors);
        if (errors.hasErrors()) {
        	//System.out.println(errors.getAllErrors());
        	return new ApiResponse<>(false,errors.getAllErrors().toString(),null);
        } else {
        	//System.out.println("---------------EXITO---------------");
        	return new ApiResponse<>(true,"".toString(),tipoTurno);
        	//return save(recursoDTO);
        }
    }
    
    private ApiResponse<TipoTurno> save(TipoTurno t){
    	TipoTurno guardado = tipoTurnoRepo.save(t);
		return (guardado!=null ? new ApiResponse<>(true,"",guardado) 
				: new ApiResponse<>(false,"Error al guardar el Tipo de Turno",null));
	}
     
    
    

	@Override
	public ApiResponse<TipoTurno> guardarTipoTurno(TipoTurno tipoTurno) {
		ApiResponse<TipoTurno> resp = validar(tipoTurno);
		if(resp.isSuccess()) {
			return save(resp.getData());
		}else {
			return new ApiResponse<>(false,"Error al guardar el Tipo de Turno, "+resp.getMessage(),null);
		}/*
		
		
		try {
			if(tipoTurno.sonValidosLosDatos() && tipoTurno.tieneLosDatosMinimos()) {
				TipoTurno t = tipoTurnoRepo.save(tipoTurno);
				if(t!=null) {
					return new ApiResponse<>(true,"",t);
				}
				return new ApiResponse<>(false,"Error al guardar el Tipo de Turno",null);
			}
			return new ApiResponse<>(false,"Datos inválidos",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}*/
	}

	@Override
	public ApiResponse<TipoTurno> editarTipoTurno(TipoTurno tipoTurno) {
		ApiResponse<TipoTurno> guardado = obtenerTipoTurnoPorId(tipoTurno.getId());
		if(guardado.isSuccess()) {
			//si el usuario es el mismo que el usuario nuevo entonces validar y guardar
			if(guardado.getData().getUsuario() == tipoTurno.getUsuario()) {
				tipoTurno.setRecursosTipoTurno(guardado.getData().getRecursosTipoTurno());
				return guardarTipoTurno(tipoTurno);//save
			}else {
				return new ApiResponse<>(false,"Usuario no autorizado a editar el Tipo de Turno.",null);
			}
		}else {
			return guardado;
		}
		
		/*
		try {
			ApiResponse<TipoTurno> response = obtenerTipoTurnoPorId(tipoTurno.getId());
			if(response.isSuccess()) {
				return (response.getData().getUsuario().getId()==idUsuario ? guardarTipoTurno(tipoTurno) 
						: new ApiResponse<>(false,"Usuario no autorizado",null));
			}
			return response;
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}*/
	}

	@Override
	public ApiResponse<List<TipoTurno>> listarTipoTurnoDeUsuario(Usuario usuario) {
		try {
			List<TipoTurno> t = tipoTurnoRepo.findByUsuario(usuario);
			if(t.size()>0) {
				return new ApiResponse<>(true,"",t);
			}
			return new ApiResponse<>(false,"Error al obtener los Tipos de Turno del usuario",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}

	@Override
	public ApiResponse<TipoTurno> obtenerTipoTurnoPorId(long idTipoTurno) {
		try {
			TipoTurno t = tipoTurnoRepo.getById(idTipoTurno);
			if(t!=null) {
				return new ApiResponse<>(true,"",t);
			}
			return new ApiResponse<>(false,"Error al obtener el Tipo de Turno",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}
	
	

}
