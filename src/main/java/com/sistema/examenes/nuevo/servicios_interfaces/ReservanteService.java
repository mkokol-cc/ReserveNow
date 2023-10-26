package com.sistema.examenes.nuevo.servicios_interfaces;

import java.util.List;

import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.modelo.usuario.Usuario;

public interface ReservanteService {
	
	Reservante guardarReservante(Reservante reservante) throws Exception;
	Reservante editarReservante(Reservante reservante, long idUsuario) throws Exception;
	Reservante obtenerPorTelefonoYUsuario(Reservante reservante) throws Exception;
	List<Reservante> listarReservanteDeUsuario(Usuario idUsuario) throws Exception;
}
