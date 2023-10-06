package com.sistema.examenes.nuevo.servicios;

import org.springframework.beans.factory.annotation.Autowired;

import com.sistema.examenes.anterior.modelo.Notificacion;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.repositorios.NotificacionRepository;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios_interfaces.NotificacionService;
import com.sistema.examenes.websocket.WebSocketService;

public class NotificacionServiceImpl implements NotificacionService{

    @Autowired
    private WebSocketService webSocket;
	
	@Autowired
	private NotificacionRepository notificacionRepo;
	
	@Override
	public void notificarNuevaReserva(Reserva r,Usuario u) {	
		Notificacion n = new Notificacion("¡Nueva Reserva!",
				"Fecha: "+r.getFecha()+
				"Hora: "+r.getHora()+
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
