package com.sistema.examenes.nuevo.servicios;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Dias;
import com.sistema.examenes.anterior.modelo.Horario;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.repositorios.HorarioRepository;
import com.sistema.examenes.nuevo.servicios_interfaces.HorarioService;

@Service
public class HorarioServiceImpl implements HorarioService{

	@Autowired
	private HorarioRepository horarioRepo;


	@Override
	public ApiResponse<Horario> comprobarHorarioRecurso(LocalTime hora, Dias dia, Recurso recurso) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResponse<Horario> comprobarHorarioAsignacion(LocalTime hora, Dias dia,
			AsignacionRecursoTipoTurno asignacion) {
		List<Horario> horarios = horarioRepo.obtenerPorHoraYAsignacion(dia, hora, asignacion);
		if(horarios.size()>0) {
			for(Horario h : horarios) {
				LocalTime horaMasDuracion = hora.plusMinutes((long)asignacion.getDuracionEnMinutos());
				if(!horaMasDuracion.isAfter(h.getHasta())) {
					//si horarios.size()>1 entonces hay horarios para la misma fecha que se pisan
					return new ApiResponse<>(true,"Horario Valido",h);
				}
			}
		}
		// TODO Auto-generated method stub
		return new ApiResponse<>(false,"Horario Invalido para el dia "+dia.name()+" a las "+hora,null);
	}

	@Override
	public ApiResponse<Horario> guardarHorarioRecurso(Horario h) {
		
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResponse<Horario> guardarHorarioAsignacion(Horario h, AsignacionRecursoTipoTurno asignacion) {
		try {
			//comprobar si el horario es correcto
			//- si el la hora desde es < hasta
			//- si la duracion del asignacion entre en el horario desde y hasta
			if(!h.getDesde().plusMinutes((long)asignacion.getDuracionEnMinutos()).isAfter(h.getHasta())) {
				h.setAsignacion(asignacion);
				Horario horario = horarioRepo.save(h);
				return new ApiResponse<>(true,"",horario);
			}
			return new ApiResponse<>(false,"Horario: "+h.getDia()+" de "+h.getDesde()+" a "+h.getHasta()+" invalido.",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}

}
