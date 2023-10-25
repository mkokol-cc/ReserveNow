package com.sistema.examenes.nuevo.servicios;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
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
    public AsignacionRecursoTipoTurno validar(AsignacionRecursoTipoTurno asignacion) throws Exception {
        Errors errors = new BeanPropertyBindingResult(asignacion, "asignacion");
        ValidationUtils.invokeValidator(validator, asignacion, errors);
        if (errors.hasErrors()) {
        	throw new Exception(errors.getFieldError().getDefaultMessage().toString());
        } else {
        	return asignacion;
        }
    }
    private AsignacionRecursoTipoTurno save(AsignacionRecursoTipoTurno asig) throws Exception {
    	AsignacionRecursoTipoTurno guardado = asignacionRepo.save(asig);
        if (guardado == null) {
            throw new Exception("Error al guardar la Asignacion de Recurso a Tipo de Turno");
        }
        return guardado;
	}
    
    
    private AsignacionRecursoTipoTurno guardarAsignacion(AsignacionRecursoTipoTurno asignacion) throws Exception{
    	if(asignacionRepo.existeAsignacion(asignacion.getRecurso(), asignacion.getTipoTurno())!=0) {
    		asignacion.setEliminado(false);
    	}
    	return save(validar(asignacion));//validar y luego save
    }
    
    
	
	@Override
	public AsignacionRecursoTipoTurno guardarAsignacion(long idTipoTurno, long idRecurso, long idUsuario) throws Exception {
		//obtener el recurso por id
		Recurso r = recursoService.obtenerRecursoPorId(idRecurso);
		//obtener el tipoturno por id
		TipoTurno t = tipoTurnoService.obtenerTipoTurnoPorId(idTipoTurno);
		if(r.getUsuario() == t.getUsuario() && r.getUsuario().getId() == idUsuario) {
			AsignacionRecursoTipoTurno asigExistente = asignacionRepo.findAsignacionByRecursoAndTipoTurno(idRecurso, idTipoTurno);
			return asigExistente==null ? guardarAsignacion(setearDatosPorDefecto(t, r, new AsignacionRecursoTipoTurno())) : asigExistente;
		}
		throw new Exception("Usuario no autorizado.");
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
	public AsignacionRecursoTipoTurno editarAsignacion(AsignacionRecursoTipoTurno asignacion, long idUsuario) throws Exception {
		AsignacionRecursoTipoTurno guardado = obtenerPorId(asignacion.getId());
		if(guardado.getRecurso().getUsuario().getId() == idUsuario 
				&& guardado.getTipoTurno().getUsuario().getId() == idUsuario) {
			asignacion.setRecurso(guardado.getRecurso());
			asignacion.setTipoTurno(guardado.getTipoTurno());
			asignacion.setReservas(guardado.getReservas());
			return save(asignacion);//save
		}else {
			throw new Exception("Usuario no autorizado a editar la Asignación.");
		}
	}


	@Override
	public List<AsignacionRecursoTipoTurno> listarAsignacionPorUsuario(Long idUsuario) {
		return asignacionRepo.findByRecursoUsuarioId(idUsuario);
	}

	@Override
	public AsignacionRecursoTipoTurno obtenerPorId(Long id) {
		return asignacionRepo.getById(id);
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
	
	
	
	
	
	public AsignacionRecursoTipoTurno obtenerAsignacionPorIdRecursoTipoTurno(long idRecurso, long idTipoTurno){
		return asignacionRepo.findAsignacionByRecursoAndTipoTurno(idRecurso, idTipoTurno);
	}
	

	
	
	//obtener todas las asignaciones del recurso
	//obtener todas las asignaciones por id
	//a todas las asignaciones que esten en asignacionesPorId y no esten en asignacionesDelRecurso entonces se deben crear
	//a todas las asignaciones que no coincidan se setea eliminado=true
	//a todas las asignaciones que si coincidan se setea eliminado=false
	@Override
	public AsignacionRecursoTipoTurno eliminarAsignacion(Long idTipoTurno, Long idRecurso, Long idUsuario) throws Exception {

		AsignacionRecursoTipoTurno asig = asignacionRepo.findAsignacionByRecursoAndTipoTurno(idRecurso, idTipoTurno);
		if(asig!=null) {
			asig.setEliminado(true);
			System.out.println("Voy a setar eliminado=true en la relacion idrec:"+idRecurso+" idTipoTurno:"+idTipoTurno);
			return editarAsignacion(asig,idUsuario);
		}
		return new AsignacionRecursoTipoTurno();
		
	}
	
	
	
	@Override
	public AsignacionRecursoTipoTurno habilitarAsignacion(Long idTipoTurno, Long idRecurso, Long idUsuario) throws Exception {

		AsignacionRecursoTipoTurno asig = asignacionRepo.findAsignacionByRecursoAndTipoTurno(idRecurso, idTipoTurno);
		if(asig!=null) {
			asig.setEliminado(false);
			return editarAsignacion(asig,idUsuario);
		}
		return guardarAsignacion(idTipoTurno, idRecurso, idUsuario);
		
	}
	
	
	
	
	
	
	
	
	
	
	@Override
	public Recurso actualizarAsignaciones(List<Long> idTiposDeTurno, long recId,Usuario usuario) throws Exception{
		String mensajeError = "";
		for(TipoTurno tt : tipoTurnoService.listarTipoTurnoDeUsuario(usuario)) {
			try {
				if(idTiposDeTurno.contains(tt.getId())) {
					habilitarAsignacion(tt.getId(), recId, usuario.getId());
				}else {
					eliminarAsignacion(tt.getId(), recId, usuario.getId());
				}
			}catch(Exception e) {
				mensajeError += e.getMessage() + " ";
			}
		}
		if(mensajeError.equals("")) {
			return null;
		}
		throw new Exception(mensajeError);
	}
	
	
	//obtener List<HorarioEspcial> para la fecha
	//si no lo hay obtener List<Horario> para la fecha
	//para el caso de que haya 
	//public List<Horario> obtenerHorarioParaFechaDeAsignacion(LocalDate fecha, AsignacionRecursoTipoTurno asignacion){}
	
	
	
	
	
	
	//DESHABILITAR POR TIPOTURNO
	public ApiResponse<?> eliminarAsignacionPorTipoTurno(long idUsuario, long idTipoTurno) throws Exception {
		String mensajeError = "";
		for(AsignacionRecursoTipoTurno a : listarAsignacionPorUsuario(idUsuario)) {
			if(a.getTipoTurno().getId() == idTipoTurno) {
				try {
					eliminarAsignacion(idTipoTurno,a.getRecurso().getId(),idUsuario);
				}catch(Exception e) {
					mensajeError += e.getMessage();
				}
			}
		}
		if(mensajeError.equals("")) {
			return null;
		}
		throw new Exception(mensajeError);
	}
	
	//DESHABILITAR POR RECURSO
	public ApiResponse<?> eliminarAsignacionPorRecurso(long idUsuario, long idRecurso) throws Exception{
		String mensajeError = "";
		for(AsignacionRecursoTipoTurno a : listarAsignacionPorUsuario(idUsuario)) {
			if(a.getRecurso().getId() == idRecurso) {
				try {
					eliminarAsignacion(a.getTipoTurno().getId(),idRecurso,idUsuario);
				}catch(Exception e) {
					mensajeError += e.getMessage();
				}
			}
		}
		if(mensajeError.equals("")) {
			return null;
		}
		throw new Exception(mensajeError);
	}
	
	
	

}
