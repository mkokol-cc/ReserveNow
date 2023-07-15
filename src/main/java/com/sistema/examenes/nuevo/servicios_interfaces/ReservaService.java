package com.sistema.examenes.nuevo.servicios_interfaces;

import java.util.List;

import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.nuevo.servicios.ApiResponse;

public interface ReservaService {

	public ApiResponse<Reserva> guardarReserva(Reserva r);
	public ApiResponse<Reserva> editarReserva(Reserva r);
	public ApiResponse<List<Reserva>> listarReservaPorUsuario(long idUsuario);
	public ApiResponse<Reserva> eliminarReservaPorId(long idReserva,long idUsuario);
	
}
