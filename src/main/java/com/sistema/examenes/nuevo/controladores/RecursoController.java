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
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.repositorios.AsignacionRecursoTipoTurnoRepository;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios.ApiResponse;
import com.sistema.examenes.nuevo.servicios_interfaces.RecursoService;
import com.sistema.examenes.repositorios.UsuarioRepository;

@RestController
@RequestMapping("/final")
@CrossOrigin("*")
public class RecursoController {
	
	@Autowired
	private RecursoService recursoService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;

	//CRUD RECURSO
	@PostMapping("/recurso/add")
	public ResponseEntity<?> guardarRecurso(@Valid @RequestBody Recurso recursoStr) throws JsonProcessingException {
		
		try {
			recursoStr.setUsuario(usuarioRepo.getById(getUserId()));
			ApiResponse<Recurso> resp = recursoService.guardarRecurso(recursoStr);
			if(resp.isSuccess()) {
				return new ResponseEntity<>("Se guardó correctamente el Recurso.", HttpStatus.CREATED);	
			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		/*
		ApiResponse<Recurso> resp = recursoService.guardarRecurso(recursoStr);
		
		
		
		if(resp.isSuccess()) {
			return new ResponseEntity<>("Se guardó correctamente el Recurso.", HttpStatus.CREATED);	
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
		*/
	}
	
	@PutMapping("/recurso/edit")
	public ResponseEntity<?> editarRecurso(/*@Valid */@RequestBody Recurso recursoStr) throws JsonProcessingException {
		try {
			recursoStr.setUsuario(getUser());
			ApiResponse<Recurso> resp = recursoService.editarRecurso(recursoStr);
			if(resp.isSuccess()) {
				return new ResponseEntity<>("Se editó correctamente el Recurso.", HttpStatus.OK);	
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error inesperado: "+e.getMessage());
		}
		//return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
	}
	
	@GetMapping("/recurso")
	public ResponseEntity</*List<Recurso>*/?> listarRecursos(){
		ApiResponse<List<Recurso>> resp = recursoService.listarRecurso(usuarioRepo.getById(getUserId()));
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
	
	public Usuario getUser() {
        // Obtener la autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Obtener los detalles del usuario autenticado
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            // Aquí puedes realizar las operaciones necesarias con el usuario autenticado
            try {
            	return usuarioRepo.findByEmail(username);
            }catch(Exception e) {
            	return null;
            }
        }

        // Si no se encuentra un usuario autenticado, puedes manejarlo según tus necesidades
        return null;
    }
	
	
	//SI EL RECURSO SE DESHABILITA ENTONCES TAMBIEN LO HARAN TODAS LAS ASIGNACIONES QUE TIENE ASOCIADAS 
	//(LA LOGICA DE ESTO LA ALMACENE EL EL ASIGNACION SERVICE)
}
