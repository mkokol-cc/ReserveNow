package com.sistema.nuevo.servicios;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.anterior.modelo.CambioEstado;
import com.sistema.anterior.modelo.Estado;
import com.sistema.anterior.modelo.Reserva;
import com.sistema.anterior.repositorios.CambioEstadoRepository;
import com.sistema.anterior.repositorios.EstadoRepository;
import com.sistema.notificaciones.NotificationsService;
import com.sistema.nuevo.servicios_interfaces.EstadoService;

@Service
public class EstadoServiceImpl implements EstadoService{

	@Autowired
	private EstadoRepository estadoRepo;
	
	@Autowired
	private CambioEstadoRepository cambioEstadoRepo;
	
	@Autowired
	private NotificationsService notificationsService;
	
	@Override
	public Reserva nuevoCambioEstadoReserva(Reserva r, Estado anterior) {
		if(!anterior.isEsEstadoFinal() && !anterior.equals(r.getEstado())) {
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
			//NOTIFICAR AL DUEÑO DE LA RESERVA
			try {
				notificationsService.notificarWppCambioEstadoReserva(r);
			}catch(Exception e) {
				System.out.println("Hubo un error al notificar cambio de estado via wpp");
			}
		}
		return r;
	}
	

	@Override
	public Reserva estadoReservaNueva(Reserva r) {
		if(r.getAsignacionTipoTurno().getSeniaCtvos()>0) {
			r.setEstado(getEstadoById(1L));// id del estado PTE PAGO
			return r;
		}
		r.setEstado(getEstadoById(2L));// id del estado VALIDO
		return r;
	}
	
	@Override 
	public Estado getEstadoByNombre(String nombre) throws Exception{
		Estado e = estadoRepo.findByNombre(nombre);
		if(e!=null) {
			return e;
		}
		throw new Exception("No se encontro un estado con el nombre "+nombre+".");
	}


	@Override
	public List<Estado> listarEstados() {
		return estadoRepo.findAll();
	}
	
	
	@Override
	public Estado getEstadoById(Long idEstado){
		return estadoRepo.getById(idEstado);
	}


	@Override
	public Reserva eliminarReserva(Reserva reserva) throws Exception {
		//seteamos el estado id ... "Eliminado"
		Estado anterior = reserva.getEstado();
		reserva.setEstado(getEstadoById(4L));
		return nuevoCambioEstadoReserva(reserva,anterior);
	}


	@Override
	public Reserva registrarPagoSenia(Reserva reserva) throws Exception {
		Estado estadoSeniaPagada = getEstadoById(2L);//ID del estado Valido
		Estado anterior = reserva.getEstado();
		reserva.setEstado(estadoSeniaPagada);
		return nuevoCambioEstadoReserva(reserva,anterior);
	}


}
