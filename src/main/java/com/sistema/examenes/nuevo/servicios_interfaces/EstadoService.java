package com.sistema.examenes.nuevo.servicios_interfaces;

import com.sistema.examenes.anterior.modelo.Estado;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.nuevo.servicios.ApiResponse;

public interface EstadoService {
	ApiResponse<Reserva> nuevoCambioEstadoReserva(Reserva reserva);
	ApiResponse<Reserva> estadoReservaNueva(Reserva reserva);
	ApiResponse<Estado> getEstadoByNombre(String nombre);
}
