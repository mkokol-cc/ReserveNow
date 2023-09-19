package com.sistema.examenes.nuevo.servicios;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.CambioEstado;
import com.sistema.examenes.anterior.modelo.Dias;
import com.sistema.examenes.anterior.modelo.Estado;
import com.sistema.examenes.anterior.modelo.Horario;
import com.sistema.examenes.anterior.modelo.HorarioEspecial;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.anterior.repositorios.ReservaRepository;
import com.sistema.examenes.nuevo.dto.TurnoDTO;
import com.sistema.examenes.nuevo.servicios_interfaces.AsignacionRecursoTipoTurnoService;
import com.sistema.examenes.nuevo.servicios_interfaces.EstadoService;
import com.sistema.examenes.nuevo.servicios_interfaces.HorarioEspecialService;
import com.sistema.examenes.nuevo.servicios_interfaces.HorarioService;
import com.sistema.examenes.nuevo.servicios_interfaces.ReservaService;
import com.sistema.examenes.nuevo.servicios_interfaces.ReservanteService;

@Service
public class ReservaServiceImpl implements ReservaService{
	
	@Autowired
	private ReservaRepository reservaRepo;
	
	@Autowired
	private ReservanteService reservanteService;
	
	@Autowired
	private AsignacionRecursoTipoTurnoService asignacionService;
	
	@Autowired
	private HorarioService horarioService;
	
	@Autowired
	private HorarioEspecialService horarioEspecialService;
	
	@Autowired
	private EstadoService estadoService;
	
	private final Validator validator;
	
    public ReservaServiceImpl(Validator validator) {
        this.validator = validator;
    }
    public ApiResponse<Reserva> validar(Reserva reserva) {
        Errors errors = new BeanPropertyBindingResult(reserva, "reserva");
        ValidationUtils.invokeValidator(validator, reserva, errors);
        if (errors.hasErrors()) {
        	//System.out.println(errors.getAllErrors());
        	return new ApiResponse<>(false,errors.getAllErrors().toString(),null);
        } else {
        	//System.out.println("---------------EXITO---------------");
        	return new ApiResponse<>(true,"".toString(),reserva);
        	//return save(recursoDTO);
        }
    }
    private ApiResponse<Reserva> save(Reserva reserva){
    	Reserva guardado = reservaRepo.save(reserva);
		return (guardado!=null ? new ApiResponse<>(true,"",guardado) 
				: new ApiResponse<>(false,"Error al guardar la Reserva",null));
	}	
	
	
	
	
	
	@Override
	public ApiResponse<Reserva> guardarReserva(Reserva reserva) {
		ApiResponse<Reserva> resp = validar(reserva);
		if(resp.isSuccess()) {
			ApiResponse<Reservante> reservanteResp = reservanteService.guardarReservante(reserva.getReservante());
			if(reservanteResp.isSuccess()) {
				reserva = resp.getData();
				reserva.setReservante(reservanteResp.getData());
				return save(reserva);
			}
			return new ApiResponse<>(false,"Error al guardar la Reserva, "+reservanteResp.getMessage(),null);
		}else {
			return new ApiResponse<>(false,"Error al guardar la Reserva, "+resp.getMessage(),null);
		}/*
		
		
        try {
        	if(reserva.tieneLosDatosMinimos() && reserva.sonValidosLosDatos()) {
        		Reserva guardado = reservaRepo.save(reserva);
    			return guardado!=null ? new ApiResponse<>(true,"",guardado) : 
    				new ApiResponse<>(false,"Error al guardar la reserva.",null);	
        	}
        	return new ApiResponse<>(false,"Datos inválidos.",null);
        } catch (Exception e) {
        	return new ApiResponse<>(false,e.getMessage(),null);
        }*/
	}
	

	@Override
	public ApiResponse<Reserva> editarReserva(Reserva r, long idUsuario) {
		Reserva reservaEditada;
		ApiResponse<Reserva> response = obtenerReservaPorId(r.getId());
		if(response.isSuccess()) {
			if(idUsuario==response.getData().getAsignacionTipoTurno().getRecurso().getUsuario().getId()
					&& idUsuario==response.getData().getAsignacionTipoTurno().getTipoTurno().getUsuario().getId()) {
				reservaEditada = editarDatosPorAdministrador(response.getData(),r);
			}else {
				reservaEditada = editarDatosPorGuest(response.getData(),r);
			}
			return guardarReserva(reservaEditada);
		}
		return new ApiResponse<>(false,"Datos inválidos.",null);
		//si el usuario no es null entonces es administrador
		//si el usuario 
		/*
		
		
		
		
		//get reserva por id
		Reserva reservaAEditar = reservaRepo.getById(r.getId());
		//setear todos los datos que no se pueden cambiar
		r.setReservante(reservaAEditar.getReservante());
		r.setAsignacionTipoTurno(reservaAEditar.getAsignacionTipoTurno());
		r.setCambioEstado(reservaAEditar.getCambioEstado());//le ponemos los cambios de estado
		if(!reservaAEditar.getFecha().isEqual(r.getFecha()) || !reservaAEditar.getHora().equals(r.getHora())) {
			ApiResponse<Reserva> horarioResponse = setHorariosReserva(r);
			if(!horarioResponse.isSuccess()) {
				return new ApiResponse<>(false,horarioResponse.getMessage(),null);	
			}
			r.setFecha(horarioResponse.getData().getFecha());
			r.setHora(horarioResponse.getData().getHora());
			r.setHoraFin(horarioResponse.getData().getHoraFin());
		}
		if(r.getEstado().getId()!=reservaAEditar.getEstado().getId()) {
			ApiResponse<Reserva> cambiarEstadoResponse = cambiarEstado(r,reservaAEditar.getEstado());
			if(cambiarEstadoResponse.isSuccess()) {
				//r.setCambioEstado(cambiarEstadoResponse.getData().getCambioEstado());
				//r.setEstado(cambiarEstadoResponse.getData().getEstado());
				Reserva guardado = reservaRepo.save(cambiarEstadoResponse.getData());
    			return new ApiResponse<>(true,"",guardado);	
			}else {
				return new ApiResponse<>(false,cambiarEstadoResponse.getMessage(),null);	
			}	
		}
		Reserva guardado = reservaRepo.save(r);
		return new ApiResponse<>(true,"",guardado);*/
	}

	@Override
	public ApiResponse<Reserva> nuevaReserva(Reserva datosReserva, long idAsignacion){
		//setear asignacion
		ApiResponse<AsignacionRecursoTipoTurno> resp = this.asignacionService.obtenerPorId(idAsignacion);
		if(resp.isSuccess()) {
			datosReserva.setAsignacionTipoTurno(resp.getData());
			return guardarReserva(datosReserva);
		}else {
			return new ApiResponse<Reserva>(false,"Error al obtener la Asignacion Recurso Tipo Turno.",null);
		}
		/*
		
		
		//obtener el estado Nuevo
		ApiResponse<Reserva> respEstado = this.estadoService.estadoReservaNueva(datosReserva);
		if(respEstado.isSuccess()) {
			datosReserva = respEstado.getData();
		}else {
			return new ApiResponse<Reserva>(false,"Error al obtener el estado de la reserva.",null);
		}
		
		
		return new ApiResponse<Reserva>(true,"",datosReserva);
		
		//obtener Las Reservas de la asignacion
		//System.out.println(datosReserva.getAsignacionTipoTurno().getReservas());
		
		
		//validar
		/*
		ApiResponse<Reserva> respValidacion = validarReserva(datosReserva);
		if(respValidacion.isSuccess()) {
			Reserva reservaGuardada = reservaRepo.save(datosReserva);
			return (reservaGuardada!=null ? new ApiResponse<Reserva>(true,"",reservaGuardada) 
					: new ApiResponse<Reserva>(false,"Error al guardar la reserva",null));
		}
		return respValidacion;
		*/
		
		
		//validar2
		/*
		return valid(datosReserva) ? new ApiResponse<Reserva>(true,"",datosReserva) : 
			new ApiResponse<Reserva>(false,"Error al guardar la reserva",null);
		*/
		
	}
	
	private Reserva editarDatosPorAdministrador(Reserva original, Reserva editada) {
		original.setFecha(editada.getFecha());
		original.setHora(editada.getHora());
		original.setHoraFin(editada.getHoraFin());
		original.setNota(editada.getNota());
		if(!original.getEstado().equals(editada.getEstado())) {
			ApiResponse<Reserva> response = cambiarEstado(editada, original.getEstado());
			original = response.isSuccess() ? response.getData() : original;
		}
		return original;
	}
	private Reserva editarDatosPorGuest(Reserva original, Reserva editada) {
		//puede editar solo el estado y los horarios
		original.setFecha(editada.getFecha());
		original.setHora(editada.getHora());
		original.setHoraFin(editada.getHoraFin());
		if(!original.getEstado().equals(editada.getEstado())) {
			ApiResponse<Reserva> response = cambiarEstado(editada, original.getEstado());
			original = response.isSuccess() ? response.getData() : original;
		}
		return original;
	}
	private ApiResponse<Reserva> obtenerReservaPorId(long id){
		Reserva r = reservaRepo.getById(id);
		if(r!=null) {
			return new ApiResponse<>(true,"",r);
		}
		return new ApiResponse<>(false,"Error al obtener la Reserva id:"+id+".",null);
	}
	private ApiResponse<Reserva> cambiarEstado(Reserva r, Estado eAnterior) {
		//nuevo cambio estado y set nuevo estado
		ApiResponse<Reserva> estadoResponse = estadoService.nuevoCambioEstadoReserva(r,eAnterior);
		if(estadoResponse.isSuccess()) {
			return new ApiResponse<>(true,"",estadoResponse.getData());
		}
		return new ApiResponse<>(false,"Cambio de Estado Invalido",null);
	}
	
	
	
	
	
	
	
	
	
	

	@Override
	public ApiResponse<List<Reserva>> listarReservaPorUsuario(long idUsuario) {
		ApiResponse<List<AsignacionRecursoTipoTurno>> asignaciones = asignacionService.listarAsignacionPorUsuario(idUsuario);
		if(asignaciones.isSuccess()) {
			List<Reserva> reservas = new ArrayList<>();
			for(AsignacionRecursoTipoTurno a : asignaciones.getData()) {
				List<Reserva> aux = reservaRepo.findByAsignacionTipoTurno(a);
				reservas.addAll(aux);
			}
			return new ApiResponse<>(true,"",reservas);
		}
		return new ApiResponse<>(false,"Hubo un Problema al obtener las Reservas",null);
	}
	
	@Override
	public ApiResponse<Reserva> eliminarReservaPorId(long r, long idUsuario) {
		Reserva reserva = reservaRepo.getById(r);
		if(reserva!=null) {
			if(reserva.getAsignacionTipoTurno().getRecurso().getUsuario().getId()==idUsuario) {
				reservaRepo.delete(reserva);
				return new ApiResponse<>(true,"",null);	
			}
			return new ApiResponse<>(false,"No se encontro la reserva",null);
		}
		return new ApiResponse<>(false,"Usuario no autorizado",null);
	}
	
	
	
	private ApiResponse<Reserva> setDatosObligatorios(Reserva r, long idUsuario) {
    	ApiResponse<Reservante> resReservante = reservanteService.guardarReservante(r.getReservante());
    	ApiResponse<AsignacionRecursoTipoTurno> resAsig = asignacionService.obtenerPorId(r.getAsignacionTipoTurno().getId());
    	ApiResponse<Reserva> setEstadoResponse = estadoService.estadoReservaNueva(r);
    	if(resReservante.isSuccess()) {
    		r.setReservante(resReservante.getData());
    		if(resAsig.isSuccess()) {
    			if(resAsig.getData().getRecurso().getUsuario().getId()!=idUsuario || resAsig.getData().getTipoTurno().getUsuario().getId()!=idUsuario) {
    				return new ApiResponse<>(false,"Usuario no autorizado",null);
    			}
    			r.setAsignacionTipoTurno(resAsig.getData());
    			r.setHoraFin(r.getHora().plusMinutes(resAsig.getData().getDuracionEnMinutos()));
    			if(setEstadoResponse.isSuccess()) {
    				return new ApiResponse<>(true,"",setEstadoResponse.getData());	
    			}
    			return new ApiResponse<>(false,setEstadoResponse.getMessage(),null);
    		}
    		return new ApiResponse<>(false,resAsig.getMessage(),null);
    	}
    	return new ApiResponse<>(false,resReservante.getMessage(),null);
	}
	
	
	
	
	private ApiResponse<Reserva> setHorariosReserva(Reserva r) {
		if((r.getFecha().isEqual(LocalDate.now()) && !r.getHora().isAfter(LocalTime.now())) ||
				r.getFecha().isBefore(LocalDate.now())) {
			return new ApiResponse<>(false,"Fecha y Hora anterior a la actual",null);
		}
		ApiResponse<Reserva> ocupadoResponse = estaOcupado(r);
		if(ocupadoResponse.isSuccess()) {
			ApiResponse<Reserva> respuestaHorarioEsp = comprobarHoraPorHorarioEspecial(r);
			if(respuestaHorarioEsp.isSuccess()) {
				if(respuestaHorarioEsp.getData()==null) {
					//comprobar horario comun
					return comprobarHoraPorHorarioComun(r);
				}
			}
			return respuestaHorarioEsp;
		}else {
			return ocupadoResponse;
		}
	}
	
	private ApiResponse<Reserva> comprobarHoraPorHorarioEspecial(Reserva r){
    	ApiResponse<HorarioEspecial> he = horarioEspecialService.comprobarHorarioEspecialAsignacion(r.getHora(),r.getFecha(), r.getAsignacionTipoTurno());
    	//si hay un he is succes y getData devuelve un objeto no null entonces hay un HE en el que la hora es valida
    	//si hay un he is succes y getData devuelve un objeto null entonces no hay HE para esa fecha
    	if(he.isSuccess()) {
    		if(he.getData()!=null) {
    			return new ApiResponse<>(true,"horario valido",r);
    		}
    		return new ApiResponse<>(true,"No hay horario especial para esa fecha",null);
    	}else{
    		//--responder que no es posible, ya que para esa fecha hay un horario especial que lo impide
    		return new ApiResponse<>(false,"horario invalido: "+he.getMessage(),r);
    	}
	}
	
	private ApiResponse<Reserva> comprobarHoraPorHorarioComun(Reserva r){
		Dias d = Dias.valueOf(r.getFecha().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase()); 
		ApiResponse<Horario> h = horarioService.comprobarHorarioAsignacion(r.getHora(),d, r.getAsignacionTipoTurno());
		//posibles retornos TRUE si la hora es valida y FALSE si no
		if(h.isSuccess()) {
			return new ApiResponse<>(true,"Horario Valido",r);
		}
		return new ApiResponse<>(false,"Horario Invalido",null);
	}

	private ApiResponse<Reserva> estaOcupado(Reserva r) {
		try {
			LocalTime desde = r.getHora();
			LocalTime hasta = desde.plusMinutes((long)r.getAsignacionTipoTurno().getDuracionEnMinutos());
			if(reservaRepo.buscarPorFechaRangoHorarioRecurso(
					r.getFecha(),
					desde,
					hasta,
					r.getAsignacionTipoTurno().getRecurso().getId()).size()>0) {
				return new ApiResponse<>(false,"El Recurso "+r.getAsignacionTipoTurno().getRecurso().getNombre()+
						" esta entre las "+desde+" y "+hasta,null);
			}
			return new ApiResponse<>(true,"",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,"Error: "+e.getMessage(),null);
		}
	}

	
	
	
	
	
	@Override
	public List<TurnoDTO> crearTurnos(AsignacionRecursoTipoTurno asignacion, LocalDate fecha){
		List<TurnoDTO> turnos = new ArrayList<>();
		List<HorarioEspecial> horariosEspeciales = new ArrayList<>();
		for(HorarioEspecial he : asignacion.getHorariosEspeciales()) {
			if(he.getFecha().isEqual(fecha)) {
				horariosEspeciales.add(he);
			}
		}
		if(horariosEspeciales.size()>0) {
			for(HorarioEspecial h : horariosEspeciales) {
				if(!h.isCerrado()) {
					turnos.addAll(listarTurnos(h.getDesde(), h.getHasta(), asignacion.getDuracionEnMinutos(), fecha));
				}
			}
		}else {
			Dias d = Dias.valueOf(fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase());
			for(Horario h : asignacion.getHorarios()) {
				if(h.getDia().equals(d)) {
					turnos.addAll(listarTurnos(h.getDesde(), h.getHasta(), asignacion.getDuracionEnMinutos(), fecha));	
				}
			}
		}
		return actualizarDisponibilidadDeTurnos(turnos,asignacion.getRecurso());
	}
	
	
	private List<TurnoDTO> listarTurnos(LocalTime desde, LocalTime hasta, int duracion, LocalDate fecha){
		List<TurnoDTO> turnos = new ArrayList<>();
		LocalTime hora = desde;
		while(hasta.isAfter(hora)) {
			turnos.add(new TurnoDTO(fecha,hora,duracion,false));
			hora= hora.plusMinutes(duracion);
		}
		return turnos;
	}
	
	private List<TurnoDTO> actualizarDisponibilidadDeTurnos(List<TurnoDTO> turnos,Recurso r){
		List<Reserva> reservas = reservaRepo.buscarRecurso(r.getId());
		for(Reserva reserva : reservas) {
			for(TurnoDTO turno : turnos) {
				if(reserva.getFecha().isEqual(turno.getFecha())){
					if(turno.getHora().compareTo(reserva.getHoraFin()) >= 0 ||
							reserva.getHora().compareTo(turno.getHasta()) >= 0) {
						turno.setOcupado(false);
					}else {
						turno.setOcupado(true);
					}
				}
			}	
		}
		return turnos;
	}
	
	
	
	

	private ApiResponse<Reserva> validarReserva(Reserva reserva){
		String valido = reserva.validarReserva();
		if(valido.equals("")) {
			return new ApiResponse<Reserva>(true,"",reserva);
		}
		return new ApiResponse<Reserva>(false,valido,null);
	}
	
	private boolean valid(@Valid Reserva reserva) {
		return true;
	}
	
	
	
	private Dias localDateToDia(LocalDate fecha) {
		Dias d = Dias.valueOf(fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase());
		return d;
	}
	
	
	
	
	
	
}
