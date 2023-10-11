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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios.ApiResponse;
import com.sistema.examenes.nuevo.servicios_interfaces.ReservanteService;
import com.sistema.examenes.repositorios.UsuarioRepository;

@RestController
@RequestMapping("/final")
@CrossOrigin("*")
public class ReservanteController {

	@Autowired
	private ReservanteService reservanteService;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	
	//"CRUD" RESERVANTE
	@GetMapping("/clientes")
	public ResponseEntity</*List<Reservante>*/?> listarClientes() throws JsonProcessingException {
		try {
			ApiResponse<List<Reservante>> resp = reservanteService.listarReservanteDeUsuario(getUser());
			if(resp.isSuccess()) {
				return new ResponseEntity<>(resp.getData(), HttpStatus.OK);	
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error inesperado: "+e.getMessage());
		}
		//return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
	}
	
	@PutMapping("/clientes/{idCliente}/edit")
	public ResponseEntity<?> editarCliente(@PathVariable Long idCliente,@Valid @RequestBody Reservante reservanteStr) throws JsonProcessingException {
		try {
			ApiResponse<Reservante> resp = reservanteService.editarReservante(reservanteStr, getUserId());
			if(resp.isSuccess()) {
				return new ResponseEntity<>(resp.getData(), HttpStatus.OK);	
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error inesperado: "+e.getMessage());
		}
		//return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
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
}
