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
import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.nuevo.servicios_interfaces.AsignacionRecursoTipoTurnoService;
import com.sistema.examenes.servicios.UsuarioService;

@RestController
@RequestMapping("/v1.1")
@CrossOrigin("*")
public class AsignacionRecursoTipoTurnoController {

	@Autowired
	private AsignacionRecursoTipoTurnoService asignacionService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	//CRUD ASIGNACION
	@PostMapping("/asignacion/{idTipoTurno}/{idRecurso}")
	public ResponseEntity<?> guardarAsignacion(@PathVariable Long idTipoTurno,@PathVariable Long idRecurso) throws JsonProcessingException {
		try {
			AsignacionRecursoTipoTurno asignacionGuardada = asignacionService.guardarAsignacion(idTipoTurno, idRecurso, usuarioService.getIdUsuarioActual());
			return ResponseEntity.ok(asignacionGuardada);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());	
		}

	}
	
	@GetMapping("/asignacion")
	public ResponseEntity<?> listarAsignaciones(){
		try {
			List<AsignacionRecursoTipoTurno> asignaciones = asignacionService.listarAsignacionPorUsuario(usuarioService.getIdUsuarioActual());
			return ResponseEntity.ok(asignaciones);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());	
		}
	}
	
	
	@GetMapping("public/{pageId}/asignacion")
	public ResponseEntity<?> listarAsignacionesPublic(@PathVariable String pageId){
		try {
			List<AsignacionRecursoTipoTurno> asignaciones = asignacionService.listarAsignacionPorUsuario(usuarioService.obtenerUsuarioPorPageId(pageId).getId());
			return ResponseEntity.ok(asignaciones);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());	
		}
	}
	
	
	@PutMapping("/asignacion/edit")
	public ResponseEntity<?> editarAsignacion(@Valid @RequestBody AsignacionRecursoTipoTurno asigStr) throws JsonProcessingException {
		try {
			AsignacionRecursoTipoTurno asignacion = asignacionService.editarAsignacion(asigStr, usuarioService.getIdUsuarioActual());
			return ResponseEntity.ok(asignacion);			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@DeleteMapping("/asignacion/{id}")
	public ResponseEntity<?> eliminarAsignacion(@PathVariable Long id){
		try {
			AsignacionRecursoTipoTurno a = asignacionService.obtenerPorId(id);
			asignacionService.eliminarAsignacion(a, usuarioService.obtenerUsuarioActual());
			return ResponseEntity.ok("Se elimino correctamente la Asignacion "+a.getRecurso().getNombre()+
					" - "+a.getTipoTurno().getNombre()+" y sus reservas.");
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
}
