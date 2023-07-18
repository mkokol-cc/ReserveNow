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
					if(!he.getDesde().isAfter(hora) /*&& !he.getDesde().isBefore(horaMasDuracion)*/) {
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
	}

	@Override
	public ApiResponse<HorarioEspecial> comprobarHorarioEspecialAsignacion(LocalTime hora, LocalDate fecha,
			AsignacionRecursoTipoTurno asignacion) {
		List<HorarioEspecial> horariosEspeciales = horarioEspRepo.findByFechaAndAsignacion(fecha,asignacion);
		//si hay horarios especiales de la asignacion para ese dia:
		if(horariosEspeciales.size()>0) {
			HorarioEspecial horarioEspCompatible = null;
			for(HorarioEspecial he : horariosEspeciales) {
				if(he.isCerrado()) {
					return new ApiResponse<>(false,"Fecha Cerrada: "+he.getMotivo(),he);
				}else{
					LocalTime horaMasDuracion = hora.plusMinutes((long)asignacion.getDuracionEnMinutos());
					if(!he.getDesde().isAfter(hora) && !he.getDesde().isBefore(horaMasDuracion)) {
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
	}

	@Override
	public ApiResponse<HorarioEspecial> guardarHorarioEspecial(HorarioEspecial h) {
		try {
			//comprobar si el horario es correcto
			//- si el la hora desde es < hasta
			//- si la duracion del asignacion entre en el horario desde y hasta
			if(h.isCerrado() || !h.getDesde().isAfter(h.getHasta())) {
				HorarioEspecial horario = horarioEspRepo.save(h);
				return new ApiResponse<>(true,"",horario);
			}
			return new ApiResponse<>(false,"Horario Especial: "+h.getFecha()+" de "+h.getDesde()+" a "+h.getHasta()+" invalido.",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}
	
	
	

	public ApiResponse<HorarioEspecial> guardarHorarioEspecialRecurso(HorarioEspecial h, Recurso r) {
		try {
			//comprobar si el horario es correcto
			//- si el la hora desde es < hasta
			//- si la duracion del asignacion entre en el horario desde y hasta
			if(h.isCerrado() || !h.getDesde().isAfter(h.getHasta())) {
				h.setRecurso(r);
				HorarioEspecial horario = horarioEspRepo.save(h);
				return new ApiResponse<>(true,"",horario);
			}
			return new ApiResponse<>(false,"Horario Especial: "+h.getFecha()+" de "+h.getDesde()+" a "+h.getHasta()+" invalido.",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}

	public ApiResponse<HorarioEspecial> guardarHorarioEspecialAsignacion(HorarioEspecial h, AsignacionRecursoTipoTurno a) {
		try {
			//comprobar si el horario es correcto
			//- si el la hora desde es < hasta
			//- si la duracion del asignacion entre en el horario desde y hasta
			//LocalTime duracion = h.getDesde().plusMinutes((long)a.getDuracionEnMinutos());
			if(h.isCerrado() || !h.getDesde().plusMinutes((long)a.getDuracionEnMinutos()).isAfter(h.getHasta())) {
				h.setAsignacion(a);
				HorarioEspecial horario = horarioEspRepo.save(h);
				return new ApiResponse<>(true,"",horario);
			}
			return new ApiResponse<>(false,"Horario Especial: "+h.getFecha()+" de "+h.getDesde()+" a "+h.getHasta()+" invalido.",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}
	
	

	@Override
	public ApiResponse<Recurso> guardarListaHorarioEspecialRecurso(Recurso recurso) {
		try {
			Set<HorarioEspecial> horarios = new HashSet<>();
			horarioEspRepo.deleteByRecurso(recurso);
			for(HorarioEspecial h : recurso.getHorariosEspeciales()) {
				ApiResponse<HorarioEspecial> resp = guardarHorarioEspecialRecurso(h,recurso);
				if(!resp.isSuccess()) {
					return new ApiResponse<>(false,"Error al guardar los Horarios: "+resp.getMessage(),null);
				}
				horarios.add(resp.getData());
			}
			recurso.setHorariosEspeciales(horarios);
			return new ApiResponse<>(true,"",recurso);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}

	@Override
	public ApiResponse<AsignacionRecursoTipoTurno> guardarListaHorarioEspecialAsignacion(AsignacionRecursoTipoTurno a) {
		try {
			Set<HorarioEspecial> horarios = new HashSet<>();
			horarioEspRepo.deleteByAsignacion(a);
			for(HorarioEspecial h : a.getHorariosEspeciales()) {
				ApiResponse<HorarioEspecial> resp = guardarHorarioEspecialAsignacion(h,a);
				if(!resp.isSuccess()) {
					return new ApiResponse<>(false,"Error al guardar los Horarios: "+resp.getMessage(),null);
				}
				horarios.add(resp.getData());
			}
			a.setHorariosEspeciales(horarios);
			return new ApiResponse<>(true,"",a);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}
	}

}
