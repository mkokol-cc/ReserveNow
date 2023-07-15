package com.sistema.examenes.nuevo.servicios_interfaces;

import java.util.List;

import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios.ApiResponse;

public interface ReservanteService {
	
	public ApiResponse<Reservante> guardarReservante(Reservante reservante);
	public ApiResponse<Reservante> editarReservante(Reservante reservante, long idUsuario);
	public ApiResponse<Reservante> obtenerPorTelefonoYUsuario(Reservante reservante);
	public ApiResponse<List<Reservante>> listarReservanteDeUsuario(Usuario idUsuario);
}
