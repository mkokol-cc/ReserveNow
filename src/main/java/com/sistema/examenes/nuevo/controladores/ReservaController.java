package com.sistema.examenes.nuevo.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.servicios.UsuarioService;
import com.sistema.examenes.servicios_v2.ReservaServiceV2;
import com.sistema.examenes.websocket.WebSocketService;

@RestController
@RequestMapping("/v1.1")
@CrossOrigin("*")
public class ReservaController {

	@Autowired
	private UsuarioService usuarioService;
	/*
	@Autowired
	private ReservaService reservaService;
	*/
	@Autowired
	private ReservaServiceV2 reservaService;

    @Autowired
    private WebSocketService webSocket;
    
	@GetMapping("reserva")
	public ResponseEntity<?> listarReservas(){
		try {
			List<Reserva> reservas = reservaService.listarReservas(usuarioService.obtenerUsuarioActual());
			//System.out.println("La cantidad de reservas es "+reservas.size());
			return ResponseEntity.ok(reservas);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Excepcion:"+e.getMessage());
		}
	}
    
    
    //nueva Reserva
    @PostMapping("public/{idUserPage}/reserva")
    public ResponseEntity<?> guardarNuevaReservaPorGuest(@PathVariable String idUserPage,@RequestBody Reserva reservaStr) {
    	try {
			Reservante r = reservaStr.getReservante();
			Usuario u = usuarioService.obtenerUsuarioActual();
			r.setUsuario(u);
			Reserva reservaGuardada = reservaService.nuevaReserva(reservaStr);
			return ResponseEntity.ok(reservaGuardada);
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
    }
    
    //editarReserva ADMINISTRADOR
    /*
    @PutMapping("public/reserva")
    public ResponseEntity<?> editarReservaPorGuest(@RequestBody Reserva reservaStr) {
    	try {
    		Reserva reservaEditada = reservaService.editarReserva(reservaStr, (Long) null);
			return ResponseEntity.ok(reservaEditada);
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
    }
    */
    
    //editarReserva GUEST
    @PutMapping("reserva")
    public ResponseEntity<?> editarReservaPorAdministrador(@RequestBody Reserva reservaStr) {
    	try {
    		Reserva reservaEditada = reservaService.editarReserva(reservaStr, usuarioService.obtenerUsuarioActual());
        	return ResponseEntity.ok(reservaEditada);
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
    }
    
    @DeleteMapping("reserva/{id}")
    public ResponseEntity<?> eliminarReserva(@PathVariable Long id) {
    	try {
    		Reserva reservaAEliminar = reservaService.obtenerReservaPorId(id); 
        	return ResponseEntity.ok(reservaService.eliminarReserva(reservaAEliminar, usuarioService.obtenerUsuarioActual()));
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
    }
    
    @DeleteMapping("reserva/borrar/{id}")
    public ResponseEntity<?> borrarReserva(@PathVariable Long id) {
    	try {
    		Reserva reservaAEliminar = reservaService.obtenerReservaPorId(id); 
        	return ResponseEntity.ok(reservaService.eliminarReserva(reservaAEliminar, usuarioService.obtenerUsuarioActual()));
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
    }
    
    /*
    
    
    @GetMapping("reserva/{idRecurso}/{fecha}/{desde}/{hasta}")
    public ResponseEntity<?> obtenerReservasInvolucradasEnRango(
    		@PathVariable Long idRecurso,@PathVariable String fecha,
    		@PathVariable String desde,@PathVariable String hasta){
    	try {
    		DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    		DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
    		if(usuarioService.getIdUsuarioActual() != 0) {
    			List<Reserva> reservasInvolucradas = reservaService.obtenerReservasInvolucradasEnRango(idRecurso,LocalDate.parse(fecha, formatterDate),
    					LocalTime.parse(desde, formatterTime),LocalTime.parse(hasta, formatterTime));
    			return ResponseEntity.ok(reservasInvolucradas);
    		}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Acceso invalido. Por favor vuelve a iniciar sesión.");
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
    	
    }
    */
    /*
    @PostMapping("reserva/estado/{idEstado}")
    public ResponseEntity<?> cambiarEstadoDeReservas(@PathVariable Long idEstado,@RequestBody List<Long> idReservas){
    	try {
    		if(usuarioService.getIdUsuarioActual() != 0) {
    			List<Reserva> reservaEditada = reservaService.cambiarEstadosDeListaDeIdsReserva(idEstado, idReservas);
    			return ResponseEntity.ok(reservaEditada);
    		}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Acceso invalido. Por favor vuelve a iniciar sesión.");
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
		}
    }
    */
    /*
    @PostMapping("notificacion/{idUsuario}")
    public void enviarNotificacion(@PathVariable long idUsuario) {
    	*//*
    	Usuario u = usuarioRepo.getById(idUsuario);
    	System.out.println("ENTRE");
    	if(u != null) {
    		notificarFrontend(u);
    	}else {
    		System.out.println("ERROR AL OBTENER EL USUARIO");
    	}*//*
    	//return null;
    	//messagingTemplate.convertAndSendToUser(String.valueOf(u.getId()), "/topic/notificaciones", new Notificacion("Nueva Reserva!","Se ha guardado un nuevo registro.",u));
    }
    */
    
    /*
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
	*/
}
