package com.sistema.examenes.nuevo.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.anterior.repositorios.RecursoRepository;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios_interfaces.RecursoService;

@Service
public class RecursoServiceImpl implements RecursoService{

	@Autowired
	private RecursoRepository recursoRepo;
	
	@Override
	public ApiResponse<Recurso> guardarRecurso(Recurso recurso) {
		try {
			Recurso r = recursoRepo.save(recurso);
			if(r!=null) {
				return new ApiResponse<>(true,"",r);
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
			Recurso r = recursoRepo.getById(recurso.getId());
			if(r!=null) {
				if(r.getUsuario().getId()==userId) {
					return new ApiResponse<>(false,"Usuario no autorizado",null);
				}
				return new ApiResponse<>(true,"",r);
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

}
