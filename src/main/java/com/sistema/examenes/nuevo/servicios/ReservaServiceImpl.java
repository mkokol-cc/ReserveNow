package com.sistema.examenes.nuevo.servicios;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
import com.sistema.examenes.anterior.modelo.TipoTurno;
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
    public Reserva validar(Reserva reserva) throws Exception{
        Errors errors = new BeanPropertyBindingResult(reserva, "reserva");
        ValidationUtils.invokeValidator(validator, reserva, errors);
        if (errors.hasErrors()) {
        	//System.out.println(errors.getAllErrors());
        	throw new Exception(errors.getFieldError().getDefaultMessage().toString());
        } else {
        	//System.out.println("---------------PASO LA VALIDACION---------------");
        	return reserva;
        	//return save(recursoDTO);
        }
    }
    private Reserva save(Reserva reserva) throws Exception{
    	Reserva guardado = reservaRepo.save(reserva);
    	if(guardado!=null) {
    		return guardado;
    	}
    	throw new Exception("Error al guardar la Reserva en la base de datos.");
	}	
	
	
	
	
	
	@Override
	public Reserva guardarReserva(Reserva reserva, long userId) throws Exception {
		AsignacionRecursoTipoTurno asignacion = asignacionService.obtenerPorId(reserva.getAsignacionTipoTurno().getId());
		if(asignacion.getRecurso().getUsuario().getId() == userId) {
			reserva.setAsignacionTipoTurno(asignacion);
			if(esPosibleReserva(reserva)!=null) {
				Reservante r = reservanteService.guardarReservante(reserva.getReservante());
				reserva.setReservante(r);
				reserva = estadoService.estadoReservaNueva(reserva);
				Reserva guardada = save(validar(reserva));
				notificacionService.notificarNuevaReserva(guardada,guardada.getAsignacionTipoTurno().getRecurso().getUsuario());
				return guardada;
			}else {
				throw new Exception("horario invalido.");
			}
		}else{
			throw new Exception("Usuario no autorizado");
		}
	}
	

	//para editar solo se puede editar: La Nota, El Estado, La Fecha y Hora
	//hay estados que no se pueden cambiar (son finales) Por ej todos los cancelados. (La logica de esto va a estar en el EstadoService)
	//el editar deberia tener una logica similar al guardar, solo que se debe crear el cambio de estado en el caso de cambiar de estado
	@Override
	public Reserva editarReserva(Reserva r, long idUsuario) throws Exception {
		Reserva reservaEditada;
		Reserva reservaId = obtenerReservaPorId(r.getId());
		if(idUsuario==reservaId.getAsignacionTipoTurno().getRecurso().getUsuario().getId()
				&& idUsuario==reservaId.getAsignacionTipoTurno().getTipoTurno().getUsuario().getId()) {
			reservaEditada = editarDatosPorAdministrador(reservaId,r);
		}else {
			reservaEditada = editarDatosPorGuest(reservaId,r);
		}
		Reserva guardada = save(validar(reservaEditada));
		if(guardada!=null) {
			//ENVIAR AVISO WPP O MAIL AL RESERVANTE
			return guardada;
		}
		throw new Exception("Error al editar la Reserva");
	}

	//esta logica ya esta en el metodo GuardarReserva implementada de manera correcta.
	//el id de la asignacion debe venir desde el body Http
	@Override
	public Reserva nuevaReserva(Reserva datosReserva, long userId) throws Exception{
		datosReserva.setFechaCreacion(LocalDate.now());
		datosReserva.setHoraCreacion(LocalTime.now());
		datosReserva.setAsignacionTipoTurno(this.asignacionService.obtenerPorId(datosReserva.getAsignacionTipoTurno().getId()));
		return guardarReserva(datosReserva,userId);
	}
	
	//esto no se deberia hacer, se deberia editar de igual manera para ambos
	private Reserva editarDatosPorAdministrador(Reserva original, Reserva editada) throws Exception {
		original.setFecha(editada.getFecha());
		original.setHora(editada.getHora());
		original.setHoraFin(editada.getHoraFin());
		original.setNota(editada.getNota());
		if(!original.getEstado().equals(editada.getEstado())) {
			original = cambiarEstado(editada, original.getEstado());
		}
		return original;
	}
	private Reserva editarDatosPorGuest(Reserva original, Reserva editada) throws Exception {
		//puede editar solo el estado y los horarios
		original.setFecha(editada.getFecha());
		original.setHora(editada.getHora());
		original.setHoraFin(editada.getHoraFin());
		if(!original.getEstado().equals(editada.getEstado())) {
			original = cambiarEstado(editada, original.getEstado());
		}
		return original;
	}
	
	
	private Reserva obtenerReservaPorId(long id){
		return reservaRepo.getById(id);
	}
	private Reserva cambiarEstado(Reserva r, Estado eAnterior) throws Exception {
		//nuevo cambio estado y set nuevo estado
		return estadoService.nuevoCambioEstadoReserva(r,eAnterior);
	}
	
	
	
	
	
	
	
	
	
	

	@Override
	public List<Reserva> listarReservaPorUsuario(long idUsuario) throws Exception {
		List<Reserva> reservas = new ArrayList<>();
		for(AsignacionRecursoTipoTurno a : asignacionService.listarAsignacionPorUsuario(idUsuario)) {
			List<Reserva> aux = reservaRepo.findByAsignacionTipoTurno(a);
			reservas.addAll(aux);
		}
		return reservas;
	}
	
	@Override
	public Reserva eliminarReservaPorId(long r, long idUsuario) throws Exception {
		Reserva reserva = reservaRepo.getById(r);
		if(reserva!=null) {
			if(reserva.getAsignacionTipoTurno().getRecurso().getUsuario().getId()==idUsuario) {
				return save(estadoService.eliminarReserva(reserva));
			}
			throw new Exception("Usuario no autorizado");
		}
		throw new Exception("No se encontro la reserva");
	}
	
	
	
	
	@Override
	public List<TurnoDTO> crearTurnos(Long idAsignacion, LocalDate fecha) throws Exception{
		List<TurnoDTO> turnos = new ArrayList<>();
		AsignacionRecursoTipoTurno asignacion = asignacionService.obtenerPorId(idAsignacion);
		List<LocalTime> horarios = getTodosLosHorariosPosiblesDeRecursoPorFecha(asignacion.getRecurso(),fecha,asignacion);
		for(LocalTime h : horarios) {
			turnos.add(new TurnoDTO(fecha,h,asignacion.getDuracionEnMinutos(),estaLibreElHorario(asignacion,fecha,h)));
		}
		return turnos;
	}

	
	
	
	//SI EL RECURSO ESTA OCUPADO (TIENE OTRA ASIGNACION EN ESE RANGO HORARIO ENTONCES ESTA OCUPADO)
	//SI EL RECURSO ESTA OCUPADO PERO LA ASIGNACION ES LA MISMA? COMPROBAR SI EL HORARIO ES EL MISMO PARA LA ASIGNACION RESERVADA
	//SI EL HORARIO ES EL MISMO Y LA CANTIDAD DE LAS RESERVAS CON ESTADO RESERVADO ES MENOR A CONCURRENCIA ENTONCES NO ESTA OCUPADO
	//SI EL HORARIO ES EL MISMO Y LA CANTIDAD DE LAS RESERVAS CON ESTADO RESERVADO ES MAYOR A CONCURRENCIA ENTONCES NO ESTA OCUPADO
	//SI EL HORARIO NO ES EL MISMO Y ESTA EN EL RANGO ENTONCES HAY UNA RESERVA REGISTRADA CON UN HORARIO MAL - ENTONCES CANCELAR RESERVA Y AVISAR
	private Reserva esPosibleReserva(Reserva r) throws Exception{
		AsignacionRecursoTipoTurno asig = asignacionService.obtenerPorId(r.getAsignacionTipoTurno().getId());
		if( getTodosLosHorariosPosiblesDeRecursoPorFecha(asig.getRecurso(),r.getFecha(),asig).contains(r.getHora()) ){
			if( estaLibreElHorario(asig,r.getFecha(),r.getHora()) ) {
				return r;
			}
			throw new Exception("Horario Ocupado, por favor seleccione otro horario.");
		}
		throw new Exception("El horario no es valido, por favor selecciona un horario valido.");
	}
	
	
	private boolean estaLibreElHorario(AsignacionRecursoTipoTurno asignacion,LocalDate fecha, LocalTime hora) {
		List<Reserva> reservas = reservaRepo.buscarPorFechaRangoHorarioRecurso(fecha, hora, hora.plusMinutes((long) asignacion.getDuracionEnMinutos()), asignacion.getRecurso().getId());
		boolean sonDeAsignacion = reservas.stream().allMatch(reserva -> reserva.getAsignacionTipoTurno().getId() == asignacion.getId());
		boolean estanEnHorario = reservas.stream().allMatch(reserva -> reserva.getHora().equals(hora));
		return (sonDeAsignacion && estanEnHorario && reservas.size()<asignacion.getCantidadConcurrencia());
	}
	
	//ESTE METODO NO SE USA DE MOMENTO, YA QUE LOS HORARIOS DE LAS ASIGNACIONES SON IGUALES QUE DE LOS RECURSOS
	//NO SE VA A ACONTEMPLAR LA LOGICA DE SETEAR HORARIO POR ASIGNACION EN LUGAR DE POR RECURSO
	private List<LocalTime> getTodosLosHorariosPosiblesDeAsigPorFecha(AsignacionRecursoTipoTurno a, LocalDate fecha) throws Exception{
		List<LocalTime> horariosPosibles = new ArrayList<>();
		//horarioEspRepo get horarios por asig y fecha
		List<HorarioEspecial> horariosEsp = horarioEspecialService.horariosEspecialesDeAsignacionParaFecha(a, fecha);
		//si no hay -> horarioRepo get horarios por asig y dia
		List<Horario> horarios = horarioService.horariosDeAsignacionParaFecha(a, localDateToDia(fecha));
		//para cualquiera de los dos, para cada horario obtenido getHorariosDisponibles
		if(!horariosEsp.isEmpty()) {
			for(HorarioEspecial he : horariosEsp) {
				if(he.isCerrado()) {
					return null;
				}
				horariosPosibles.addAll(getHorariosDisponibles(he.getDesde(),he.getHasta(),(long)a.getDuracionEnMinutos()));
			}
		}else if(!horarios.isEmpty()) {
			for(Horario h : horarios) {
				horariosPosibles.addAll(getHorariosDisponibles(h.getDesde(),h.getHasta(),(long)a.getDuracionEnMinutos()));
			}
		}
		//devolver la coleccion completa
		return horariosPosibles;
	}
	
	private List<LocalTime> getTodosLosHorariosPosiblesDeRecursoPorFecha(Recurso recurso, LocalDate fecha, AsignacionRecursoTipoTurno asignacionElegida) throws Exception{
		List<LocalTime> horariosPosibles = new ArrayList<>();
		//horarioEspRepo get horarios por asig y fecha
		List<HorarioEspecial> horariosEsp = horarioEspecialService.horariosEspecialesDeRecursoParaFecha(recurso,fecha);
		//si no hay -> horarioRepo get horarios por asig y dia
		List<Horario> horarios = horarioService.horariosDeRecursoParaFecha(recurso, localDateToDia(fecha));
		//para cualquiera de los dos, para cada horario obtenido getHorariosDisponibles
		if(!horariosEsp.isEmpty()) {
			for(HorarioEspecial he : horariosEsp) {
				if(he.isCerrado()) {
					return null;
				}
				horariosPosibles.addAll(getHorariosDisponibles(he.getDesde(),he.getHasta(),(long)asignacionElegida.getDuracionEnMinutos()));
			}
		}else if(!horarios.isEmpty()) {
			for(Horario h : horarios) {
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
	public List<Reserva> obtenerReservasInvolucradasEnRango(Long idRecurso,/*long asignacionId,*/LocalDate fecha, LocalTime desde, LocalTime hasta){
		//List<Reserva> reservas = reservaRepo.buscarPorFechaRangoHorarioAsignacion(fecha,desde,hasta,asignacionId);
		List<Reserva> reservas = reservaRepo.buscarPorFechaRangoHorarioRecurso(fecha,desde,hasta,idRecurso);
		List<Reserva> resFiltradas = obtenerSoloEstadosNoFinales(reservas);
		return resFiltradas;
	}
	
	public List<Reserva> obtenerReservasInvolucradasPorAsignacion(AsignacionRecursoTipoTurno asig,LocalDate fecha, LocalTime desde, LocalTime hasta){
		List<Reserva> reservas = reservaRepo.findByAsignacionTipoTurno(asig);
		List<Reserva> resFiltradas = obtenerSoloEstadosNoFinales(reservas);
		return resFiltradas;
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
	
	public List<Reserva> cambiarEstadoA(Estado e, List<Reserva> reservas) throws Exception{
		//ApiResponse<Reserva> reservaConNuevoCambioEstado;
		String error = "";
		for(Reserva r : reservas) {
			Estado estadoAnterior = r.getEstado();
			r.setEstado(e);
			try {
				r = estadoService.nuevoCambioEstadoReserva(r, estadoAnterior);
				save(r);
			}catch(Exception ex) {
				error += "Reserva id "+r.getId()+" "+ex.getMessage();
			}
		}
		if(error.equals("")) {
			return null;
		}
		throw new Exception(error);
	}
	
	
	@Override
	public List<Reserva> cambiarEstadosDeListaDeIdsReserva(Long idEstado, List<Long> idReserva) throws Exception{
		List<Reserva> reservas = new ArrayList<>();
		for(Long id : idReserva) {
			reservas.add(reservaRepo.getById(id));
		}
		cambiarEstadoA(estadoService.getEstadoById(idEstado),reservas);
		return null;
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
	

	
	
	//si se elimina un Recurso entonces Se debe eliminar todas sus Asignaciones
	
	
	
	@Override
	public List<Reserva> obtenerReservasEnEstadoNoFinal(LocalDate fecha, LocalTime desde, LocalTime hasta){
		return reservaRepo.buscarPorFechaYRangoHorario(fecha, desde, hasta).stream()
	            .filter(reserva -> !reserva.getEstado().isEsEstadoFinal())
	            .collect(Collectors.toList());
	};
	@Override
	public List<Reserva> obtenerReservasEnEstadoNoFinal(AsignacionRecursoTipoTurno asignacion){
		return reservaRepo.findByAsignacionTipoTurno(asignacion).stream()
	            .filter(reserva -> !reserva.getEstado().isEsEstadoFinal())
	            .collect(Collectors.toList());
	};
	@Override
	public List<Reserva> obtenerReservasEnEstadoNoFinal(Recurso r){
		return reservaRepo.findByRecurso(r.getId()).stream()
	            .filter(reserva -> !reserva.getEstado().isEsEstadoFinal())
	            .collect(Collectors.toList());
	};
	@Override
	public List<Reserva> obtenerReservasEnEstadoNoFinal(TipoTurno t){
		return reservaRepo.findByTipoTurno(t.getId()).stream()
	            .filter(reserva -> !reserva.getEstado().isEsEstadoFinal())
	            .collect(Collectors.toList());
	};
	
	
	
	@Override
	public void eliminarReservasMalRegistradas(List<Reserva> reservas) throws Exception {
		for(Reserva r: reservas) {
			if(!controlarReserva(r)) {
				eliminarReservaPorId(r.getId(),r.getAsignacionTipoTurno().getRecurso().getUsuario().getId());
			}
		}
	}
	
	private boolean controlarReserva(Reserva r) throws Exception {
		//comprobar horario especial y comun
		if(!cumpleConHorariosDelRecurso(r) || !cumpleConHorariosEspecialesDelRecurso(r)
				|| !r.getReservante().isHabilitado() || r.getAsignacionTipoTurno().isEliminado()
				|| r.getAsignacionTipoTurno().getRecurso().isEliminado() 
				|| r.getAsignacionTipoTurno().getTipoTurno().isEliminado()) {
			return false;
		}else {
			return true;
		}
		//corroborar eliminado
	}
	
	private boolean cumpleConHorariosDelRecurso(Reserva r) throws Exception {
		boolean cumple = false;
		List<Horario> horariosComunes = horarioService.horariosDeRecursoParaFecha(r.getAsignacionTipoTurno().getRecurso(),localDateToDia(r.getFecha()));
		for(Horario h : horariosComunes) {
			LocalTime horarioDesde = h.getDesde();
			while(horarioDesde.isBefore(h.getHasta())) {
				if(horarioDesde.equals(r.getHora())) {
					cumple = true;
					break;
				}
				horarioDesde.plusMinutes((long)r.getAsignacionTipoTurno().getDuracionEnMinutos());
			}
		}
		return cumple;
	}
	private boolean cumpleConHorariosEspecialesDelRecurso(Reserva r) throws Exception {
		boolean cumple = false;
		List<HorarioEspecial> horariosEspeciales = horarioEspecialService.horariosEspecialesDeRecursoParaFecha(r.getAsignacionTipoTurno().getRecurso(),r.getFecha());
		for(HorarioEspecial h : horariosEspeciales) {
			if(!h.isCerrado()) {
				LocalTime horarioDesde = h.getDesde();
				while(horarioDesde.isBefore(h.getHasta())) {
					if(horarioDesde.equals(r.getHora())) {
						cumple = true;
						break;
					}
					horarioDesde.plusMinutes((long)r.getAsignacionTipoTurno().getDuracionEnMinutos());
				}	
			}else {
				return false;
			}
		}
		return horariosEspeciales.isEmpty() ? true : cumple;
	}
}
