package com.sistema.nuevo.servicios_interfaces;

import com.sistema.anterior.modelo.Reserva;
import com.sistema.modelo.usuario.Usuario;

public interface NotificacionService {

	void notificarNuevaReserva(Reserva r,Usuario u);
	//void notificarPago(Reserva r);
}
