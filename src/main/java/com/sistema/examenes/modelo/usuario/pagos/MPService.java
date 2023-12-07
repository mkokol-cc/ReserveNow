package com.sistema.examenes.modelo.usuario.pagos;

import com.mercadopago.resources.preference.Preference;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.modelo.usuario.Usuario;

public interface MPService {
	Preference crearPagoLicencia(Usuario u, Licencia l, Pago p) throws Exception;
	Preference crearPagoSeña(Reserva r) throws Exception;
}
