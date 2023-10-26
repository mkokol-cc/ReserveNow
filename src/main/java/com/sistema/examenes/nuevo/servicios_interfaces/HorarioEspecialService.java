package com.sistema.examenes.nuevo.servicios_interfaces;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.HorarioEspecial;
import com.sistema.examenes.anterior.modelo.Recurso;

public interface HorarioEspecialService {

	HorarioEspecial guardarHorarioEspecial(HorarioEspecial h) throws Exception;
	
	Recurso guardarListaHorarioEspecialRecurso(Recurso recurso) throws Exception;
	AsignacionRecursoTipoTurno guardarListaHorarioEspecialAsignacion(AsignacionRecursoTipoTurno a) throws Exception;
	
	/*public ApiResponse<HorarioEspecial> comprobarHorarioEspecialRecurso(LocalTime hora, LocalDate fecha, Recurso recurso);*/
	HorarioEspecial comprobarHorarioEspecialAsignacion(LocalTime hora, LocalDate fecha,AsignacionRecursoTipoTurno asignacion) throws Exception;
	
	List<HorarioEspecial> horariosEspecialesDeAsignacionParaFecha(AsignacionRecursoTipoTurno asig, LocalDate fecha) throws Exception;

	List<HorarioEspecial> horariosEspecialesDeRecursoParaFecha(Recurso recurso, LocalDate fecha) throws Exception;
}
