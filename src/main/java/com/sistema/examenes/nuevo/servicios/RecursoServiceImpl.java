package com.sistema.examenes.nuevo.servicios;

import java.util.HashSet;
import java.util.List;
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
import com.sistema.examenes.anterior.repositorios.RecursoRepository;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios_interfaces.HorarioEspecialService;
import com.sistema.examenes.nuevo.servicios_interfaces.HorarioService;
import com.sistema.examenes.nuevo.servicios_interfaces.RecursoService;

@Service
public class RecursoServiceImpl implements RecursoService{

	@Autowired
	private RecursoRepository recursoRepo;
	
	@Autowired
	private HorarioService horarioService;
	
	@Autowired
	private HorarioEspecialService horarioEspService;
	
	private final Validator validator;
	
    public RecursoServiceImpl(Validator validator) {
        this.validator = validator;
    }
	
    
    
	public ApiResponse<Recurso> validar(Recurso recursoDTO) {
        Errors errors = new BeanPropertyBindingResult(recursoDTO, "recursoDTO");
        ValidationUtils.invokeValidator(validator, recursoDTO, errors);
        if (errors.hasErrors()) {
        	//System.out.println(errors.getAllErrors());
        	return new ApiResponse<>(false,errors.getAllErrors().toString(),null);
        } else {
        	//System.out.println("---------------EXITO---------------");
        	return new ApiResponse<>(true,"".toString(),recursoDTO);
        	//return save(recursoDTO);
        }
    }
	
	
	private ApiResponse<Recurso> save(Recurso r){
		Recurso guardado = recursoRepo.save(r);
		if(guardado!=null) {
			guardado.setHorarios(r.getHorarios());
			ApiResponse<Recurso> resp = setHorarios(guardado);
			guardado.setHorariosEspeciales(r.getHorariosEspeciales());
			ApiResponse<Recurso> respp = setHorariosEspeciales(guardado);
			return (resp.isSuccess() && respp.isSuccess() ? new ApiResponse<>(true,"",respp.getData()) : 
				new ApiResponse<>(false,"Error al guardar los horarios del Recurso",null));
		}
		return (guardado!=null ? new ApiResponse<>(true,"",guardado) 
				: new ApiResponse<>(false,"Error al cargar el Recurso",null));
	}
	
	@Override
	public ApiResponse<Recurso> guardarRecurso(Recurso recurso) {
		//Recurso r = recursoRepo.save(recurso);
		//if(r!=null) {
			//ApiResponse<Recurso> resp = guardarHorariosRecurso(recurso);
			//if(resp.isSuccess()) {
				//Recurso guardado = recursoRepo.save(resp.getData());
				//return new ApiResponse<>(true,"",guardado);
			//}
			//return new ApiResponse<>(false,resp.getMessage(),null);
		//}
		
		//obtener usuario
		ApiResponse<Recurso> resp = validar(recurso);
		if(resp.isSuccess()) {
			return save(resp.getData());
		}else {
			return new ApiResponse<>(false,"Error al cargar el Recurso, "+resp.getMessage(),null);
		}
		//return new ApiResponse<>(false,"Error al cargar el Recurso",null);
		
		/*
		try {
			if(recurso.getNombre().trim().equals("")) {
				return new ApiResponse<>(false,"Es obligatorio el campo Nombre para guardar el recurso",null);
			}
			Recurso r = recursoRepo.save(recurso);
			if(r!=null) {
				ApiResponse<Recurso> resp = guardarHorariosRecurso(recurso);
				if(resp.isSuccess()) {
					Recurso guardado = recursoRepo.save(resp.getData());
					return new ApiResponse<>(true,"",guardado);
				}
				return new ApiResponse<>(false,resp.getMessage(),null);
			}
			return new ApiResponse<>(false,"Error al cargar el Recurso",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}*/
	}

	@Override
	public ApiResponse<Recurso> obtenerRecursoPorId(long idRecurso) {
		Recurso r = recursoRepo.getById(idRecurso);
		return (r==null ? new ApiResponse<>(false,"No se encontro el Recurso con el id "+idRecurso+".",null) 
				: new ApiResponse<>(true,"",r));
	}

	@Override
	public ApiResponse<Recurso> editarRecurso(Recurso recurso) {
		//obtener por id
		ApiResponse<Recurso> respuesta = obtenerRecursoPorId(recurso.getId());
		if(respuesta.isSuccess()) {
			//si el usuario es el mismo que el usuario nuevo entonces validar y guardar
			if(respuesta.getData().getUsuario() == recurso.getUsuario()) {
				return guardarRecurso(recurso);//save
			}else {
				return new ApiResponse<>(false,"Usuario no autorizado a editar el Recurso.",null);
			}
		}else {
			return respuesta;
		}
		/*
		try {
			if(recurso.getNombre().trim().equals("")) {
				return new ApiResponse<>(false,"Es obligatorio el campo Nombre para guardar el recurso",null);
			}
			Recurso r = recursoRepo.getById(recurso.getId());
			if(r!=null) {
				if(r.getUsuario().getId()!=userId) {
					return new ApiResponse<>(false,"Usuario no autorizado",null);
				}
				r.setNombre(recurso.getNombre());
				r.setDescripcion(recurso.getDescripcion());
				r.setEliminado(recurso.isEliminado());
				r.setHorarios(recurso.getHorarios());
				r.setHorariosEspeciales(recurso.getHorariosEspeciales());
				ApiResponse<Recurso> resp = guardarHorariosRecurso(r);
				if(resp.isSuccess()) {
					Recurso guardado = recursoRepo.save(resp.getData());
					return new ApiResponse<>(true,"",guardado);
				}
				return new ApiResponse<>(false,resp.getMessage(),null);
			}
			return new ApiResponse<>(false,"Error al obtener el Recurso",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}*/
	}

	@Override
	public ApiResponse<List<Recurso>> listarRecurso(Usuario user) {
		try {
			List<Recurso> recursos = recursoRepo.findByUsuario(user);
			if(recursos.size()>0) {
				return new ApiResponse<>(true,"",recursos);
			}
			return new ApiResponse<>(false,"Error al obtener los Tipos de Turno del usuario",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}
	
	
	private ApiResponse<Recurso> guardarHorariosRecurso(Recurso r){
		String error = "";
		Set<Horario> horarios = new HashSet<>();
		for(Horario h : r.getHorarios()) {
			ApiResponse<Horario> resp = horarioService.guardarHorarioRecurso(h, r);
			if(!resp.isSuccess()) {
				error = error + resp.getMessage();
			}else {
				horarios.add(resp.getData());
			}
		}
		r.setHorarios(horarios);
		return (error.length()>0 ? new ApiResponse<>(false,error,r) 
				: new ApiResponse<>(true,error,r));
		/*
		if(!r.getHorarios().isEmpty()) {
			for(Horario h : r.getHorarios()) {
				h.setRecurso(r);
				horarioService.guardarHorarioRecurso(h, r)
			}
		}*/
		
		/*
		try {
			String message = "";
			ApiResponse<Recurso> resp = setHorarios(r);
			if(resp.isSuccess()) {
				message = message + resp.getMessage();
				Recurso recRes = resp.getData();
				ApiResponse<Recurso> respDos = setHorariosEspeciales(recRes);
				if(respDos.isSuccess()) {
					message = message + respDos.getMessage();
					return new ApiResponse<>(true,message,respDos.getData());
				}
				return new ApiResponse<>(false,"Error al cargar los horarios especiales. "+respDos.getMessage(),null);
			}
			return new ApiResponse<>(false,"Error al cargar los horarios comunes. "+resp.getMessage(),null);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
		
		*/
	}
	
	
	private ApiResponse<Recurso> setHorarios(Recurso r){
		try {
			if(!r.getHorarios().isEmpty()) {
				ApiResponse<Recurso> resp = horarioService.guardarListaHorariosRecurso(r);
				if(resp.isSuccess()) {
					return new ApiResponse<>(true,"",r);
				}
				return new ApiResponse<>(false,resp.getMessage(),null);
			}
			return new ApiResponse<>(true,"",r);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}
	
	
	private ApiResponse<Recurso> setHorariosEspeciales(Recurso r){
		String error = "";
		Set<HorarioEspecial> horarios = new HashSet<>();
		for(HorarioEspecial h : r.getHorariosEspeciales()) {
			h.setRecurso(r);
			ApiResponse<HorarioEspecial> resp = horarioEspService.guardarHorarioEspecial(h);
			if(!resp.isSuccess()) {
				error = error + resp.getMessage();
			}else {
				horarios.add(resp.getData());
			}
		}
		r.setHorariosEspeciales(horarios);
		return (error.length()>0 ? new ApiResponse<>(false,error,r) 
				: new ApiResponse<>(true,error,r));
		/*
		try {
			if(!r.getHorariosEspeciales().isEmpty()) {
				ApiResponse<Recurso> resp = horarioEspService.guardarListaHorarioEspecialRecurso(r);
				if(resp.isSuccess()) {
					return new ApiResponse<>(true,"",r);
				}
				return new ApiResponse<>(false,resp.getMessage(),null);
			}
			return new ApiResponse<>(true,"",r);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
		
		*/
	}

}
