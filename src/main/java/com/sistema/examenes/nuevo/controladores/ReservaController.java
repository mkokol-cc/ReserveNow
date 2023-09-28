package com.sistema.examenes.nuevo.controladores;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios.ApiResponse;

@RestController
public class ReservaController {

	private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ReservaController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    
    
    @PostMapping("/reserva")
    public void nuevaReserva(@RequestBody Reserva objeto) {
        // Lógica para crear el objeto en el backend
        
        // Notificar a los clientes conectados sobre la creación del objeto
        messagingTemplate.convertAndSend("/topic/nuevoObjeto", objeto);
    }
    
    @PutMapping("/reserva")
    public void editarReserva(@RequestBody Reserva objeto) {
        // Lógica para crear el objeto en el backend
        
        // Notificar a los clientes conectados sobre la creación del objeto
        messagingTemplate.convertAndSend("/topic/nuevoObjeto", objeto);
    }
    
    @PostMapping("/{idUserPage}/reservas/{idAsignacion}/add")
	public ResponseEntity<?> registrarReserva(@PathVariable String idUserPage,@PathVariable Long idAsignacion,@Valid @RequestBody Reserva reservaStr) throws JsonProcessingException {
		try {
			Reservante r = reservaStr.getReservante();
			Usuario u = getUserByPageId(idUserPage);
			r.setUsuario(u);
			ApiResponse<Reserva> resp = reservaService.guardarReserva(reservaStr/*,u.getId()*/);
			if(resp.isSuccess()) {
		        // Notificar a los clientes conectados sobre la creación del objeto
		        messagingTemplate.convertAndSend("/topic/nuevoObjeto", objeto);
		        //----
				return ResponseEntity.ok(resp.getData());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
		}
	}
    
    
    
    
}
