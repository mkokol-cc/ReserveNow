package com.sistema.examenes.nuevo.servicios_interfaces;

import java.time.LocalTime;
import java.util.List;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Dias;
import com.sistema.examenes.anterior.modelo.Horario;
import com.sistema.examenes.anterior.modelo.Recurso;

public interface HorarioService {
	
	Horario guardarHorarioRecurso(Horario h, Recurso r) throws Exception;
	Horario guardarHorarioAsignacion(Horario h, AsignacionRecursoTipoTurno asignacion) throws Exception;
	
	Recurso guardarListaHorariosRecurso(Recurso r) throws Exception;
	AsignacionRecursoTipoTurno guardarListaHorariosAsignacion(AsignacionRecursoTipoTurno asignacion) throws Exception;
	
	/*public ApiResponse<Horario> comprobarHorarioRecurso(LocalTime hora, Dias dia, Recurso recurso);*/
	Horario comprobarHorarioAsignacion(LocalTime hora, Dias dia, AsignacionRecursoTipoTurno asignacion) throws Exception;
	
	List<Horario> horariosDeAsignacionParaFecha(AsignacionRecursoTipoTurno asig, Dias dia) throws Exception;
	List<Horario> horariosDeRecursoParaFecha(Recurso recurso, Dias dia) throws Exception;
}
