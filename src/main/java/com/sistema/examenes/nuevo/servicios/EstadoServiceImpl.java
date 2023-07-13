package com.sistema.examenes.nuevo.servicios;

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
	public ApiResponse<Reserva> nuevoCambioEstadoReserva(Reserva r, Estado anterior, Estado nuevo) {
		//obtener cambio de estado con la fecha mayor (la ultima)
		//de ese cambio de estado obtener el estado nuevo (q en este caso vendria a ser el anterior)
		//hacer con ese estado y el estado actual un nuevo cambio de estado
		//agregar cambio de estado a la reserva y devolver
		CambioEstado cambio = new CambioEstado();
		cambio.s
		CambioEstado cambio = cambioEstadoRepo.save(null)
		// TODO Auto-generated method stub
		return null;
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

}
