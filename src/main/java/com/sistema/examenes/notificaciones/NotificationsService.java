package com.sistema.examenes.notificaciones;

import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.modelo.usuario.Usuario;

public interface NotificationsService {
	
	public void notificarWppCambioEstadoReserva();
	
	public void notificarWppNuevaReservaReservante() ;

	void enviarEmailReestablecerClave(Usuario usuario) throws Exception;

	void notificarEmailNuevaReservaAdministrador(Usuario usuario, Reserva r) throws Exception;

	void enviarEmailValidacionUsuario(Usuario usuario) throws Exception;

	void notificarVencimientoLicencia(Usuario usuario) throws Exception;

	void notificarPagoCorrectoLicencia(Usuario usuario) throws Exception;
	
}
