package com.sistema.servicios_v2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import com.sistema.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.modelo.usuario.Usuario;

public interface AsignacionServiceV2 {
	AsignacionRecursoTipoTurno obtenerAsignacionPorId(Long idAsignacion) throws Exception;
	AsignacionRecursoTipoTurno nuevaAsignacion(Long idRecurso, Long idTipoTurno, Usuario u) throws Exception;
	AsignacionRecursoTipoTurno editarAsignacion(AsignacionRecursoTipoTurno asig, Usuario u) throws Exception;
	List<AsignacionRecursoTipoTurno> listarAsignaciones(Usuario u);
	void eliminarAsignacion(Long idAsignacion, Usuario u) throws Exception;
	public Map<LocalDateTime,Integer> obtenerHorariosDisponibles(Long idAsignacion) throws Exception;
}
