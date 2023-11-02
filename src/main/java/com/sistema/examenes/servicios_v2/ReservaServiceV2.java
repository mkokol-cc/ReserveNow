package com.sistema.examenes.servicios_v2;

import java.util.List;

import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.modelo.usuario.Usuario;

public interface ReservaServiceV2 {
	Reserva obtenerReservaPorId(Long id) throws Exception;
	Reserva eliminarReserva(Reserva r,  Usuario u);
	void borrarReserva(Long idReserva) throws Exception;
	Reserva editarReserva(Reserva r, Usuario u) throws Exception;
	Reserva nuevaReserva(Reserva r);
	List<Reserva> listarReservas(Usuario u);
}
