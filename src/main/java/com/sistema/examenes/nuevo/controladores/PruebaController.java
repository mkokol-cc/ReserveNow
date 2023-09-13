package com.sistema.examenes.nuevo.controladores;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.anterior.modelo.TipoTurno;

@RestController
@Validated
@RequestMapping("/v1.0")
@CrossOrigin("*")
public class PruebaController {
		
		//CRUD RECURSO
		@PostMapping("/recurso/add")
		public ResponseEntity<?> guardarRecurso(@Valid @RequestBody Recurso recursoStr) throws JsonProcessingException {
			return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}

		@PutMapping("/recurso/edit")
		@Transactional
		public ResponseEntity<?> editarRecurso(@Valid @RequestBody Recurso recursoStr) throws JsonProcessingException {
			return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}
		
		@GetMapping("/recurso")
		public ResponseEntity</*List<Recurso>*/?> listarRecursos(){
			return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}
		
		
		//CRUD TIPO TURNO
		@PostMapping("/tipo-turno/add")
		public ResponseEntity</*TipoTurno*/?> guardarTipoTurno(@Valid @RequestBody TipoTurno tipoTurnoStr) throws JsonProcessingException {
			return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}

		@PutMapping("/tipo-turno/edit")
		public ResponseEntity<?> editarTipoTurno(@Valid @RequestBody TipoTurno tipoTurnoStr) throws JsonProcessingException {
			return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}
		
		@GetMapping("/tipo-turno")
		public ResponseEntity<?/*List<TipoTurno>*/> listarTipoTurno(){
			return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}
		
		
		//CRUD ASIGNACION
		@PostMapping("/asignacion/{idTipoTurno}/{idRecurso}")
		public ResponseEntity<?> guardarAsignacion(@PathVariable Long idTipoTurno,@PathVariable Long idRecurso) throws JsonProcessingException {
			return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}
		
		@GetMapping("/asignacion")
		public ResponseEntity<?> listarAsignaciones(){
			return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}
		
		@PutMapping("/asignacion/edit")
		@Transactional
		public ResponseEntity<?> editarAsignacion(@Valid @RequestBody AsignacionRecursoTipoTurno asigStr) throws JsonProcessingException {
			return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}
		
		
		//CRUD RESERVAS
		@PostMapping("/reservas/{idAsignacion}/add")
		public ResponseEntity<?> registrarReserva(@PathVariable Long idAsignacion,@Valid @RequestBody Reserva reservaStr) throws JsonProcessingException {
			return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}
		
		@PutMapping("/reservas/{idReserva}/edit")
		public ResponseEntity<?> editarReserva(@PathVariable Long idReserva,@Valid @RequestBody Reserva reservaStr) throws JsonProcessingException {
			return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}
		
		@GetMapping("/reservas")
		@Transactional
		public ResponseEntity</*List<Reserva>*/?> listarReservas() throws JsonProcessingException {
			return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}
		
		@DeleteMapping("/reservas/{idReserva}/delete")//solo lo puede hacer el administrador
		public ResponseEntity<?> borrarReservas(@PathVariable Long idReserva) throws JsonProcessingException {
			return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}
		
		//CRUD CONFIGURACION
		//AHORA NO
		
		//"CRUD" RESERVANTE
		@GetMapping("/clientes")
		public ResponseEntity</*List<Reservante>*/?> listarClientes() throws JsonProcessingException {
			return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}
		
		@PutMapping("/clientes/{idCliente}/edit")
		public ResponseEntity<?> editarCliente(@PathVariable Long idCliente,@Valid @RequestBody Reservante reservanteStr) throws JsonProcessingException {
			return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}

}
