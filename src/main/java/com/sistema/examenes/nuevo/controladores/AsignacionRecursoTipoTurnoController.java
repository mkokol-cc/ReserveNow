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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.nuevo.servicios.ApiResponse;
import com.sistema.examenes.nuevo.servicios_interfaces.AsignacionRecursoTipoTurnoService;
import com.sistema.examenes.repositorios.UsuarioRepository;

@RestController
@RequestMapping("/final")
@CrossOrigin("*")
public class AsignacionRecursoTipoTurnoController {

	@Autowired
	private AsignacionRecursoTipoTurnoService asignacionService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	//CRUD ASIGNACION
	@PostMapping("/asignacion/{idTipoTurno}/{idRecurso}")
	public ResponseEntity<?> guardarAsignacion(@PathVariable Long idTipoTurno,@PathVariable Long idRecurso) throws JsonProcessingException {
		ApiResponse<AsignacionRecursoTipoTurno> resp = asignacionService.guardarAsignacion(idTipoTurno, idRecurso, getUserId());
		if(resp.isSuccess()) {
			return ResponseEntity.ok(resp.getData());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
	}
	
	@GetMapping("/asignacion")
	public ResponseEntity<?> listarAsignaciones(){
		ApiResponse<List<AsignacionRecursoTipoTurno>> resp = asignacionService.listarAsignacionPorUsuario(getUserId());
		if(resp.isSuccess()) {
			return ResponseEntity.ok(resp.getData());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
	}
	
	@PutMapping("/asignacion/edit")
	public ResponseEntity<?> editarAsignacion(@Valid @RequestBody AsignacionRecursoTipoTurno asigStr) throws JsonProcessingException {
		ApiResponse<AsignacionRecursoTipoTurno> resp = asignacionService.editarAsignacion(asigStr, getUserId());
		if(resp.isSuccess()) {
			return ResponseEntity.ok(resp.getData());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
	}
	
	public Long getUserId() {
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
