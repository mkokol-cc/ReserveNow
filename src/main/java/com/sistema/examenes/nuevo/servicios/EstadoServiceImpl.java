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
	public ApiResponse<Reserva> nuevoCambioEstadoReserva(Reserva r) {
		ApiResponse<Estado> buscarEstadoPorNombreRes = getEstadoByNombre(r.getEstado().getNombre());
		if(buscarEstadoPorNombreRes.isSuccess()) {
			//obtener cambio de estado con la fecha mayor (la ultima)
			List<CambioEstado> cambiosDeEstado = r.getCambioEstado();
			CambioEstado cambioEstadoMasReciente = Collections.max(cambiosDeEstado, Comparator.comparing(CambioEstado::getFecha));
			//de ese cambio de estado obtener el estado nuevo (q en este caso vendria a ser el anterior)
			//hacer con ese estado y el estado actual un nuevo cambio de estado
			CambioEstado cambio = new CambioEstado();
			if(buscarEstadoPorNombreRes.getData() != cambioEstadoMasReciente.getEstadoNuevo()) {
				cambio.setEstadoAnterior(cambioEstadoMasReciente.getEstadoNuevo());
				cambio.setEstadoNuevo(buscarEstadoPorNombreRes.getData());
				cambio.setFecha(LocalDate.now());
				cambio.setHora(LocalTime.now());//nose si guaardar el cambio de estado antes de setearsela a la reserva
				r.setEstado(buscarEstadoPorNombreRes.getData());
				//agregar cambio de estado a la reserva y devolver
				ApiResponse<CambioEstado> reponseGuardarCambioEstado = guardarCambioEstado(cambio);
				if(reponseGuardarCambioEstado.isSuccess()) {
					cambiosDeEstado.add(reponseGuardarCambioEstado.getData());
					r.setCambioEstado(cambiosDeEstado);
					return new ApiResponse<>(true,"",r);
				}else {
					return new ApiResponse<>(false,reponseGuardarCambioEstado.getMessage(),null);
				}
			}else {
				return new ApiResponse<>(false,"Error, agregar un cambio de estado con dos estados iguales",null);
			}
		}
		return new ApiResponse<>(false,buscarEstadoPorNombreRes.getMessage(),null);
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
	
	private ApiResponse<CambioEstado> guardarCambioEstado(CambioEstado c){
		try {
			CambioEstado cambioGuardado = cambioEstadoRepo.save(c);
			if(cambioGuardado!=null) {
				return new ApiResponse<>(true,"",cambioGuardado);
			}
			return new ApiResponse<>(false,"No se pudo guardar el cambio de estado",null);
		}catch(Exception e) {
			return new ApiResponse<>(false,"Error al cambiar de estado. "+e.getMessage(),null);
		}
		
		
	}

}
