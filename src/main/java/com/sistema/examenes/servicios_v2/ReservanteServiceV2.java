package com.sistema.examenes.servicios_v2;

import com.sistema.examenes.anterior.modelo.Reservante;

public interface ReservanteServiceV2 {
	Reservante nuevoReservante(Reservante r) throws Exception;//retorna el reservante guardado si esta sino lo guarda y lo retorna
}
