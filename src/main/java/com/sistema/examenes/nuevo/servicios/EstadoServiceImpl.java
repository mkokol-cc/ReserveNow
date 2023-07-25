package com.sistema.examenes.nuevo.servicios;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.examenes.anterior.modelo.CambioEstado;
import com.sistema.examenes.anterior.modelo.Estado;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.repositorios.CambioEstadoRepository;
import com.sistema.examenes.anterior.repositorios.EstadoRepository;
import com.sistema.examenes.nuevo.servicios_interfaces.EstadoService;

@Service
public class EstadoServiceImpl implements EstadoService{

	@Autowired
	private EstadoRepository estadoRepo;
	
	@Autowired
	private CambioEstadoRepository cambioEstadoRepo;
	
	@Override
	public ApiResponse<Reserva> nuevoCambioEstadoReserva(Reserva r, Estado anterior) {
		
		try {
			CambioEstado c = new CambioEstado();
			c.setEstadoAnterior(anterior);
			c.setEstadoNuevo(r.getEstado());
			c.setFecha(LocalDate.now());
			c.setHora(LocalTime.now());
			c.setReserva(r);
			CambioEstado guardado = cambioEstadoRepo.save(c);
			List<CambioEstado> cambios = r.getCambioEstado();
			cambios.add(guardado);
			r.setCambioEstado(cambios);
			return new ApiResponse<>(true,"",r);
		}catch(Exception e) {
			return new ApiResponse<>(false,e.getMessage(),null);
		}	
	}
	

	@Override
	public ApiResponse<Reserva> estadoReservaNueva(Reserva r) {
		if(r.getAsignacionTipoTurno().getSeniaCtvos()>0) {
			r.setEstado(estadoRepo.getById((long)1));// id del estado RESERVADO - CON SEÑA
			return new ApiResponse<>(true,"",r);
		}
		r.setEstado(estadoRepo.getById((long)2));// id del estado RESERVADO - SIN SEÑA
		return new ApiResponse<>(true,"",r);
	}
	
	@Override 
	public ApiResponse<Estado> getEstadoByNombre(String nombre){
		try {
			Estado e = estadoRepo.findByNombre(nombre);
			if(e==null) {
				return new ApiResponse<>(false,"No se encontro ningun estado con el nombre "+nombre,null);
			}
			return new ApiResponse<>(true,"",e);
		}catch(Exception e) {
			return new ApiResponse<>(false,"Error al obtener un estado con ese nombre",null);
		}
		
	}


	@Override
	public ApiResponse<List<Estado>> listarEstados() {
		try {
			List<Estado> e = estadoRepo.findAll();
			if(e.isEmpty()) {
				return new ApiResponse<>(false,"No se encontro ningun estado",null);
			}
			return new ApiResponse<>(true,"",e);
		}catch(Exception e) {
			return new ApiResponse<>(false,"Error al obtener los estados",null);
		}
	}


}
