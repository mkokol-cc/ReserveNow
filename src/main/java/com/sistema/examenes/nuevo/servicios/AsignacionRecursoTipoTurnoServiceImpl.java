package com.sistema.examenes.nuevo.servicios;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Horario;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.anterior.repositorios.AsignacionRecursoTipoTurnoRepository;
import com.sistema.examenes.nuevo.servicios_interfaces.AsignacionRecursoTipoTurnoService;
import com.sistema.examenes.nuevo.servicios_interfaces.HorarioService;
import com.sistema.examenes.nuevo.servicios_interfaces.RecursoService;
import com.sistema.examenes.nuevo.servicios_interfaces.TipoTurnoService;

@Service
public class AsignacionRecursoTipoTurnoServiceImpl implements AsignacionRecursoTipoTurnoService{

	@Autowired
	private AsignacionRecursoTipoTurnoRepository asignacionRepo;
	
	@Autowired
	private RecursoService recursoService;
	
	@Autowired
	private TipoTurnoService tipoTurnoService;
	
	@Autowired
	private HorarioService horarioService;
	
	@Override
	public ApiResponse<AsignacionRecursoTipoTurno> guardarAsignacion(AsignacionRecursoTipoTurno asignacion, long idUsuario) {
		try {
		//obtener el recurso por id
		ApiResponse<Recurso> r = recursoService.obtenerRecursoPorId(asignacion.getRecurso().getId());
		//obtener el tipoturno por id
		ApiResponse<TipoTurno> t = tipoTurnoService.obtenerTipoTurnoPorId(asignacion.getTipoTurno().getId());
		//comprobar si el usuario es dueño de ambos
		if(t.isSuccess() && r.isSuccess()) {
			if(t.getData().getUsuario().getId()==idUsuario && r.getData().getUsuario().getId()==idUsuario) {
				//setear datos a asignacion
				asignacion.setRecurso(r.getData());
				asignacion.setTipoTurno(t.getData());
				AsignacionRecursoTipoTurno guardado = asignacionRepo.save(asignacion);
				return new ApiResponse<>(true,"",guardado);
			}
			return new ApiResponse<>(false,"Usuario no autorizado",null);
		}		
		return new ApiResponse<>(false,"Error obtener los datos para la asigancion",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,"Error: "+e.getMessage(),null);
		}
	}

	@Override
	public ApiResponse<AsignacionRecursoTipoTurno> editarAsignacion(AsignacionRecursoTipoTurno asignacion, long idUsuario) {
		try {
			ApiResponse<AsignacionRecursoTipoTurno> resp = obtenerPorId(asignacion.getId());
			if(resp.isSuccess()) {
				if(resp.getData().getRecurso().getUsuario().getId()==idUsuario) {
					asignacion.setRecurso(resp.getData().getRecurso());
					asignacion.setTipoTurno(resp.getData().getTipoTurno());
					ApiResponse<AsignacionRecursoTipoTurno> horarioResp = setHorarioAsignacion(asignacion);
					if(horarioResp.isSuccess()) {
						AsignacionRecursoTipoTurno guardado = asignacionRepo.save(asignacion);
						return new ApiResponse<>(true,"",guardado);
					}
					return new ApiResponse<>(false,horarioResp.getMessage(),null);	
				}
				return new ApiResponse<>(false,"Usuario no autorizado",null);	
			}
			return new ApiResponse<>(false,resp.getMessage(),null);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}

	@Override
	public ApiResponse<List<AsignacionRecursoTipoTurno>> listarAsignacion(Long id) {
		return null;/*
		try {
			List<AsignacionRecursoTipoTurno> lista = asignacionRepo.findByRecursoUsuarioId(id);
			return new ApiResponse<>(true,"",lista);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}*/
	}

	@Override
	public ApiResponse<List<AsignacionRecursoTipoTurno>> listarAsignacionPorUsuario(Long idUsuario) {
		try {
			List<AsignacionRecursoTipoTurno> lista = asignacionRepo.findByRecursoUsuarioId(idUsuario);
			return new ApiResponse<>(true,"",lista);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}

	@Override
	public ApiResponse<AsignacionRecursoTipoTurno> obtenerPorId(Long id) {
		AsignacionRecursoTipoTurno asig = asignacionRepo.getById(id);
		if(asig!=null) {
			return new ApiResponse<>(true,"",asig);
		}
		return new ApiResponse<>(false,"No se obtuvo el Reservante",asig);
	}
	
	private ApiResponse<AsignacionRecursoTipoTurno> setHorarioAsignacion(AsignacionRecursoTipoTurno a) {
		try {
			//AsignacionRecursoTipoTurno savedAsig = asignacionRepo.save(a);
			Set<Horario> horarios = new HashSet<>();
			String erroneos = "";
			for(Horario h : a.getHorarios()) {
				ApiResponse<Horario> resp = horarioService.guardarHorarioAsignacion(h, a);
				if(resp.isSuccess()) {
					horarios.add(resp.getData());
				}else {
					erroneos = erroneos + "Error al guardar el horario del dia "+h.getDia().name()+
							" Horario: "+h.getDesde()+" - "+h.getHasta()+" (Error: "+resp.getMessage()+").\n";
				}
			}
			if(erroneos.equals("")) {
				return new ApiResponse<>(false,erroneos + "No se cargo ningun horario",null);
			}
			a.setHorarios(horarios);
			return new ApiResponse<>(true,"",a);
		}catch(Exception e) {
			return new ApiResponse<>(false,"Error: "+e.getMessage(),null);
		}

	}
	
	private ApiResponse<AsignacionRecursoTipoTurno> setHorarioAsignacionDesdeRecurso(AsignacionRecursoTipoTurno a) {
		try {
			AsignacionRecursoTipoTurno savedAsig = asignacionRepo.save(a);
			Set<Horario> horarios = new HashSet<>();
			String erroneos = "";
			for(Horario h : savedAsig.getRecurso().getHorarios()) {
				Horario nuevoHorario = new Horario();
				nuevoHorario.setDesde(h.getDesde());
				nuevoHorario.setHasta(h.getHasta());
				nuevoHorario.setDia(h.getDia());
				ApiResponse<Horario> resp = horarioService.guardarHorarioAsignacion(h, savedAsig);
				if(resp.isSuccess()) {
					horarios.add(h);
				}else {
					erroneos = erroneos + "Error al guardar el horario del dia "+h.getDia().name()+
							" Horario: "+h.getDesde()+" - "+h.getHasta()+" (Error: "+resp.getMessage()+").\n";
				}
			}
			if(erroneos.equals("")) {
				return new ApiResponse<>(false,erroneos + "No se cargo ningun horario",null);
			}
			savedAsig.setHorarios(horarios);
			return new ApiResponse<>(true,"",savedAsig);
		}catch(Exception e) {
			return new ApiResponse<>(false,"Error: "+e.getMessage(),null);
		}
	}

}
