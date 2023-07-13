package com.sistema.examenes.nuevo.servicios;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.HorarioEspecial;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.repositorios.HorarioEspecialRepository;
import com.sistema.examenes.nuevo.servicios_interfaces.HorarioEspecialService;

public class HorarioEspecialServiceImpl implements HorarioEspecialService{

	@Autowired
	private HorarioEspecialRepository horarioEspRepo;
	

	@Override
	public ApiResponse<HorarioEspecial> comprobarHorarioEspecialRecurso(LocalTime hora, LocalDate fecha,
			Recurso recurso) {
		//comprobar si hay un horario especial en la fecha que sea FERIADO
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResponse<HorarioEspecial> comprobarHorarioEspecialAsignacion(LocalTime hora, LocalDate fecha,
			AsignacionRecursoTipoTurno asignacion) {
		List<HorarioEspecial> horariosEspeciales = horarioEspRepo.findByAsignacionAndFecha(asignacion, fecha);
		//si hay horarios especiales de la asignacion para ese dia:
		if(horariosEspeciales.size()>0) {
			HorarioEspecial horarioEspCerrado = null;
			HorarioEspecial horarioEspCompatible = null;
			for(HorarioEspecial he : horariosEspeciales) {
				if(he.isCerrado()) {
					horarioEspCerrado = he;
					//return new ApiResponse<>(false,"Fecha Cerrada: "+he.getMotivo(),he);
				}else{
					LocalTime horaMasDuracion = hora.plusMinutes((long)asignacion.getDuracionEnMinutos());
					if(!he.getDesde().isAfter(hora) && !he.getDesde().isBefore(horaMasDuracion)) {
						horarioEspCompatible = he;
					}
				}
			}
			if(horarioEspCerrado!=null) {
				return new ApiResponse<>(false,"Fecha Cerrada: "+horarioEspCerrado.getMotivo(),horarioEspCerrado);
			}else if(horarioEspCompatible!=null) {
				return new ApiResponse<>(true,"Horario Valido, Motivo horario Especial: "+horarioEspCompatible.getMotivo(),horarioEspCompatible);
			}
			return new ApiResponse<>(false,"Hay Horario Especial Para el dia "+fecha+" y la hora no es valida para ese dia",null);
		}else {
			return new ApiResponse<>(true,"No hay ningun horario especial que se oponga",null);	
		}
	}

	@Override
	public ApiResponse<HorarioEspecial> guardarHorarioEspecial(HorarioEspecial h) {
		// TODO Auto-generated method stub
		return null;
	}

}
