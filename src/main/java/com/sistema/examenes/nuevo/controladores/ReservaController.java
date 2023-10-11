package com.sistema.examenes.nuevo.controladores;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.examenes.anterior.modelo.Notificacion;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.dto.TurnoDTO;
import com.sistema.examenes.nuevo.servicios.ApiResponse;
import com.sistema.examenes.nuevo.servicios_interfaces.ReservaService;
import com.sistema.examenes.repositorios.UsuarioRepository;
import com.sistema.examenes.websocket.WebSocketService;

@RestController
@RequestMapping("/v1.0")
@CrossOrigin("*")
public class ReservaController {

	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private ReservaService reservaService;

    @Autowired
    private WebSocketService webSocket;
    
    
    
    @GetMapping("public/horarios/{idAsignacion}/{fecha}")
    public ResponseEntity<?> getTurnosDeAsignacionParaFecha(@PathVariable Long idAsignacion,@PathVariable String fecha) {
    	try {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    		LocalDate fechaLocalDate = LocalDate.parse(fecha, formatter);
			ApiResponse<List<TurnoDTO>> resp = reservaService.crearTurnos(idAsignacion, fechaLocalDate);
			if(resp.isSuccess()) {
				return ResponseEntity.ok(resp.getData());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
		}
    }
    
    
    //nueva Reserva
    @PostMapping("public/{idUserPage}/reserva")
    public ResponseEntity<?> guardarNuevaReservaPorGuest(@PathVariable String idUserPage,@RequestBody Reserva reservaStr) {
    	try {
			Reservante r = reservaStr.getReservante();
			Usuario u = getUserByPageId(idUserPage);
			r.setUsuario(u);
			ApiResponse<Reserva> resp = reservaService.guardarReserva(reservaStr,u.getId());
			if(resp.isSuccess()) {
				return ResponseEntity.ok(resp.getData());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
		}
    }
    
    //editarReserva ADMINISTRADOR
    @PutMapping("public/reserva")
    public ResponseEntity<?> editarReservaPorGuest(@RequestBody Reserva reservaStr) {
    	try {
			ApiResponse<Reserva> resp = reservaService.editarReserva(reservaStr, (Long) null);
			if(resp.isSuccess()) {
				return ResponseEntity.ok(resp.getData());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
		}
    }
    
    //editarReserva GUEST
    @PutMapping("reserva")
    public ResponseEntity<?> editarReservaPorAdministrador(@RequestBody Reserva reservaStr) {
    	try {
    		if(getUserId() != 0) {
    			ApiResponse<Reserva> resp = reservaService.editarReserva(reservaStr, getUserId());
    			if(resp.isSuccess()) {
    				return ResponseEntity.ok(resp.getData());
    			}
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
    		}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Acceso invalido. Por favor vuelve a iniciar sesión.");
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
		}
    }
    
    
    
    
    
    @GetMapping("reserva/{idRecurso}/{fecha}/{desde}/{hasta}")
    public ResponseEntity<?> obtenerReservasInvolucradasEnRango(
    		@PathVariable Long idRecurso,@PathVariable String fecha,
    		@PathVariable String desde,@PathVariable String hasta){
    	try {
    		DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    		DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
    		if(getUserId() != 0) {
    			ApiResponse<List<Reserva>> resp = reservaService.obtenerReservasInvolucradasEnRango(idRecurso,LocalDate.parse(fecha, formatterDate),
    					LocalTime.parse(desde, formatterTime),LocalTime.parse(hasta, formatterTime));
    			if(resp.isSuccess()) {
    				return ResponseEntity.ok(resp.getData());
    			}
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
    		}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Acceso invalido. Por favor vuelve a iniciar sesión.");
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
		}
    	
    }
    
    //
    @PostMapping("reserva/estado/{idEstado}")
    public ResponseEntity<?> cambiarEstadoDeReservas(@PathVariable Long idEstado,@RequestBody List<Long> idReservas){
    	try {
    		if(getUserId() != 0) {
    			ApiResponse<List<Reserva>> resp = reservaService.cambiarEstadosDeListaDeIsReserva(idEstado, idReservas);
    			if(resp.isSuccess()) {
    				return ResponseEntity.ok(resp.getData());
    			}
    			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
    		}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Acceso invalido. Por favor vuelve a iniciar sesión.");
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
		}
    }
    
    
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
	private Usuario getUserByPageId(String url){
		try {
			Usuario u = usuarioRepo.findByDbUrl(url);
			return u;
		}catch(Exception e) {
			return null;
		}
	}
	private Long getUserId() {
        // Obtener la autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Obtener los detalles del usuario autenticado
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            // Aquí puedes realizar las operaciones necesarias con el usuario autenticado
            try {
            	return usuarioRepo.findByEmail(username).getId();
            }catch(Exception e) {
            	return (long) 0;
            }
        }
        // Si no se encuentra un usuario autenticado, puedes manejarlo según tus necesidades
        return (long) 0;
    }
    
}
