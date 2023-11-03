package com.sistema.examenes.servicios_v2;

import java.util.Set;

import com.sistema.examenes.anterior.modelo.HorarioEspecial;
import com.sistema.examenes.anterior.modelo.Recurso;

public interface HorarioEspecialServiceV2 {
	Recurso guardarHorariosEspecialesRecurso(Recurso r) throws Exception;
	void validarLista(Set<HorarioEspecial> horarios) throws Exception;
}
