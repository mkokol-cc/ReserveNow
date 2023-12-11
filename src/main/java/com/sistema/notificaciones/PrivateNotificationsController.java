package com.sistema.notificaciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sistema.anterior.modelo.TipoTurno;
import com.sistema.servicios.UsuarioService;

@RestController
@RequestMapping("/wpp")
@CrossOrigin("*")
public class PrivateNotificationsController {
	
	@Autowired
    private UsuarioService usuarioService;
	@Autowired
    private NotificationsService notificationsService;
	
    @PostMapping("/{mensaje}")
    private ResponseEntity<?> enviarWpp(@PathVariable("mensaje") String mensaje){
		try {
			notificationsService.enviarWppValidacionUsuario(usuarioService.obtenerUsuarioActual().getTelefono(), mensaje);
			return ResponseEntity.ok("Se envió correctamente el código.");
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
    }
}
