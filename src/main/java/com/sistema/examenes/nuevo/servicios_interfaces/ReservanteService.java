package com.sistema.examenes.nuevo.servicios_interfaces;

import org.springframework.http.ResponseEntity;

import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.nuevo.servicios.ApiResponse;

public interface ReservanteService {
	
	public ApiResponse<Reservante> guardarReservante(Reservante reservante);
	public ApiResponse<Reservante> editarReservante(Reservante reservante);
	public ApiResponse<Reservante> obtenerPorTelefonoYUsuario(Reservante reservante);
}
