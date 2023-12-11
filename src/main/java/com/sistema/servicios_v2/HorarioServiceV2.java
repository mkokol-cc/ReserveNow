package com.sistema.servicios_v2;

import java.util.Set;

import com.sistema.anterior.modelo.Horario;
import com.sistema.anterior.modelo.Recurso;

public interface HorarioServiceV2 {
	Recurso guardarHorariosRecurso(Recurso r) throws Exception;
	void validarLista(Set<Horario> horarios) throws Exception;
}
