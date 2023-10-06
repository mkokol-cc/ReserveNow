package com.sistema.examenes.nuevo.servicios_interfaces;

import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.modelo.usuario.Usuario;

public interface NotificacionService {

	void notificarNuevaReserva(Reserva r,Usuario u);
	//void notificarPago(Reserva r);
}
