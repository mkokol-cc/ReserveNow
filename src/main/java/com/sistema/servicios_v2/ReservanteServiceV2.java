package com.sistema.servicios_v2;

import java.util.List;

import com.sistema.anterior.modelo.Reservante;
import com.sistema.modelo.usuario.Usuario;

public interface ReservanteServiceV2 {
	Reservante nuevoReservante(Reservante r) throws Exception;//retorna el reservante guardado si esta sino lo guarda y lo retorna

	List<Reservante> listarReservantes(Usuario u) throws Exception;

	Reservante editarReservante(Reservante r, Usuario u) throws Exception;
}
