package com.sistema.servicios_v2;

import java.util.List;

import com.sistema.anterior.modelo.Reserva;
import com.sistema.modelo.usuario.Usuario;

public interface ReservaServiceV2 {
	Reserva obtenerReservaPorId(Long id) throws Exception;
	Reserva eliminarReserva(Reserva r,  Usuario u) throws Exception;
	void borrarReserva(Long idReserva) throws Exception;
	Reserva editarReserva(Reserva r, Usuario u) throws Exception;
	Reserva nuevaReserva(Reserva r) throws Exception;
	List<Reserva> listarReservas(Usuario u);
	void actualizarReservas() throws Exception;
	void notificarReservas() throws Exception;
}
