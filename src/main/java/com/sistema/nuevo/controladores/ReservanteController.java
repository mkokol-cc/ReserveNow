package com.sistema.nuevo.controladores;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sistema.anterior.modelo.Reservante;
import com.sistema.servicios.UsuarioService;
import com.sistema.servicios_v2.ReservanteServiceV2;

@RestController
@RequestMapping("/v1.1")
@CrossOrigin("*")
public class ReservanteController {

	@Autowired
	private ReservanteServiceV2 reservanteService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	
	@GetMapping("/cliente")
	public ResponseEntity<?> listarClientes() throws JsonProcessingException {
		try {
			List<Reservante> reservantes = reservanteService.listarReservantes(usuarioService.obtenerUsuarioActual());
			return ResponseEntity.ok(reservantes);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	
	@PutMapping("/cliente")
	public ResponseEntity<?> editarCliente(@RequestBody Reservante reservanteStr) throws JsonProcessingException {
		try {
			Reservante reservanteEditado = reservanteService.editarReservante(reservanteStr, usuarioService.obtenerUsuarioActual());
			return ResponseEntity.ok(reservanteEditado);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
