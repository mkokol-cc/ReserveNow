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
	public Horario comprobarHorarioAsignacion(LocalTime hora, Dias dia,
			AsignacionRecursoTipoTurno asignacion) throws Exception {
		//comprobar si no hay horarios para el mismo dia que se pisen
		List<Horario> horarios = horarioRepo.obtenerPorHoraYAsignacion(dia, hora, asignacion);
		if(horarios.size()>0) {
			for(Horario h : horarios) {
				//LocalTime horaMasDuracion = hora.plusMinutes((long)asignacion.getDuracionEnMinutos());
				if(comprobarHorario(h.getDesde(),h.getHasta(),asignacion.getDuracionEnMinutos(),hora)/*!horaMasDuracion.isAfter(h.getHasta())*/) {
					//si horarios.size()>1 entonces hay horarios para la misma fecha que se pisan
					return h;
				}
			}
		}
		// TODO Auto-generated method stub
		throw new Exception("Horario Invalido para el dia "+dia.name()+" a las "+hora);
	}

	@Override
	public Horario guardarHorarioRecurso(Horario h, Recurso r) throws Exception {
		//comprobar si el horario es correcto
		//- si el la hora desde es < hasta
		//- si la duracion del asignacion entre en el horario desde y hasta
		if(!h.getDesde().isAfter(h.getHasta())) {
			if(horarioRepo.obtenerPorHoraYRecurso(h.getDia(), h.getDesde(), r).isEmpty() && 
					horarioRepo.obtenerPorHoraYRecurso(h.getDia(), h.getHasta(), r).isEmpty()) {
				h.setRecurso(r);
				Horario horario = horarioRepo.save(h);
				if(horario!=null) {
					return horario;
				}
				throw new Exception("Error al guardar el horario en la base de datos");	
			}
			throw new Exception("Hay horarios que interfieren entre sí");
		}
		throw new Exception("Horario: "+h.getDia()+" de "+h.getDesde()+" a "+h.getHasta()+" invalido.");
	}

	@Override
	public Horario guardarHorarioAsignacion(Horario h, AsignacionRecursoTipoTurno asignacion) throws Exception {
		if(!h.getDesde().plusMinutes((long)asignacion.getDuracionEnMinutos()).isAfter(h.getHasta())) {
			if(horarioRepo.obtenerPorHoraYAsignacion(h.getDia(), h.getDesde(), asignacion).isEmpty() && 
					horarioRepo.obtenerPorHoraYAsignacion(h.getDia(), h.getHasta(), asignacion).isEmpty()) {
				h.setAsignacion(asignacion);
				Horario horario = horarioRepo.save(h);
				if(horario!=null) {
					return horario;
				}
				throw new Exception("Error al guardar el horario en la base de datos");	
			}
			throw new Exception("Hay horarios que interfieren entre sí");
		}
		throw new Exception("Horario: "+h.getDia()+" de "+h.getDesde()+" a "+h.getHasta()+" invalido.");
	}
	
	@Override
	public Recurso guardarListaHorariosRecurso(Recurso r) throws Exception {
		String mensajeError = "";
		Set<Horario> horarios = new HashSet<>();
		horarioRepo.deleteByRecurso(r);
		for(Horario h : r.getHorarios()) {
			try {
				horarios.add(guardarHorarioRecurso(h,r));
			}catch(Exception e) {
				mensajeError += "Horario: "+h.getDia()+" de "+h.getDesde()+" a "+h.getHasta()+", "+e.getMessage()+". ";
			}
		}
		if(mensajeError.equals("")) {
			r.setHorarios(horarios);
			return r;
		}
		throw new Exception(mensajeError);
		
	}
	@Override
	public AsignacionRecursoTipoTurno guardarListaHorariosAsignacion(AsignacionRecursoTipoTurno a) throws Exception {
		String mensajeError = "";
		Set<Horario> horarios = new HashSet<>();
		horarioRepo.deleteByAsignacion(a);
		for(Horario h : a.getHorarios()) {
			try {
				horarios.add(guardarHorarioAsignacion(h,a));
			}catch(Exception e) {
				mensajeError += "Horario: "+h.getDia()+" de "+h.getDesde()+" a "+h.getHasta()+", "+e.getMessage()+". ";
			}
		}
		if(mensajeError.equals("")) {
			a.setHorarios(horarios);
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
	public List<Horario> horariosDeAsignacionParaFecha(AsignacionRecursoTipoTurno asig, Dias dia){
		List<Horario> horarios = horarioRepo.findByAsignacionAndDia(asig, dia);
		return horarios;
	}
	@Override
	public List<Horario> horariosDeRecursoParaFecha(Recurso recurso, Dias dia){
		List<Horario> horarios = horarioRepo.findByRecursoAndDia(recurso, dia);
		return horarios;
	}
	

}
