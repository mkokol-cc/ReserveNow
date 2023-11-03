package com.sistema.examenes.servicios_v2;

import java.util.Set;

import com.sistema.examenes.anterior.modelo.Horario;
import com.sistema.examenes.anterior.modelo.Recurso;

public interface HorarioServiceV2 {
	Recurso guardarHorariosRecurso(Recurso r) throws Exception;
	void validarLista(Set<Horario> horarios) throws Exception;
}
