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
		}
		return r;
	}
	

	@Override
	public Reserva estadoReservaNueva(Reserva r) {
		if(r.getAsignacionTipoTurno().getSeniaCtvos()>0) {
			r.setEstado(estadoRepo.getById((long)1));// id del estado RESERVADO - CON SEÑA
			return r;
		}
		r.setEstado(estadoRepo.getById((long)2));// id del estado RESERVADO - SIN SEÑA
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
		reserva.setEstado(getEstadoById(6L));
		return nuevoCambioEstadoReserva(reserva,anterior);
	}


}
