package com.sistema.examenes.nuevo.servicios;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Horario;
import com.sistema.examenes.anterior.modelo.HorarioEspecial;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.anterior.repositorios.AsignacionRecursoTipoTurnoRepository;
import com.sistema.examenes.nuevo.servicios_interfaces.AsignacionRecursoTipoTurnoService;
import com.sistema.examenes.nuevo.servicios_interfaces.HorarioEspecialService;
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
	
	@Autowired
	private HorarioEspecialService horarioEspService;
	
	@Override
	public ApiResponse<AsignacionRecursoTipoTurno> guardarAsignacion(long idTipoTurno, long idRecurso/*AsignacionRecursoTipoTurno asignacion*/, long idUsuario) {
		try {
			//obtener el recurso por id
			ApiResponse<Recurso> r = recursoService.obtenerRecursoPorId(idRecurso);
			//obtener el tipoturno por id
			ApiResponse<TipoTurno> t = tipoTurnoService.obtenerTipoTurnoPorId(idTipoTurno);
			//comprobar si el usuario es dueño de ambos
			if(asignacionRepo.findByRecursoAndTipoTurno(r.getData(), t.getData()).size()>0/*!=null*/) {
				return new ApiResponse<>(false,"La asignacion ya existe, no se puede volver a crear",null);
			}
			if(t.isSuccess() && r.isSuccess()) {
				if(t.getData().getUsuario().getId()==idUsuario && r.getData().getUsuario().getId()==idUsuario) {
					//setear datos a asignacion
					AsignacionRecursoTipoTurno asignacion = new AsignacionRecursoTipoTurno();
					asignacion.setRecurso(r.getData());
					asignacion.setTipoTurno(t.getData());
					//setear horarios
					ApiResponse<AsignacionRecursoTipoTurno> resp = setHorarioAsignacionDesdeRecurso(asignacion);
					if(resp.isSuccess()) {
						AsignacionRecursoTipoTurno guardado = asignacionRepo.save(asignacion);
						return new ApiResponse<>(true,"",guardado);	
					}
					return new ApiResponse<>(false,resp.getMessage(),null);
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
					asignacion.setReservas(resp.getData().getReservas());
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
	}/*
	
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

	}*/
	
	
	private ApiResponse<AsignacionRecursoTipoTurno> setHorarioAsignacion(AsignacionRecursoTipoTurno a) {
		try {
			ApiResponse<AsignacionRecursoTipoTurno> asigResp = setHorarios(a);
			if(asigResp.isSuccess()) {
				ApiResponse<AsignacionRecursoTipoTurno> asigRespDos = setHorariosEspeciales(asigResp.getData());
				if(asigRespDos.isSuccess()) {
					return new ApiResponse<>(true,"",asigRespDos.getData());
				}
				return new ApiResponse<>(false,"Error: "+asigRespDos.getMessage(),null);
			}
			return new ApiResponse<>(false,"Error: "+asigResp.getMessage(),null);
		}catch(Exception e) {
			return new ApiResponse<>(false,"Error: "+e.getMessage(),null);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private ApiResponse<AsignacionRecursoTipoTurno> setHorarioAsignacionDesdeRecurso(AsignacionRecursoTipoTurno a) {
		try {
			AsignacionRecursoTipoTurno savedAsig = asignacionRepo.save(a);
			//obtener el recurso para saber sus horarios especiales y comunes
			ApiResponse<Recurso> r = recursoService.obtenerRecursoPorId(a.getRecurso().getId());
			if(r.isSuccess()) {
				//setear esos horarios a la asignacion
				ApiResponse<AsignacionRecursoTipoTurno> asigConHorariosResp = setearLosHorariosDelRecurso(r.getData(),savedAsig);
				if(asigConHorariosResp.isSuccess()) {
					//llamar al metodo setHorarios
					ApiResponse<AsignacionRecursoTipoTurno> asigResp = setHorarioAsignacion(asigConHorariosResp.getData());
					if(asigResp.isSuccess()) {
						return new ApiResponse<>(true,"",asigResp.getData());
					}
					return new ApiResponse<>(false,asigResp.getMessage(),null);	
				}
				return new ApiResponse<>(false,asigConHorariosResp.getMessage(),null);	
			}
			return new ApiResponse<>(false,r.getMessage(),null);/*
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
			if(!erroneos.equals("")) {
				return new ApiResponse<>(false,erroneos + ". Los demas horarios se cargaron correctamente",null);
			}
			savedAsig.setHorarios(horarios);
			return new ApiResponse<>(true,"",savedAsig);*/
		}catch(Exception e) {
			return new ApiResponse<>(false,"Error: "+e.getMessage(),null);
		}
	}
	
	
	private ApiResponse<AsignacionRecursoTipoTurno> setearLosHorariosDelRecurso(Recurso r, AsignacionRecursoTipoTurno a){
		try {
			Set<Horario> horarios = new HashSet<>();
			Set<HorarioEspecial> horariosEspeciales = new HashSet<>();
			for(Horario h : r.getHorarios()) {
				Horario horario = new Horario();
				horario.setAsignacion(a);
				horario.setDesde(h.getDesde());
				horario.setDia(h.getDia());
				horario.setHasta(h.getHasta());
				horarios.add(horario);
			}
			for(HorarioEspecial he : r.getHorariosEspeciales()) {
				HorarioEspecial horario = new HorarioEspecial();
				horario.setAsignacion(a);
				horario.setDesde(he.getDesde());
				horario.setFecha(he.getFecha());
				horario.setHasta(he.getHasta());
				horario.setCerrado(he.isCerrado());
				horario.setMotivo(he.getMotivo());
				horariosEspeciales.add(he);
			}
			a.setHorarios(horarios);
			a.setHorariosEspeciales(horariosEspeciales);
			return new ApiResponse<>(true,"",a);
		}catch(Exception e) {
			return new ApiResponse<>(false,"Error: "+e.getMessage(),null);
		}

	}
	
	
	
	private ApiResponse<AsignacionRecursoTipoTurno> setHorariosEspeciales(AsignacionRecursoTipoTurno a) {
		try {
			if(!a.getHorariosEspeciales().isEmpty()) {
				ApiResponse<AsignacionRecursoTipoTurno> resp = horarioEspService.guardarListaHorarioEspecialAsignacion(a);
				if(resp.isSuccess()) {
					return new ApiResponse<>(true,"",a);
				}
				return new ApiResponse<>(false,resp.getMessage(),null);
			}
			return new ApiResponse<>(true,"",a);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}
	
	private ApiResponse<AsignacionRecursoTipoTurno> setHorarios(AsignacionRecursoTipoTurno a){
		try {
			if(!a.getHorarios().isEmpty()) {
				ApiResponse<AsignacionRecursoTipoTurno> resp = horarioService.guardarListaHorariosAsignacion(a);
				if(resp.isSuccess()) {
					return new ApiResponse<>(true,"",a);
				}
				return new ApiResponse<>(false,resp.getMessage(),null);
			}
			return new ApiResponse<>(true,"",a);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}
	

}
