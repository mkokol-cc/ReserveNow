package com.sistema.examenes.nuevo.servicios_interfaces;

import java.time.LocalDate;
import java.util.List;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.nuevo.dto.TurnoDTO;
import com.sistema.examenes.nuevo.servicios.ApiResponse;

public interface ReservaService {

	public ApiResponse<Reserva> guardarReserva(Reserva r, long idUsuario);
	public ApiResponse<Reserva> editarReserva(Reserva r, long idUsuario);
	public ApiResponse<List<Reserva>> listarReservaPorUsuario(long idUsuario);
	public ApiResponse<Reserva> eliminarReservaPorId(long idReserva,long idUsuario);
	ApiResponse<List<TurnoDTO>> crearTurnos(Long idAsignacion, LocalDate fecha);
	
	
	public ApiResponse<Reserva> nuevaReserva(Reserva r, long idAsignacion);
	
}
