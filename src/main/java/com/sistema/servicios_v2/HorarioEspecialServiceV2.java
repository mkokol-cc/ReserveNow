package com.sistema.servicios_v2;

import java.util.Set;

import com.sistema.anterior.modelo.HorarioEspecial;
import com.sistema.anterior.modelo.Recurso;

public interface HorarioEspecialServiceV2 {
	Recurso guardarHorariosEspecialesRecurso(Recurso r) throws Exception;
	void validarLista(Set<HorarioEspecial> horarios) throws Exception;
}
