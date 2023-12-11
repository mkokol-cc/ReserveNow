package com.sistema.nuevo.controladores;

import java.util.List;

import javax.validation.Valid;

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
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sistema.anterior.modelo.TipoTurno;
import com.sistema.servicios.UsuarioService;
import com.sistema.servicios_v2.TipoTurnoServiceV2;


@RestController
@RequestMapping("/v1.1")
@CrossOrigin("*")
public class TipoTurnoController {

	@Autowired
	private TipoTurnoServiceV2 tipoTurnoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	//CRUD TIPO TURNO
	@PostMapping("/tipo-turno")
	public ResponseEntity<?> guardarTipoTurno(@RequestBody TipoTurno tipoTurnoStr) throws JsonProcessingException {
		try {
			tipoTurnoStr.setUsuario(usuarioService.obtenerUsuarioActual());
			TipoTurno tipoTurnoGuardado = tipoTurnoService.nuevoTipoTurno(tipoTurnoStr);
			return ResponseEntity.ok(tipoTurnoGuardado);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PutMapping("/tipo-turno")
	public ResponseEntity<?> editarTipoTurno(@Valid @RequestBody TipoTurno tipoTurnoStr) throws JsonProcessingException {
		try {
			TipoTurno tipoTurnoEditado = tipoTurnoService.editarTipoTurno(tipoTurnoStr,usuarioService.obtenerUsuarioActual());
			return ResponseEntity.ok(tipoTurnoEditado);	
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@GetMapping("/tipo-turno")
	public ResponseEntity<?> listarTipoTurno(){
		try {
			List<TipoTurno> listaTiposTurnos = tipoTurnoService.listarTipoTurno(usuarioService.obtenerUsuarioActual());
			return ResponseEntity.ok(listaTiposTurnos);	
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@DeleteMapping("/tipo-turno/{id}")
	public ResponseEntity<?> eliminarTipoTurno(@PathVariable Long id){
		try {
			TipoTurno t = tipoTurnoService.obtenerTipoTurnoPorId(id);
			tipoTurnoService.eliminarTipoTurno(id, usuarioService.obtenerUsuarioActual());
			return ResponseEntity.ok("Se elimino correctamente el tipo de turno "+t.getNombre()+" y se cancelaron sus reservas.");
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@DeleteMapping("/tipo-turno/borrar/{id}")
	public ResponseEntity<?> borrarTipoTurno(@PathVariable Long id){
		try {
			TipoTurno t = tipoTurnoService.obtenerTipoTurnoPorId(id);
			tipoTurnoService.borrarTipoTurno(id);
			return ResponseEntity.ok("Se elimino correctamente el tipo de turno "+t.getNombre()+" y sus reservas.");
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
}
