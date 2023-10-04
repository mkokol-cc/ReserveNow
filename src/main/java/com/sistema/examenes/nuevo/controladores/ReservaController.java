package com.sistema.examenes.nuevo.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.examenes.anterior.modelo.Notificacion;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios_interfaces.ReservaService;
import com.sistema.examenes.repositorios.UsuarioRepository;
import com.sistema.examenes.websocket.WebSocketService;

@RestController
@RequestMapping("/v1.0/public")
@CrossOrigin("*")
public class ReservaController {

	@Autowired
	private UsuarioRepository usuarioRepo;
	@Autowired
	private ReservaService reservaService;

    @Autowired
    private WebSocketService webSocket;
    
    /*
    @PostMapping("/{idUserPage}/reservas/{idAsignacion}/add")
	public ResponseEntity<?> registrarReserva(@PathVariable String idUserPage,@PathVariable Long idAsignacion,@Valid @RequestBody Reserva reservaStr) throws JsonProcessingException {
		try {
			Reservante r = reservaStr.getReservante();
			Usuario u = getUserByPageId(idUserPage);
			r.setUsuario(u);
			ApiResponse<Reserva> resp = reservaService.guardarReserva(reservaStr);
			if(resp.isSuccess()) {
		        // Notificar a los clientes conectados sobre la creación del objeto
				messagingTemplate.convertAndSendToUser(String.valueOf(u.getId()), "/topic/notificaciones", new Notificacion("Nueva Reserva!","Se ha guardado un nuevo registro.",u));
		        //----
				return ResponseEntity.ok(resp.getData());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
		}
	}*/
    
    /*
    @MessageMapping("/enviar")
    @SendTo("/topic/2")
    public Notificacion enviar() {
    	Usuario u = usuarioRepo.getById((long)2);
    	return new Notificacion("Titular","HOLA",u);
    }
    */
    
    
    
    
    
    
    //nueva Reserva
    
    //editarReserva ADMINISTRADOR
    
    //editarReserva GUEST
    
    
    
    
    
    @PostMapping("notificacion/{idUsuario}")
    public void enviarNotificacion(@PathVariable long idUsuario) {
    	Usuario u = usuarioRepo.getById(idUsuario);
    	System.out.println("ENTRE");
    	if(u != null) {
    		notificarFrontend(u);
    	}else {
    		System.out.println("ERROR AL OBTENER EL USUARIO");
    	}
    	//messagingTemplate.convertAndSendToUser(String.valueOf(u.getId()), "/topic/notificaciones", new Notificacion("Nueva Reserva!","Se ha guardado un nuevo registro.",u));
    }
    
    
    private void notificarFrontend(Usuario u) {
    	
    	Notificacion n = new Notificacion("Titular","HOLA",u);
    	
    	
    	String entityTopic = String.valueOf(u.getId());
    	
    	
    	
    	if(entityTopic.isBlank() || entityTopic.isEmpty()) {
    		System.out.println("ERROR AL OBTENER EL TOPICO");
    		return;
    	}
    	
    	
    	//webSocket.sendMessage(entityTopic);
    	webSocket.sendNotificacion(entityTopic, n);
    }
    
    
    
    //public 
    
    
	private Usuario getUserByPageId(String url){
		try {
			Usuario u = usuarioRepo.findByDbUrl(url);
			return u;
		}catch(Exception e) {
			return null;
		}
	}
    
    
}
