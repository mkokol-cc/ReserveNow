package com.sistema.examenes.nuevo.servicios;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	@Override
	public ApiResponse<Recurso> guardarRecurso(Recurso recurso) {
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
		}
	}

	@Override
	public ApiResponse<Recurso> obtenerRecursoPorId(long idRecurso) {
		try {
			Recurso r = recursoRepo.getById(idRecurso);
			if(r!=null) {
				return new ApiResponse<>(true,"",r);
			}
			return new ApiResponse<>(false,"Error al obtener el Recurso",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}

	@Override
	public ApiResponse<Recurso> editarRecurso(Recurso recurso, long userId) {
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
		}
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
	}

}
