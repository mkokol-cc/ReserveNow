package com.sistema.examenes.nuevo.servicios_interfaces;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.nuevo.dto.TurnoDTO;

public interface ReservaService {

	Reserva guardarReserva(Reserva r, long idUsuario) throws Exception;
	Reserva editarReserva(Reserva r, long idUsuario) throws Exception;
	List<Reserva> listarReservaPorUsuario(long idUsuario) throws Exception;
	Reserva eliminarReservaPorId(long idReserva,long idUsuario) throws Exception;
	List<TurnoDTO> crearTurnos(Long idAsignacion, LocalDate fecha) throws Exception;
	
	
	Reserva nuevaReserva(Reserva r, long idAsignacion) throws Exception;
	List<Reserva> obtenerReservasInvolucradasEnRango(Long idRecurso, LocalDate fecha, LocalTime desde,
			LocalTime hasta) throws Exception;
	List<Reserva> cambiarEstadosDeListaDeIdsReserva(Long idEstado, List<Long> idReserva) throws Exception;
	
	
	public List<Reserva> obtenerReservasEnEstadoNoFinal(LocalDate fecha, LocalTime desde, LocalTime hasta);
	public List<Reserva> obtenerReservasEnEstadoNoFinal(AsignacionRecursoTipoTurno asignacion);
	public List<Reserva> obtenerReservasEnEstadoNoFinal(Recurso r);
	public List<Reserva> obtenerReservasEnEstadoNoFinal(TipoTurno t);
	
	
	void eliminarReservasMalRegistradas(List<Reserva> reservas) throws Exception;
	void eliminarReserva(Reserva r) throws Exception;
}
