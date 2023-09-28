package com.sistema.examenes.nuevo.servicios_interfaces;

import java.util.List;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios.ApiResponse;

public interface AsignacionRecursoTipoTurnoService {

	public ApiResponse<AsignacionRecursoTipoTurno> guardarAsignacion(long idTipoTurno, long idRecurso/*AsignacionRecursoTipoTurno asignacion*/, long idUsuario);
	public ApiResponse<AsignacionRecursoTipoTurno> editarAsignacion(AsignacionRecursoTipoTurno asignacion, long idUsuario);
	public ApiResponse<List<AsignacionRecursoTipoTurno>> listarAsignacion(Long id);
	public ApiResponse<List<AsignacionRecursoTipoTurno>> listarAsignacionPorUsuario(Long idUsuario);
	
	public ApiResponse<AsignacionRecursoTipoTurno> obtenerPorId(Long id);
	
	
	public ApiResponse<AsignacionRecursoTipoTurno> eliminarAsignacion(Long idTipoTurno, Long idRecurso, Long idUsuario);
	
	
	public ApiResponse<Recurso> actualizarAsignaciones(List<Long> idTiposDeTurno, long recId,Usuario usuario);
	public ApiResponse<AsignacionRecursoTipoTurno> habilitarAsignacion(Long idTipoTurno, Long idRecurso, Long idUsuario);
	
	//public ApiResponse<?> comprobarHorario();
	
	//public ApiResponse<?> comprobarHorarioEspecial();
}
