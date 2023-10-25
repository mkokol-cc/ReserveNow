package com.sistema.examenes.nuevo.servicios_interfaces;

import java.util.List;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios.ApiResponse;

public interface AsignacionRecursoTipoTurnoService {

	AsignacionRecursoTipoTurno guardarAsignacion(long idTipoTurno, long idRecurso/*AsignacionRecursoTipoTurno asignacion*/, long idUsuario) throws Exception;
	AsignacionRecursoTipoTurno editarAsignacion(AsignacionRecursoTipoTurno asignacion, long idUsuario) throws Exception;
	//public ApiResponse<List<AsignacionRecursoTipoTurno>> listarAsignacion(Long id);
	List<AsignacionRecursoTipoTurno> listarAsignacionPorUsuario(Long idUsuario) throws Exception;
	
	AsignacionRecursoTipoTurno obtenerPorId(Long id) throws Exception;
	
	
	AsignacionRecursoTipoTurno eliminarAsignacion(Long idTipoTurno, Long idRecurso, Long idUsuario) throws Exception;
	
	
	Recurso actualizarAsignaciones(List<Long> idTiposDeTurno, long recId,Usuario usuario) throws Exception;
	AsignacionRecursoTipoTurno habilitarAsignacion(Long idTipoTurno, Long idRecurso, Long idUsuario) throws Exception;
	
	//public ApiResponse<?> comprobarHorario();
	
	//public ApiResponse<?> comprobarHorarioEspecial();
}
