package com.sistema.servicios_v2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sistema.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.anterior.modelo.Reserva;
import com.sistema.anterior.modelo.Reservante;
import com.sistema.anterior.repositorios.ReservaRepository;
import com.sistema.modelo.pagosMP.MPService;
import com.sistema.modelo.usuario.Usuario;
import com.sistema.notificaciones.NotificationsService;
import com.sistema.nuevo.servicios_interfaces.EstadoService;

@Service
public class ReservaServiceImplV2 implements ReservaServiceV2{
	
	@Autowired
	private ReservaRepository reservaRepo;
	@Autowired
	private ReservanteServiceV2 reservanteService;
	@Autowired
	private EstadoService estadoService;
	@Autowired
	private AsignacionServiceV2 asignacionService;
	@Autowired
	private MPService mpService;
	@Autowired
	private NotificationsService notificationsService;
	
	private final Validator validator;
	
    public ReservaServiceImplV2(Validator validator) {
        this.validator = validator;
    }
	
	@Override
	public Reserva eliminarReserva(Reserva r, Usuario u) throws Exception {
		return reservaRepo.save(estadoService.eliminarReserva(r));
	}

	@Override
	public void borrarReserva(Long idReserva) throws Exception {
		Reserva r = obtenerReservaPorId(idReserva);
		reservaRepo.delete(r);
	}

	@Override
	public Reserva editarReserva(Reserva r, Usuario u) throws Exception {
		Reserva guardada = obtenerReservaPorId(r.getId());
		if(r.getAsignacionTipoTurno().getRecurso().getUsuario().equals(u) && r.getAsignacionTipoTurno().getTipoTurno().getUsuario().equals(u)) {
			validar(r);
			guardada.editarReserva(r);
			return reservaRepo.save(estadoService.nuevoCambioEstadoReserva(guardada, guardada.obtenerUltimoCambioEstado().getEstadoNuevo()));
		}
		throw new Exception("Usuario no autorizado");
	}

	@Override
	public Reserva nuevaReserva(Reserva r) throws Exception {
		AsignacionRecursoTipoTurno a = asignacionService.obtenerAsignacionPorId(r.getAsignacionTipoTurno().getId());
		r.setAsignacionTipoTurno(a);
		Reservante reservante = reservanteService.nuevoReservante(r.getReservante());
		r.setReservante(reservante);
		r = estadoService.estadoReservaNueva(r);
		validar(r);
		if(r.getAsignacionTipoTurno().getSeniaCtvos()>0 && r.getReservante().getUsuario().getTokenMP()!=null) {
			r.setLinkPago(mpService.crearPagoSeña(r).getInitPoint());//metodo que crea el link de pago de la seña	
		}
		return reservaRepo.save(r);
	}

	@Override
	public List<Reserva> listarReservas(Usuario u) {
		List<Reserva> reservas = new ArrayList<>();
		for(AsignacionRecursoTipoTurno a : asignacionService.listarAsignaciones(u)) {
			List<Reserva> aux = reservaRepo.findByAsignacionTipoTurno(a);
			reservas.addAll(aux);
		}
		return reservas;
	}

	@Override
	public Reserva obtenerReservaPorId(Long id) throws Exception {
		Reserva r = reservaRepo.getById(id);
		if(r!=null) {
			return r;
		}
		throw new Exception("Reserva no encontrada");
	}
	
	
	
	private void validar(Reserva r) throws Exception{
		Errors errors = new BeanPropertyBindingResult(r, "reserva");
        ValidationUtils.invokeValidator(validator, r, errors);
        if (errors.hasErrors()) {
        	throw new Exception(errors.getFieldError().getDefaultMessage().toString());
        }
	}

	@Override
	public void actualizarReservas() throws Exception {
		// TODO Auto-generated method stub
		List<Reserva> reservas = reservaRepo.findAll();
		for(Reserva r : reservas) {
			if(r.isEsTurnoFijo() && r.getFechaHoraFin().isBefore(LocalDateTime.now())) {
				if(r.isEsTurnoFijo()) {
					r.setFechaHoraInicio(r.getFechaHoraInicio().plusWeeks(r.getCadaCuantasSemanas()));
					r.setFechaHoraFin(r.getFechaHoraFin().plusWeeks(r.getCadaCuantasSemanas()));					
				}else {
					r.setEstado(null);
				}
				editarReserva(r,r.getAsignacionTipoTurno().getRecurso().getUsuario());
			}
		}
		
	}

	@Override
	public void notificarReservas() throws Exception {
		// TODO Auto-generated method stub
		List<Reserva> reservas = reservaRepo.findAll();
		for(Reserva r : reservas) {
			Long minutosAnticipacion = r.getReservante().getUsuario().getNotificarMinutosAntes();
			if(!r.getFechaHoraInicio().isBefore(r.getFechaHoraInicio().minusMinutes(minutosAnticipacion)) &&
					r.getEstado().getId()==1) {
				notificationsService.notificarWppProximaReserva(r);
			}
		}
	}
	

}
