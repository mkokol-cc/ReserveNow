package com.sistema.notificaciones;

import com.sistema.anterior.modelo.Reserva;
import com.sistema.modelo.usuario.Usuario;

public interface NotificationsService {
	
	public void notificarWppCambioEstadoReserva(Reserva r) throws Exception;
	
	//public void notificarWppNuevaReservaReservante() ;

	void enviarEmailReestablecerClave(Usuario usuario) throws Exception;

	void notificarEmailNuevaReservaAdministrador(Usuario usuario, Reserva r) throws Exception;

	void enviarEmailValidacionUsuario(Usuario usuario) throws Exception;

	void notificarVencimientoLicencia(Usuario usuario) throws Exception;

	void notificarPagoCorrectoLicencia(Usuario usuario) throws Exception;

	void notificarWppProximaReserva(Reserva r) throws Exception;

	void enviarWppValidacionUsuario(String telefono, String codigo) throws Exception;
	
	void enviarEmailBienvenida(Usuario u) throws Exception;
	
}
