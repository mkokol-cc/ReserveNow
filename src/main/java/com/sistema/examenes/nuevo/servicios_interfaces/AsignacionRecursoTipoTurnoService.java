package com.sistema.examenes.nuevo.servicios_interfaces;

import java.util.List;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.nuevo.servicios.ApiResponse;

public interface AsignacionRecursoTipoTurnoService {

	public ApiResponse<AsignacionRecursoTipoTurno> guardarAsignacion(AsignacionRecursoTipoTurno asignacion);
	public ApiResponse<AsignacionRecursoTipoTurno> editarAsignacion(AsignacionRecursoTipoTurno asignacion);
	public ApiResponse<List<AsignacionRecursoTipoTurno>> listarAsignacion(Long id);
	public ApiResponse<List<AsignacionRecursoTipoTurno>> listarAsignacionPorUsuario(Long idUsuario);
	
	public ApiResponse<AsignacionRecursoTipoTurno> obtenerPorId(Long id);
	
	//public ApiResponse<?> comprobarHorario();
	
	//public ApiResponse<?> comprobarHorarioEspecial();
}
