package com.sistema.modelo.pagosMP;

import com.sistema.modelo.usuario.Usuario;

public interface PagoService {
	

	public Pago nuevoPago(Licencia l, Usuario u) throws Exception;

	Pago cambiarEstadoPago(int estado, Long id) throws Exception;
}
