package com.sistema.examenes.nuevo.servicios;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Dias;
import com.sistema.examenes.anterior.modelo.Horario;
import com.sistema.examenes.anterior.modelo.HorarioEspecial;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.repositorios.HorarioRepository;
import com.sistema.examenes.nuevo.servicios_interfaces.HorarioService;

@Service
public class HorarioServiceImpl implements HorarioService{

	@Autowired
	private HorarioRepository horarioRepo;

	/*
	@Override
	public ApiResponse<Horario> comprobarHorarioRecurso(LocalTime hora, Dias dia, Recurso recurso) {
		//comprobar si no hay horarios para el mismo dia que se pisen
		List<Horario> horarios = horarioRepo.obtenerPorHoraYRecurso(dia, hora, recurso);
		if(horarios.size()>0) {
			for(Horario h : horarios) {
				//LocalTime horaMasDuracion = hora.plusMinutes((long)asignacion.getDuracionEnMinutos());
				if(!hora.isAfter(h.getHasta())) {
					//si horarios.size()>1 entonces hay horarios para la misma fecha que se pisan
					return new ApiResponse<>(true,"Horario Valido",h);
				}
			}
		}
		// TODO Auto-generated method stub
		return new ApiResponse<>(false,"Horario Invalido para el dia "+dia.name()+" a las "+hora,null);
	}*/

	@Override
	public ApiResponse<Horario> comprobarHorarioAsignacion(LocalTime hora, Dias dia,
			AsignacionRecursoTipoTurno asignacion) {
		//comprobar si no hay horarios para el mismo dia que se pisen
		List<Horario> horarios = horarioRepo.obtenerPorHoraYAsignacion(dia, hora, asignacion);
		if(horarios.size()>0) {
			for(Horario h : horarios) {
				//LocalTime horaMasDuracion = hora.plusMinutes((long)asignacion.getDuracionEnMinutos());
				if(comprobarHorario(h.getDesde(),h.getHasta(),asignacion.getDuracionEnMinutos(),hora)/*!horaMasDuracion.isAfter(h.getHasta())*/) {
					//si horarios.size()>1 entonces hay horarios para la misma fecha que se pisan
					return new ApiResponse<>(true,"Horario Valido",h);
				}
			}
		}
		// TODO Auto-generated method stub
		return new ApiResponse<>(false,"Horario Invalido para el dia "+dia.name()+" a las "+hora,null);
	}

	@Override
	public ApiResponse<Horario> guardarHorarioRecurso(Horario h, Recurso r) {
		try {
			//comprobar si el horario es correcto
			//- si el la hora desde es < hasta
			//- si la duracion del asignacion entre en el horario desde y hasta
			if(!h.getDesde()/*.plusMinutes((long)asignacion.getDuracionEnMinutos())*/.isAfter(h.getHasta())) {
				if(horarioRepo.obtenerPorHoraYRecurso(h.getDia(), h.getDesde(), r).isEmpty() && 
						horarioRepo.obtenerPorHoraYRecurso(h.getDia(), h.getHasta(), r).isEmpty()) {
					h.setRecurso(r);
					Horario horario = horarioRepo.save(h);
					return new ApiResponse<>(true,"",horario);	
				}
				return new ApiResponse<>(false,"Hay horarios que interfieren entre sí",null);
			}
			return new ApiResponse<>(false,"Horario: "+h.getDia()+" de "+h.getDesde()+" a "+h.getHasta()+" invalido.",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}

	@Override
	public ApiResponse<Horario> guardarHorarioAsignacion(Horario h, AsignacionRecursoTipoTurno asignacion) {
		try {
			//comprobar si el horario es correcto
			//- si el la hora desde es < hasta
			//- si la duracion del asignacion entre en el horario desde y hasta
			if(!h.getDesde().plusMinutes((long)asignacion.getDuracionEnMinutos()).isAfter(h.getHasta())) {
				if(horarioRepo.obtenerPorHoraYAsignacion(h.getDia(), h.getDesde(), asignacion).isEmpty() && 
						horarioRepo.obtenerPorHoraYAsignacion(h.getDia(), h.getHasta(), asignacion).isEmpty()) {
					h.setAsignacion(asignacion);
					Horario horario = horarioRepo.save(h);
					return new ApiResponse<>(true,"",horario);	
				}
				return new ApiResponse<>(false,"Hay horarios que interfieren entre sí",null);
			}
			return new ApiResponse<>(false,"Horario: "+h.getDia()+" de "+h.getDesde()+" a "+h.getHasta()+" invalido.",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}
	
	@Override
	public ApiResponse<Recurso> guardarListaHorariosRecurso(Recurso r){
		try {
			Set<Horario> horarios = new HashSet<>();
			horarioRepo.deleteByRecurso(r);
			for(Horario h : r.getHorarios()) {
				ApiResponse<Horario> resp = guardarHorarioRecurso(h,r);
				if(!resp.isSuccess()) {
					return new ApiResponse<>(false,"Error al guardar los Horarios: "+resp.getMessage(),null);
				}
				horarios.add(resp.getData());
			}
			r.setHorarios(horarios);
			return new ApiResponse<>(true,"",r);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}
	@Override
	public ApiResponse<AsignacionRecursoTipoTurno> guardarListaHorariosAsignacion(AsignacionRecursoTipoTurno a){
		try {
			Set<Horario> horarios = new HashSet<>();
			horarioRepo.deleteByAsignacion(a);
			for(Horario h : a.getHorarios()) {
				ApiResponse<Horario> resp = guardarHorarioAsignacion(h,a);
				if(!resp.isSuccess()) {
					return new ApiResponse<>(false,"Error al guardar los Horarios: "+resp.getMessage(),null);
				}
				horarios.add(resp.getData());
			}
			a.setHorarios(horarios);
			return new ApiResponse<>(true,"",a);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}
	
	
	
	
	private boolean comprobarHorario(LocalTime desde, LocalTime hasta, int duracion, LocalTime hora) {
		LocalTime auxiliar = desde;
		while (auxiliar.isBefore(hasta)) {
			if(hora.equals(auxiliar)) {
				return true;
			}
			auxiliar.plusMinutes(duracion);
		    // Código a ejecutar mientras la condición sea verdadera
		}
		return false;
	}
	
	@Override
	public ApiResponse<List<Horario>> horariosDeAsignacionParaFecha(AsignacionRecursoTipoTurno asig, Dias dia){
		try {
			List<Horario> horarios = horarioRepo.findByAsignacionAndDia(asig, dia);
			return new ApiResponse<>(true,"",horarios);
		}catch(Exception e) {
			return new ApiResponse<>(false,"Error al obtener los horarios para el dia "+dia.name(),null);
		}
	}
	@Override
	public ApiResponse<List<Horario>> horariosDeRecursoParaFecha(Recurso recurso, Dias dia){
		try {
			List<Horario> horarios = horarioRepo.findByRecursoAndDia(recurso, dia);
			return new ApiResponse<>(true,"",horarios);
		}catch(Exception e) {
			return new ApiResponse<>(false,"Error al obtener los horarios para el dia "+dia.name(),null);
		}
	}
	

}
