package com.sistema.modelo.pagosMP;

import com.mercadopago.resources.preference.Preference;
import com.sistema.anterior.modelo.Reserva;
import com.sistema.modelo.usuario.Usuario;

public interface MPService {
	Preference crearPagoLicencia(Usuario u, Licencia l, Pago p) throws Exception;
	Preference crearPagoSeña(Reserva r) throws Exception;
}
