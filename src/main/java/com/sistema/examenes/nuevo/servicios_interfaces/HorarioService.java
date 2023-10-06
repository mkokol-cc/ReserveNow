package com.sistema.examenes.nuevo.servicios_interfaces;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Dias;
import com.sistema.examenes.anterior.modelo.Horario;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.nuevo.servicios.ApiResponse;

public interface HorarioService {
	
	public ApiResponse<Horario> guardarHorarioRecurso(Horario h, Recurso r);
	public ApiResponse<Horario> guardarHorarioAsignacion(Horario h, AsignacionRecursoTipoTurno asignacion);
	
	public ApiResponse<Recurso> guardarListaHorariosRecurso(Recurso r);
	public ApiResponse<AsignacionRecursoTipoTurno> guardarListaHorariosAsignacion(AsignacionRecursoTipoTurno asignacion);
	
	/*public ApiResponse<Horario> comprobarHorarioRecurso(LocalTime hora, Dias dia, Recurso recurso);*/
	public ApiResponse<Horario> comprobarHorarioAsignacion(LocalTime hora, Dias dia, AsignacionRecursoTipoTurno asignacion);
	
	public ApiResponse<List<Horario>> horariosDeAsignacionParaFecha(AsignacionRecursoTipoTurno asig, Dias dia);
	ApiResponse<List<Horario>> horariosDeRecursoParaFecha(Recurso recurso, Dias dia);
}
