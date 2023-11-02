package com.sistema.examenes.servicios_v2;

import java.util.List;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.modelo.usuario.Usuario;

public interface AsignacionServiceV2 {
	AsignacionRecursoTipoTurno obtenerAsignacionPorId(Long idAsignacion) throws Exception;
	AsignacionRecursoTipoTurno nuevaAsignacion(Long idRecurso, Long idTipoTurno, Usuario u) throws Exception;
	AsignacionRecursoTipoTurno editarAsignacion(AsignacionRecursoTipoTurno asig, Usuario u) throws Exception;
	List<AsignacionRecursoTipoTurno> listarAsignaciones(Usuario u);
	void eliminarAsignacion(Long idAsignacion, Usuario u) throws Exception;
}
