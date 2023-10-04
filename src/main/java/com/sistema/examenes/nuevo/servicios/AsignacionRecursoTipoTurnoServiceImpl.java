package com.sistema.examenes.nuevo.servicios;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Horario;
import com.sistema.examenes.anterior.modelo.HorarioEspecial;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.anterior.repositorios.AsignacionRecursoTipoTurnoRepository;
import com.sistema.examenes.modelo.usuario.Usuario;
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
	
	
	private final Validator validator;
	
    public AsignacionRecursoTipoTurnoServiceImpl(Validator validator) {
        this.validator = validator;
    }
    public ApiResponse<AsignacionRecursoTipoTurno> validar(AsignacionRecursoTipoTurno asignacion) {
        Errors errors = new BeanPropertyBindingResult(asignacion, "asignacion");
        ValidationUtils.invokeValidator(validator, asignacion, errors);
        if (errors.hasErrors()) {
        	//System.out.println(errors.getAllErrors());
        	return new ApiResponse<>(false,errors.getFieldError().getDefaultMessage().toString(),null);
        } else {
        	//System.out.println("---------------EXITO---------------");
        	return new ApiResponse<>(true,"".toString(),asignacion);
        	//return save(recursoDTO);
        }
    }
    private ApiResponse<AsignacionRecursoTipoTurno> save(AsignacionRecursoTipoTurno asig){
    	AsignacionRecursoTipoTurno guardado = asignacionRepo.save(asig);
		return (guardado!=null ? new ApiResponse<>(true,"",guardado) 
				: new ApiResponse<>(false,"Error al guardar el Tipo de Turno",null));
	}
    
    
    private ApiResponse<AsignacionRecursoTipoTurno> guardarAsignacion(AsignacionRecursoTipoTurno asignacion){
    	if(asignacionRepo.existeAsignacion(asignacion.getRecurso(), asignacion.getTipoTurno())!=0) {
    		asignacion.setEliminado(false);
    	}
    	ApiResponse<AsignacionRecursoTipoTurno> resp = validar(asignacion);
		if(resp.isSuccess()) {
			return save(resp.getData());
		}else {
			return new ApiResponse<>(false,"Error al guardar la Asignación, "+resp.getMessage(),null);
		}
    }
    
    
	
	@Override
	public ApiResponse<AsignacionRecursoTipoTurno> guardarAsignacion(long idTipoTurno, long idRecurso/*AsignacionRecursoTipoTurno asignacion*/, long idUsuario) {
		//obtener el recurso por id
		ApiResponse<Recurso> r = recursoService.obtenerRecursoPorId(idRecurso);
		//obtener el tipoturno por id
		ApiResponse<TipoTurno> t = tipoTurnoService.obtenerTipoTurnoPorId(idTipoTurno);
		if(r.isSuccess() && t.isSuccess()) {
			if(r.getData().getUsuario() == t.getData().getUsuario() && r.getData().getUsuario().getId() == idUsuario) {
				
				if(asignacionRepo.findAsignacionByRecursoAndTipoTurno(idRecurso, idTipoTurno)==null) {
					return guardarAsignacion(setearDatosPorDefecto(t.getData(), r.getData(), new AsignacionRecursoTipoTurno()));
				}
				return new ApiResponse<>(true,"Asignacion ya creada.",null);
			}
			return new ApiResponse<>(false,"Usuario no autorizado.",null);
		}
		return new ApiResponse<>(false,"Error al obtener los datos del Recurso y Tipo de Turno.",null);
		
		
	}
	
	
	private AsignacionRecursoTipoTurno setearDatosPorDefecto(TipoTurno t, Recurso r, AsignacionRecursoTipoTurno a) {
		a.setRecurso(r);
		a.setTipoTurno(t);
		a.setDuracionEnMinutos(t.getDuracionEnMinutos());
		a.setSeniaCtvos(t.getSeniaCtvos());
		a.setPrecioEstimadoDesdeCtvos(t.getPrecioEstimadoDesdeCtvos());
		a.setPrecioEstimadoHastaCtvos(t.getPrecioEstimadoHastaCtvos());
		a.setCantidadConcurrencia(1);
		//a.setHorarios(r.getHorarios());
		//a.setHorariosEspeciales(r.getHorariosEspeciales());
		return a;
	}
	
	

	@Override
	public ApiResponse<AsignacionRecursoTipoTurno> editarAsignacion(AsignacionRecursoTipoTurno asignacion, long idUsuario) {
		
		
		ApiResponse<AsignacionRecursoTipoTurno> guardado = obtenerPorId(asignacion.getId());
		if(guardado.isSuccess()) {
			//si el usuario es el mismo que el usuario nuevo entonces validar y guardar
			if(guardado.getData().getRecurso().getUsuario().getId() == idUsuario 
					&& guardado.getData().getTipoTurno().getUsuario().getId() == idUsuario) {
				asignacion.setRecurso(guardado.getData().getRecurso());
				asignacion.setTipoTurno(guardado.getData().getTipoTurno());
				asignacion.setReservas(guardado.getData().getReservas());
				return save(asignacion);//save
			}else {
				return new ApiResponse<>(false,"Usuario no autorizado a editar la Asignación.",null);
			}
		}else {
			return guardado;
		}
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
		return new ApiResponse<>(false,"No se pudo obtener la asignacion",asig);
	}
	/*
	
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
			return new ApiResponse<>(false,r.getMessage(),null);
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
	*/
	
	
	
	
	
	public ApiResponse<AsignacionRecursoTipoTurno> obtenerAsignacionPorIdRecursoTipoTurno(long idRecurso, long idTipoTurno){
		AsignacionRecursoTipoTurno asig = asignacionRepo.findAsignacionByRecursoAndTipoTurno(idRecurso, idTipoTurno);
		return asig!=null ? new ApiResponse<>(true,"",asig) : new ApiResponse(false,"No se pudo obtener la Asignación",null);
	}
	

	
	
	//obtener todas las asignaciones del recurso
	//obtener todas las asignaciones por id
	//a todas las asignaciones que esten en asignacionesPorId y no esten en asignacionesDelRecurso entonces se deben crear
	//a todas las asignaciones que no coincidan se setea eliminado=true
	//a todas las asignaciones que si coincidan se setea eliminado=false
	@Override
	public ApiResponse<AsignacionRecursoTipoTurno> eliminarAsignacion(Long idTipoTurno, Long idRecurso, Long idUsuario) {

		AsignacionRecursoTipoTurno asig = asignacionRepo.findAsignacionByRecursoAndTipoTurno(idRecurso, idTipoTurno);
		if(asig!=null) {
			asig.setEliminado(true);
			System.out.println("Voy a setar eliminado=true en la relacion idrec:"+idRecurso+" idTipoTurno:"+idTipoTurno);
			return editarAsignacion(asig,idUsuario);
		}
		return new ApiResponse<>(true,"",null);
		
	}
	
	
	
	@Override
	public ApiResponse<AsignacionRecursoTipoTurno> habilitarAsignacion(Long idTipoTurno, Long idRecurso, Long idUsuario) {

		AsignacionRecursoTipoTurno asig = asignacionRepo.findAsignacionByRecursoAndTipoTurno(idRecurso, idTipoTurno);
		if(asig!=null) {
			asig.setEliminado(false);
			return editarAsignacion(asig,idUsuario);
		}
		return guardarAsignacion(idTipoTurno, idRecurso, idUsuario);
		
	}
	
	
	
	
	
	
	
	
	
	
	@Override
	public ApiResponse<Recurso> actualizarAsignaciones(List<Long> idTiposDeTurno, long recId,Usuario usuario){
		String mensajeError = "";
		ApiResponse<AsignacionRecursoTipoTurno> resp;
		ApiResponse<List<TipoTurno>> todosLosTipoTurno = tipoTurnoService.listarTipoTurnoDeUsuario(usuario);
		if(todosLosTipoTurno.isSuccess()) {
			for(TipoTurno tt : todosLosTipoTurno.getData()) {
				resp = idTiposDeTurno.contains(tt.getId()) ? habilitarAsignacion(tt.getId(), recId, usuario.getId()) 
						: eliminarAsignacion(tt.getId(), recId, usuario.getId());
				mensajeError = resp.isSuccess() ? mensajeError : mensajeError + resp.getMessage() + " ";

			}
			return mensajeError.length()>0 ? new ApiResponse<>(false,"Error al actualizar las asignaciones del Recurso, "+mensajeError,null)
					: new ApiResponse<>(true,"Se guardaron correctamente los cambios.",null);
		}else {
			return new ApiResponse<>(false,"Error al actualizar las asignaciones del Recurso",null);
		}
	}
	
	
	
	
}
