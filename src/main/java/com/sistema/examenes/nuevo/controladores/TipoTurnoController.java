package com.sistema.examenes.nuevo.controladores;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios.ApiResponse;
import com.sistema.examenes.nuevo.servicios_interfaces.TipoTurnoService;
import com.sistema.examenes.repositorios.UsuarioRepository;


@RestController
@RequestMapping("/final")
@CrossOrigin("*")
public class TipoTurnoController {

	@Autowired
	private TipoTurnoService tipoTurnoService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	//CRUD TIPO TURNO
	@PostMapping("/tipo-turno/add")
	public ResponseEntity</*TipoTurno*/?> guardarTipoTurno(@Valid @RequestBody TipoTurno tipoTurnoStr) throws JsonProcessingException {
		ApiResponse<TipoTurno> resp = tipoTurnoService.guardarTipoTurno(tipoTurnoStr);
		if(resp.isSuccess()) {
			return new ResponseEntity<>("Se guardó correctamente el Tipo de Turno.", HttpStatus.CREATED);	
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
	}

	@PutMapping("/tipo-turno/edit")
	public ResponseEntity<?> editarTipoTurno(@Valid @RequestBody TipoTurno tipoTurnoStr) throws JsonProcessingException {
		/*ApiResponse<TipoTurno> resp = tipoTurnoService.editarTipoTurno(tipoTurnoStr,getUserId());
		if(resp.isSuccess()) {
			return new ResponseEntity<>("Se editó correctamente el Tipo de Turno.", HttpStatus.CREATED);	
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());*/
		return null;
	}
	
	@GetMapping("/tipo-turno")
	public ResponseEntity<?/*List<TipoTurno>*/> listarTipoTurno(){
		ApiResponse<List<TipoTurno>> resp = tipoTurnoService.listarTipoTurnoDeUsuario(usuarioRepo.getById(getUserId()));
		if(resp.isSuccess()) {
			return ResponseEntity.ok(resp.getData());
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
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
