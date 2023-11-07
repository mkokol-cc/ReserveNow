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
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.servicios.UsuarioService;
import com.sistema.examenes.servicios_v2.RecursoServiceV2;

@RestController
@RequestMapping("/v1.1")
@CrossOrigin("*")
public class RecursoController {
	/*
	@Autowired
	private RecursoService recursoService;
	*/
	
	@Autowired
	private RecursoServiceV2 recursoService;
	
	@Autowired
	private UsuarioService usuarioService;

	//CRUD RECURSO
	@PostMapping("/recurso")
	public ResponseEntity<?> guardarRecurso(@RequestBody Recurso recursoStr) throws JsonProcessingException {
		try {
			recursoStr.setUsuario(usuarioService.obtenerUsuarioActual());
			Recurso recursoGuardado = recursoService.nuevoRecurso(recursoStr);
			return ResponseEntity.ok(recursoGuardado);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@PutMapping("/recurso")
	public ResponseEntity<?> editarRecurso(@RequestBody Recurso recursoStr) throws JsonProcessingException {
		try {
			recursoStr.setUsuario(usuarioService.obtenerUsuarioActual());
			Recurso recursoEditado = recursoService.editarRecurso(recursoStr,usuarioService.obtenerUsuarioActual());
			return ResponseEntity.ok(recursoEditado);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping("/recurso")
	public ResponseEntity<?> listarRecursos(){
		try {
			List<Recurso> listaRecursos = recursoService.listarRecursos(usuarioService.obtenerUsuarioActual());
			return ResponseEntity.ok(listaRecursos);	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	
	@DeleteMapping("/recurso/{id}")
	public ResponseEntity<?> eliminarRecurso(@PathVariable Long id){
		try {
			Recurso r = recursoService.obtenerRecursoPorId(id);
			recursoService.eliminarRecurso(id, usuarioService.obtenerUsuarioActual());
			return ResponseEntity.ok("Se elimino correctamente el recurso "+r.getNombre()+" y se cancelaron sus reservas.");
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@DeleteMapping("/recurso/borrar/{id}")
	public ResponseEntity<?> borrarRecurso(@PathVariable Long id){
		try {
			Recurso r = recursoService.obtenerRecursoPorId(id);
			recursoService.borrarRecurso(id);
			return ResponseEntity.ok("Se elimino correctamente el recurso "+r.getNombre()+" y sus reservas.");
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
