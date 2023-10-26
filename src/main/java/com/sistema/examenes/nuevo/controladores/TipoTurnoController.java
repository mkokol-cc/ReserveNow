package com.sistema.examenes.nuevo.controladores;

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
import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.nuevo.servicios_interfaces.TipoTurnoService;
import com.sistema.examenes.servicios.UsuarioService;


@RestController
@RequestMapping("/final")
@CrossOrigin("*")
public class TipoTurnoController {

	@Autowired
	private TipoTurnoService tipoTurnoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	//CRUD TIPO TURNO
	@PostMapping("/tipo-turno")
	public ResponseEntity</*TipoTurno*/?> guardarTipoTurno(@Valid @RequestBody TipoTurno tipoTurnoStr) throws JsonProcessingException {
		try {
			TipoTurno tipoTurnoGuardado = tipoTurnoService.guardarTipoTurno(tipoTurnoStr);
			return ResponseEntity.ok(tipoTurnoGuardado);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PutMapping("/tipo-turno")
	public ResponseEntity<?> editarTipoTurno(@Valid @RequestBody TipoTurno tipoTurnoStr) throws JsonProcessingException {
		try {
			TipoTurno tipoTurnoEditado = tipoTurnoService.editarTipoTurno(tipoTurnoStr);
			return ResponseEntity.ok(tipoTurnoEditado);	
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@GetMapping("/tipo-turno")
	public ResponseEntity<?> listarTipoTurno(){
		try {
			List<TipoTurno> listaTiposTurnos = tipoTurnoService.listarTipoTurnoDeUsuario(usuarioService.obtenerUsuarioActual());
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
			return ResponseEntity.ok("Se elimino correctamente el tipo de turno "+t.getNombre()+" y sus reservas.");
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
}
