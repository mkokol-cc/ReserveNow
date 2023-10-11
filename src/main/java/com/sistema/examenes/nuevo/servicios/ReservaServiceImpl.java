package com.sistema.examenes.nuevo.servicios;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
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
import com.sistema.examenes.nuevo.servicios_interfaces.NotificacionService;
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
	
	@Autowired
	private NotificacionService notificacionService;
	
	private final Validator validator;
	
    public ReservaServiceImpl(Validator validator) {
        this.validator = validator;
    }
    public ApiResponse<Reserva> validar(Reserva reserva) {
        Errors errors = new BeanPropertyBindingResult(reserva, "reserva");
        ValidationUtils.invokeValidator(validator, reserva, errors);
        if (errors.hasErrors()) {
        	//System.out.println(errors.getAllErrors());
        	return new ApiResponse<>(false,errors.getFieldError().getDefaultMessage().toString(),null);
        } else {
        	//System.out.println("---------------PASO LA VALIDACION---------------");
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
	public ApiResponse<Reserva> guardarReserva(Reserva reserva, long userId) {
		
		ApiResponse<AsignacionRecursoTipoTurno> respAsignacion = asignacionService.obtenerPorId(reserva.getAsignacionTipoTurno().getId());
		if(respAsignacion.isSuccess() && respAsignacion.getData().getRecurso().getUsuario().getId() == userId) {
			reserva.setAsignacionTipoTurno(respAsignacion.getData());
			ApiResponse<Reserva> resp = validar(reserva);
			if(resp.isSuccess()) {
				ApiResponse<Reserva> horarioValido = esPosibleReserva(reserva);
				if(horarioValido.isSuccess()) {
					ApiResponse<Reservante> reservanteResp = reservanteService.guardarReservante(reserva.getReservante());
					if(reservanteResp.isSuccess()) {
						reserva = estadoService.estadoReservaNueva(reserva).getData();
						reserva = resp.getData();
						reserva.setReservante(reservanteResp.getData());
						ApiResponse<Reserva> guardarResp = save(reserva);
						if(guardarResp.isSuccess()) {
							notificacionService.notificarNuevaReserva(guardarResp.getData(),guardarResp.getData().getAsignacionTipoTurno().getRecurso().getUsuario());
						} 
						return guardarResp;
					}
					return new ApiResponse<>(false,"Error al guardar la Reserva, "+reservanteResp.getMessage(),null);	
				}
				return new ApiResponse<>(false,"Error al guardar la Reserva, "+horarioValido.getMessage(),null);	
			}
			return new ApiResponse<>(false,"Error al guardar la Reserva, "+resp.getMessage(),null);
		}
		return new ApiResponse<>(false,"Error al guardar la Reserva, "+respAsignacion.getMessage(),null);
	}
	

	//para editar solo se puede editar: La Nota, El Estado, La Fecha y Hora
	//hay estados que no se pueden cambiar (son finales) Por ej todos los cancelados. (La logica de esto va a estar en el EstadoService)
	//el editar deberia tener una logica similar al guardar, solo que se debe crear el cambio de estado en el caso de cambiar de estado
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
			ApiResponse<Reserva> respGuardarReserva = guardarReserva(reservaEditada,idUsuario);
			if(respGuardarReserva.isSuccess()) {
				//notificar al cliente
			}
			return respGuardarReserva;
		}
		return new ApiResponse<>(false,"Datos inválidos. Error al obtener la Reserva.",null);
	}

	//esta logica ya esta en el metodo GuardarReserva implementada de manera correcta.
	//el id de la asignacion debe venir desde el body Http
	@Override
	public ApiResponse<Reserva> nuevaReserva(Reserva datosReserva, long userId){
		//setear asignacion
		ApiResponse<AsignacionRecursoTipoTurno> resp = this.asignacionService.obtenerPorId(datosReserva.getAsignacionTipoTurno().getId());
		if(resp.isSuccess()) {
			//los datos de la fecha y hora de creacion se hace aca porque los otros metodos tambien se usan para editar
			datosReserva.setFechaCreacion(LocalDate.now());
			datosReserva.setHoraCreacion(LocalTime.now());
			datosReserva.setAsignacionTipoTurno(resp.getData());
			return guardarReserva(datosReserva,userId);
		}else {
			return new ApiResponse<Reserva>(false,"Error al obtener la Asignacion Recurso Tipo Turno.",null);
		}
		
	}
	
	//esto no se deberia hacer, se deberia editar de igual manera para ambos
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
	
	
	/*
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
	}*/

	
	
	
	
	
	@Override
	public ApiResponse<List<TurnoDTO>> crearTurnos(Long idAsignacion, LocalDate fecha){
		List<TurnoDTO> turnos = new ArrayList<>();
		ApiResponse<AsignacionRecursoTipoTurno> resp = asignacionService.obtenerPorId(idAsignacion);
		if(resp.isSuccess()) {
			//List<LocalTime> horarios = getTodosLosHorariosPosiblesDeAsigPorFecha(resp.getData(),fecha);
			List<LocalTime> horarios = getTodosLosHorariosPosiblesDeRecursoPorFecha(resp.getData().getRecurso(),fecha,resp.getData());
			for(LocalTime h : horarios) {
				turnos.add(new TurnoDTO(fecha,h,resp.getData().getDuracionEnMinutos(),estaLibreElHorario(resp.getData(),fecha,h)));
			}
			return new ApiResponse<>(true,"",turnos);
		}
		return new ApiResponse<>(false,"Error al obtener la asignacion",null);
		
		
		
		
		/*
		
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
		return actualizarDisponibilidadDeTurnos(turnos,asignacion.getRecurso());*/
	}
	/*
	
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
	}*/
	

	
	
	
	//SI EL RECURSO ESTA OCUPADO (TIENE OTRA ASIGNACION EN ESE RANGO HORARIO ENTONCES ESTA OCUPADO)
	//SI EL RECURSO ESTA OCUPADO PERO LA ASIGNACION ES LA MISMA? COMPROBAR SI EL HORARIO ES EL MISMO PARA LA ASIGNACION RESERVADA
	//SI EL HORARIO ES EL MISMO Y LA CANTIDAD DE LAS RESERVAS CON ESTADO RESERVADO ES MENOR A CONCURRENCIA ENTONCES NO ESTA OCUPADO
	//SI EL HORARIO ES EL MISMO Y LA CANTIDAD DE LAS RESERVAS CON ESTADO RESERVADO ES MAYOR A CONCURRENCIA ENTONCES NO ESTA OCUPADO
	//SI EL HORARIO NO ES EL MISMO Y ESTA EN EL RANGO ENTONCES HAY UNA RESERVA REGISTRADA CON UN HORARIO MAL - ENTONCES CANCELAR RESERVA Y AVISAR
	private ApiResponse<Reserva> esPosibleReserva(Reserva r){
		ApiResponse<AsignacionRecursoTipoTurno> resp = asignacionService.obtenerPorId(r.getAsignacionTipoTurno().getId());
		if(resp.isSuccess()) {
			//if( getTodosLosHorariosPosiblesDeAsigPorFecha(resp.getData(),r.getFecha()).contains(r.getHora()) ){
			if( getTodosLosHorariosPosiblesDeRecursoPorFecha(resp.getData().getRecurso(),r.getFecha(),resp.getData()).contains(r.getHora()) ){
				return estaLibreElHorario(resp.getData(),r.getFecha(),r.getHora()) ? new ApiResponse<>(true,"",r) 
						: new ApiResponse<>(false,"Horario Ocupado, por favor seleccione otro horario.",null);
			}
			return new ApiResponse<>(false,"El horario no es valido, por favor selecciona un horario valido.",null);
		}
		return new ApiResponse<>(false,"Error al obtener la asignacion.",null);
	}
	
	
	private boolean estaLibreElHorario(AsignacionRecursoTipoTurno asignacion,LocalDate fecha, LocalTime hora) {
		List<Reserva> reservas = reservaRepo.buscarPorFechaRangoHorarioRecurso(fecha, hora, hora.plusMinutes((long) asignacion.getDuracionEnMinutos()), asignacion.getRecurso().getId());
		boolean sonDeAsignacion = reservas.stream().allMatch(reserva -> reserva.getAsignacionTipoTurno().getId() == asignacion.getId());
		boolean estanEnHorario = reservas.stream().allMatch(reserva -> reserva.getHora().equals(hora));
		return (sonDeAsignacion && estanEnHorario && reservas.size()<asignacion.getCantidadConcurrencia());
	}
	
	//ESTE METODO NO SE USA DE MOMENTO, YA QUE LOS HORARIOS DE LAS ASIGNACIONES SON IGUALES QUE DE LOS RECURSOS
	//NO SE VA A ACONTEMPLAR LA LOGICA DE SETEAR HORARIO POR ASIGNACION EN LUGAR DE POR RECURSO
	private List<LocalTime> getTodosLosHorariosPosiblesDeAsigPorFecha(AsignacionRecursoTipoTurno a, LocalDate fecha){
		List<LocalTime> horariosPosibles = new ArrayList<>();
		//horarioEspRepo get horarios por asig y fecha
		ApiResponse<List<HorarioEspecial>> horariosEsp = horarioEspecialService.horariosEspecialesDeAsignacionParaFecha(a, fecha);
		//si no hay -> horarioRepo get horarios por asig y dia
		ApiResponse<List<Horario>> horarios = horarioService.horariosDeAsignacionParaFecha(a, localDateToDia(fecha));
		//para cualquiera de los dos, para cada horario obtenido getHorariosDisponibles
		if(horariosEsp.isSuccess() && !horariosEsp.getData().isEmpty()) {
			for(HorarioEspecial he : horariosEsp.getData()) {
				if(he.isCerrado()) {
					return null;
				}
				horariosPosibles.addAll(getHorariosDisponibles(he.getDesde(),he.getHasta(),(long)a.getDuracionEnMinutos()));
			}
		}else if(horarios.isSuccess() && !horarios.getData().isEmpty()) {
			for(Horario h : horarios.getData()) {
				horariosPosibles.addAll(getHorariosDisponibles(h.getDesde(),h.getHasta(),(long)a.getDuracionEnMinutos()));
			}
		}
		//devolver la coleccion completa
		return horariosPosibles;
	}
	
	private List<LocalTime> getTodosLosHorariosPosiblesDeRecursoPorFecha(Recurso recurso, LocalDate fecha, AsignacionRecursoTipoTurno asignacionElegida){
		List<LocalTime> horariosPosibles = new ArrayList<>();
		//horarioEspRepo get horarios por asig y fecha
		ApiResponse<List<HorarioEspecial>> horariosEsp = horarioEspecialService.horariosEspecialesDeRecursoParaFecha(recurso,fecha);
		//si no hay -> horarioRepo get horarios por asig y dia
		ApiResponse<List<Horario>> horarios = horarioService.horariosDeRecursoParaFecha(recurso, localDateToDia(fecha));
		//para cualquiera de los dos, para cada horario obtenido getHorariosDisponibles
		if(horariosEsp.isSuccess() && !horariosEsp.getData().isEmpty()) {
			for(HorarioEspecial he : horariosEsp.getData()) {
				if(he.isCerrado()) {
					return null;
				}
				horariosPosibles.addAll(getHorariosDisponibles(he.getDesde(),he.getHasta(),(long)asignacionElegida.getDuracionEnMinutos()));
			}
		}else if(horarios.isSuccess() && !horarios.getData().isEmpty()) {
			for(Horario h : horarios.getData()) {
				horariosPosibles.addAll(getHorariosDisponibles(h.getDesde(),h.getHasta(),(long)asignacionElegida.getDuracionEnMinutos()));
			}
		}
		//devolver la coleccion completa
		return horariosPosibles;
	}
	
	private List<LocalTime> getHorariosDisponibles(LocalTime desde, LocalTime hasta, long minutos){
		List<LocalTime> horariosCorrectos = new ArrayList<>();
		//mientras desde <= hasta
		while(!desde.isAfter(hasta)) {
			//si desde + minutos <= hasta
			if(!desde.plusMinutes(minutos).isAfter(hasta)) {
				horariosCorrectos.add(desde);
			}
			desde = desde.plusMinutes(minutos);
		}
		return horariosCorrectos;
	}
	
	
	private Dias localDateToDia(LocalDate fecha) {
		Dias d = Dias.valueOf(fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase());
		return d;
	}
	
	//PROCEDIMIENTO AUTOMATICO
	//obtener todas las reservas
	//chequear que ester correctas? opcional, por el momento no lo voy a implementar
	//chequear si ya paso la fecha de y cambiar su estado a finalizado
	
	
	
	//----ACA OBTENEMOS EN CASO DE QUE SE DESHABILITE UNA ASIGNACION O SE CAMBIE EL HORARIO DE UNA ASIGNACION
	//----LAS RESERVAS INVOLUCRADAS Y LAS FILTRAMOS PARA OBTENER LAS QUE TIENEN ESTADO NO FINALES
	//OBTENER RESERVAS NO FINALIZADAS POR ASIGNACION Y RANGO HORARIO
	//OBTENER RESERVAS NO FINALIZADAS POR ASIGNACION
	//paramentro Rango Inhabilitado, si esta cerrado para la fecha el rango es de 00:00 a 23:59
	@Override
	public ApiResponse<List<Reserva>> obtenerReservasInvolucradasEnRango(Long idRecurso,/*long asignacionId,*/LocalDate fecha, LocalTime desde, LocalTime hasta){
		//List<Reserva> reservas = reservaRepo.buscarPorFechaRangoHorarioAsignacion(fecha,desde,hasta,asignacionId);
		List<Reserva> reservas = reservaRepo.buscarPorFechaRangoHorarioRecurso(fecha,desde,hasta,idRecurso);
		List<Reserva> resFiltradas = obtenerSoloEstadosNoFinales(reservas);
		return new ApiResponse<>(true,"",resFiltradas);
	}
	
	public ApiResponse<List<Reserva>> obtenerReservasInvolucradasPorAsignacion(AsignacionRecursoTipoTurno asig,LocalDate fecha, LocalTime desde, LocalTime hasta){
		List<Reserva> reservas = reservaRepo.findByAsignacionTipoTurno(asig);
		List<Reserva> resFiltradas = obtenerSoloEstadosNoFinales(reservas);
		return new ApiResponse<>(true,"",resFiltradas);
	}
	
	private List<Reserva> obtenerSoloEstadosNoFinales(List<Reserva> reservas){
		List<Reserva> resFiltradas = new ArrayList<>();
		for(Reserva r : reservas) {
			if(!r.getEstado().isEsEstadoFinal()) {
				resFiltradas.add(r);
			}
		}
		return resFiltradas;
	}
	
	public ApiResponse<List<Reserva>> cambiarEstadoA(Estado e, List<Reserva> reservas){
		ApiResponse<Reserva> reservaConNuevoCambioEstado;
		String error = "";
		for(Reserva r : reservas) {
			Estado estadoAnterior = r.getEstado();
			r.setEstado(e);
			reservaConNuevoCambioEstado = estadoService.nuevoCambioEstadoReserva(r, estadoAnterior);
			error = reservaConNuevoCambioEstado.isSuccess() ? error : error + "Error en reserva "+ 
			r.getId() + ", " + reservaConNuevoCambioEstado.getMessage() + " ";
			if(reservaConNuevoCambioEstado.isSuccess()) {
				ApiResponse<Reserva> resp = save(r);
				error = resp.isSuccess() ? error : error + "Error en reserva "+ 
						r.getId() + ", " + resp.getMessage() + " ";
			}
		}
		return error.length()>0 ? new ApiResponse<>(false,error,null) :  new ApiResponse<>(true,"",null);
	}
	
	
	@Override
	public ApiResponse<List<Reserva>> cambiarEstadosDeListaDeIsReserva(Long idEstado, List<Long> idReserva){
		List<Reserva> reservas = new ArrayList<>();
		for(Long id : idReserva) {
			reservas.add(reservaRepo.getById(id));
		}
		ApiResponse<Estado> respEstado = estadoService.getEstadoById(idEstado);
		return respEstado.isSuccess() ? cambiarEstadoA(respEstado.getData(),reservas) 
				: new ApiResponse<>(false,respEstado.getMessage(),null);
	}
	
	
	/*
	public ApiResponse<List<Reserva>> obtenerReservaPorRecursoYFecha(long idRecurso, LocalDate fecha){
		//obtener Asignaciones del Recurso
		//cancelarReserva
		List<Reserva> reservas = reservaRepo.buscarPorFechaYRecurso(fecha,idRecurso);
	}*/
	
	
	
	//NOTIFICAR
	//private boolean notificarReservante(Reservante r, String mensaje){
		//System.out.println(r.getTelefono() + " Enviar Mensaje: "+mensaje);
		//return true;)
	//}
}
