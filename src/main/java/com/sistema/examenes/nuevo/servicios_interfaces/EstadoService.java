package com.sistema.examenes.nuevo.servicios_interfaces;

import com.sistema.examenes.anterior.modelo.Estado;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.nuevo.servicios.ApiResponse;

public interface EstadoService {
	public ApiResponse<Reserva> nuevoCambioEstadoReserva(Reserva reserva,Estado anterior, Estado nuevo);
	public ApiResponse<Reserva> estadoReservaNueva(Reserva reserva);
}
