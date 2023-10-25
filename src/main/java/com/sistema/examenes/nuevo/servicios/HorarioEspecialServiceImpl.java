package com.sistema.examenes.nuevo.servicios;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Horario;
import com.sistema.examenes.anterior.modelo.HorarioEspecial;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.repositorios.HorarioEspecialRepository;
import com.sistema.examenes.nuevo.servicios_interfaces.HorarioEspecialService;

@Service
public class HorarioEspecialServiceImpl implements HorarioEspecialService{

	@Autowired
	private HorarioEspecialRepository horarioEspRepo;
	
/*
	@Override
	public ApiResponse<HorarioEspecial> comprobarHorarioEspecialRecurso(LocalTime hora, LocalDate fecha,
			Recurso recurso) {
		List<HorarioEspecial> horariosEspeciales = horarioEspRepo.findByFechaAndRecurso(fecha,recurso);
		//si hay horarios especiales de la asignacion para ese dia:
		if(horariosEspeciales.size()>0) {
			HorarioEspecial horarioEspCompatible = null;
			for(HorarioEspecial he : horariosEspeciales) {
				if(he.isCerrado()) {
					return new ApiResponse<>(false,"Fecha Cerrada: "+he.getMotivo(),he);
				}else{
					//LocalTime horaMasDuracion = hora.plusMinutes((long)asignacion.getDuracionEnMinutos());
					if(!he.getDesde().isAfter(hora) /*&& !he.getDesde().isBefore(horaMasDuracion)*//*) {
						horarioEspCompatible = he;
					}
				}
			}
			if(horarioEspCompatible!=null) {
				return new ApiResponse<>(true,"Horario Valido, Motivo horario Especial: "+horarioEspCompatible.getMotivo(),horarioEspCompatible);
			}
			return new ApiResponse<>(false,"Hay Horario Especial Para el dia "+fecha+" y la hora no es valida para ese dia",null);
		}else {
			return new ApiResponse<>(true,"No hay ningun horario especial que se oponga",null);	
		}
	}*/

	@Override
	public HorarioEspecial comprobarHorarioEspecialAsignacion(LocalTime hora, LocalDate fecha,
			AsignacionRecursoTipoTurno asignacion) throws Exception {
		List<HorarioEspecial> horariosEspeciales = horarioEspRepo.findByFechaAndAsignacion(fecha,asignacion);
		//si hay horarios especiales de la asignacion para ese dia:
		if(horariosEspeciales.size()>0) {
			HorarioEspecial horarioEspCompatible = null;
			for(HorarioEspecial he : horariosEspeciales) {
				if(he.isCerrado()) {
					throw new Exception("Fecha Cerrada: "+he.getMotivo());
				}else{
					//LocalTime horaMasDuracion = hora.plusMinutes((long)asignacion.getDuracionEnMinutos());
					if(comprobarHorario(he.getDesde(),he.getHasta(),asignacion.getDuracionEnMinutos(),hora)) {
						horarioEspCompatible = he;
					}
				}
			}
			if(horarioEspCompatible!=null) {
				return horarioEspCompatible;
			}
			throw new Exception("Hay Horario Especial Para el dia "+fecha+" y la hora no es valida para ese dia");
		}
		return new HorarioEspecial();
	}

	@Override
	public HorarioEspecial guardarHorarioEspecial(HorarioEspecial h) throws Exception {
		//comprobar si el horario es correcto
		//- si el la hora desde es < hasta
		//- si la duracion del asignacion entre en el horario desde y hasta
		if(h.isCerrado() || !h.getDesde().isAfter(h.getHasta())) {
			HorarioEspecial horario = horarioEspRepo.save(h);
			return horario;
		}
		throw new Exception("Horario Especial: "+h.getFecha()+" de "+h.getDesde()+" a "+h.getHasta()+" invalido.");
	}
	
	
	

	public HorarioEspecial guardarHorarioEspecialRecurso(HorarioEspecial h, Recurso r) throws Exception {
		//comprobar si el horario es correcto
		//- si el la hora desde es < hasta
		//- si la duracion del asignacion entre en el horario desde y hasta
		if(h.isCerrado() || !h.getDesde().isAfter(h.getHasta())) {
			h.setRecurso(r);
			HorarioEspecial horario = horarioEspRepo.save(h);
			return horario;
		}
		throw new Exception("Horario Especial: "+h.getFecha()+" de "+h.getDesde()+" a "+h.getHasta()+" invalido.");
	}

	public HorarioEspecial guardarHorarioEspecialAsignacion(HorarioEspecial h, AsignacionRecursoTipoTurno a) throws Exception {
		//comprobar si el horario es correcto
		//- si el la hora desde es < hasta
		//- si la duracion del asignacion entre en el horario desde y hasta
		//LocalTime duracion = h.getDesde().plusMinutes((long)a.getDuracionEnMinutos());
		if(h.isCerrado() || !h.getDesde().plusMinutes((long)a.getDuracionEnMinutos()).isAfter(h.getHasta())) {
			h.setAsignacion(a);
			HorarioEspecial horario = horarioEspRepo.save(h);
			return horario;
		}
		throw new Exception("Horario Especial: "+h.getFecha()+" de "+h.getDesde()+" a "+h.getHasta()+" invalido.");
	}
	
	

	@Override
	public Recurso guardarListaHorarioEspecialRecurso(Recurso recurso) throws Exception {
		String mensajeError = "";
		Set<HorarioEspecial> horarios = new HashSet<>();
		horarioEspRepo.deleteByRecurso(recurso);
		for(HorarioEspecial h : recurso.getHorariosEspeciales()) {
			try {
				horarios.add(guardarHorarioEspecialRecurso(h,recurso));
			}catch(Exception e) {
				mensajeError += "Horario: "+h.getFecha()+" de "+h.getDesde()+" a "+h.getHasta()+", "+e.getMessage()+". ";
			}
		}
		if(mensajeError.equals("")) {
			recurso.setHorariosEspeciales(horarios);
			return recurso;
		}
		throw new Exception(mensajeError);
	}

	@Override
	public AsignacionRecursoTipoTurno guardarListaHorarioEspecialAsignacion(AsignacionRecursoTipoTurno a) throws Exception {
		String mensajeError = "";
		Set<HorarioEspecial> horarios = new HashSet<>();
		horarioEspRepo.deleteByAsignacion(a);
		for(HorarioEspecial h : a.getHorariosEspeciales()) {
			try {
				horarios.add(guardarHorarioEspecialAsignacion(h,a));
			}catch(Exception e) {
				mensajeError += "Horario: "+h.getFecha()+" de "+h.getDesde()+" a "+h.getHasta()+", "+e.getMessage()+". ";
			}
		}
		if(mensajeError.equals("")) {
			a.setHorariosEspeciales(horarios);
			return a;
		}
		throw new Exception(mensajeError);
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
	public List<HorarioEspecial> horariosEspecialesDeAsignacionParaFecha(AsignacionRecursoTipoTurno asig, LocalDate fecha){
		List<HorarioEspecial> horarios = horarioEspRepo.findByFechaAndAsignacion(fecha, asig);
		return horarios;
	}
	
	@Override
	public List<HorarioEspecial> horariosEspecialesDeRecursoParaFecha(Recurso recurso, LocalDate fecha){
		List<HorarioEspecial> horarios = horarioEspRepo.findByFechaAndRecurso(fecha, recurso);
		return horarios;
	}
}
