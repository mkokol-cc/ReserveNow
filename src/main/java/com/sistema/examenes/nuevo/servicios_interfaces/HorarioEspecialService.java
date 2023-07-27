package com.sistema.examenes.nuevo.servicios_interfaces;

import java.time.LocalDate;
import java.time.LocalTime;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.HorarioEspecial;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.nuevo.servicios.ApiResponse;

public interface HorarioEspecialService {

	public ApiResponse<HorarioEspecial> guardarHorarioEspecial(HorarioEspecial h);
	
	public ApiResponse<Recurso> guardarListaHorarioEspecialRecurso(Recurso recurso);
	public ApiResponse<AsignacionRecursoTipoTurno> guardarListaHorarioEspecialAsignacion(AsignacionRecursoTipoTurno a);
	
	/*public ApiResponse<HorarioEspecial> comprobarHorarioEspecialRecurso(LocalTime hora, LocalDate fecha, Recurso recurso);*/
	public ApiResponse<HorarioEspecial> comprobarHorarioEspecialAsignacion(LocalTime hora, LocalDate fecha,AsignacionRecursoTipoTurno asignacion);
}
