package com.sistema.examenes.nuevo.servicios;

import java.time.LocalTime;
import java.util.List;

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
	private HorarioRepository HorarioRepo;
	
	@Override
	public ApiResponse<Horario> guardarHorario(Horario h) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResponse<Horario> comprobarHorarioRecurso(LocalTime hora, Dias dia, Recurso recurso) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResponse<Horario> comprobarHorarioAsignacion(LocalTime hora, Dias dia,
			AsignacionRecursoTipoTurno asignacion) {
		List<Horario> horarios = HorarioRepo.obtenerPorHoraYAsignacion(dia, hora, asignacion);
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

}
