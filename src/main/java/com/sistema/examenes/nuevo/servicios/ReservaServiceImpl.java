package com.sistema.examenes.nuevo.servicios;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Dias;
import com.sistema.examenes.anterior.modelo.Estado;
import com.sistema.examenes.anterior.modelo.Horario;
import com.sistema.examenes.anterior.modelo.HorarioEspecial;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.anterior.repositorios.ReservaRepository;
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
	
	@Override
	public ApiResponse<Reserva> guardarReserva(Reserva reserva, long idUsuario) {
        try {
        	ApiResponse<Reserva> datosObligResponse = setDatosObligatorios(reserva, idUsuario);
        	if(datosObligResponse.isSuccess()) {
        		ApiResponse<Reserva> horarioResponse = setHorariosReserva(reserva);
        		if(horarioResponse.isSuccess()) {
        			Reserva guardado = reservaRepo.save(reserva);
        			return new ApiResponse<>(true,"",guardado);	
        		}else {
        			return new ApiResponse<Reserva>(false,horarioResponse.getMessage(),null);
        		}
        	}else {
        		return new ApiResponse<>(false,"Datos Erroneos: "+datosObligResponse.getMessage(),null);
        	}
        } catch (Exception e) {
        	return new ApiResponse<>(false,e.getMessage(),null);
        }
	}
	

	@Override
	public ApiResponse<Reserva> editarReserva(Reserva r) {
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
		return new ApiResponse<>(true,"",guardado);
	}

	@Override
	public ApiResponse<List<Reserva>> listarReservaPorUsuario(long idUsuario) {
		ApiResponse<List<AsignacionRecursoTipoTurno>> asignaciones = asignacionService.listarAsignacionPorUsuario(idUsuario);
		if(asignaciones.isSuccess()) {
			List<Reserva> reservas = new ArrayList<>();
			for(AsignacionRecursoTipoTurno a : asignaciones.getData()) {
				reservas.addAll(a.getReservas());
			}
			return new ApiResponse<>(true,"",reservas);
		}
		return new ApiResponse<>(false,"Hubo un Problema al obtener las Reservas",null);
	}
	
	@Override
	public ApiResponse<Reserva> eliminarReservaPorId(long r, long idUsuario) {
		try {
			Reserva reserva = reservaRepo.getById(r);
			if(reserva!=null) {
				if(reserva.getAsignacionTipoTurno().getRecurso().getUsuario().getId()==idUsuario) {
					reservaRepo.delete(reserva);
					return new ApiResponse<>(true,"",null);	
				}
				return new ApiResponse<>(false,"No se encontro la reserva",null);
			}
			return new ApiResponse<>(false,"Usuario no autorizado",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,"Error: "+e.getMessage(),null);
		}
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
    			if(setEstadoResponse.isSuccess()) {
    				return new ApiResponse<>(true,"",setEstadoResponse.getData());	
    			}else {
    				return new ApiResponse<>(false,setEstadoResponse.getMessage(),null);
    			}
    		}else {
    			return new ApiResponse<>(false,resAsig.getMessage(),null);
    		}
    	}else {
    		return new ApiResponse<>(false,resReservante.getMessage(),null);
    	}
	}
	
	private ApiResponse<Reserva> cambiarEstado(Reserva r, Estado eAnterior) {
		//nuevo cambio estado y set nuevo estado
		ApiResponse<Reserva> estadoResponse = estadoService.nuevoCambioEstadoReserva(r,eAnterior);
		if(estadoResponse.isSuccess()) {
			return new ApiResponse<>(true,"",estadoResponse.getData());
		}
		return new ApiResponse<>(false,"Cambio de Estado Invalido",null);
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
				}else {
					return respuestaHorarioEsp;
				}
			}else {
				return respuestaHorarioEsp;
			}	
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
    		//ApiResponse
    		//--responder que no es posible, ya que para esa fecha hay un horario especial que lo impide
    		return new ApiResponse<>(false,"horario invalido: "+he.getMessage(),r);
    		//return ;
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

}
