package com.sistema.nuevo.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.anterior.modelo.Notificacion;
import com.sistema.anterior.modelo.Reserva;
import com.sistema.anterior.repositorios.NotificacionRepository;
import com.sistema.modelo.usuario.Usuario;
import com.sistema.nuevo.servicios_interfaces.NotificacionService;
import com.sistema.websocket.WebSocketService;

@Service
public class NotificacionServiceImpl implements NotificacionService{

    @Autowired
    private WebSocketService webSocket;
	
	@Autowired
	private NotificacionRepository notificacionRepo;
	
	@Override
	public void notificarNuevaReserva(Reserva r,Usuario u) {	
		Notificacion n = new Notificacion("¡Nueva Reserva!",
				"Fecha: "+r.getFechaHoraInicio().toLocalDate()+
				"Hora: "+r.getFechaHoraInicio().toLocalTime()+
				"Recurso: "+r.getAsignacionTipoTurno().getRecurso().getNombre()+
				"Tipo de Turno: "+r.getAsignacionTipoTurno().getTipoTurno().getNombre()+
				"Reservante: "+r.getReservante().getNombre()+" "+r.getReservante().getApellido()+" ("+r.getReservante().getTelefono()+")"
				,u);
		enviarNotificacionWeb(u,notificacionRepo.save(n));
	}
	
	private void enviarNotificacionWeb(Usuario u,Notificacion n) {
		String entityTopic = String.valueOf(u.getId());
		webSocket.sendNotificacion(entityTopic, n);
	}

}
