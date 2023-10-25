package com.sistema.examenes.nuevo.servicios_interfaces;

import java.util.List;

import com.sistema.examenes.anterior.modelo.Estado;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.nuevo.servicios.ApiResponse;

public interface EstadoService {
	Reserva nuevoCambioEstadoReserva(Reserva reserva,Estado anterior) throws Exception;
	Reserva estadoReservaNueva(Reserva reserva) throws Exception;
	Estado getEstadoByNombre(String nombre) throws Exception;
	
	
	List<Estado> listarEstados() throws Exception;
	Estado getEstadoById(Long idEstado) throws Exception;
}
