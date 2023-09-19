package com.sistema.examenes.nuevo.controladores;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios.ApiResponse;
import com.sistema.examenes.nuevo.servicios_interfaces.AsignacionRecursoTipoTurnoService;
import com.sistema.examenes.nuevo.servicios_interfaces.RecursoService;
import com.sistema.examenes.nuevo.servicios_interfaces.ReservaService;
import com.sistema.examenes.nuevo.servicios_interfaces.ReservanteService;
import com.sistema.examenes.nuevo.servicios_interfaces.TipoTurnoService;
import com.sistema.examenes.repositorios.UsuarioRepository;

@RestController
@Validated
@RequestMapping("/v1.0")
@CrossOrigin("*")
public class PruebaController {
	
		@Autowired
		ReservaService reservaService;
		@Autowired
		RecursoService recursoService;
		@Autowired
		TipoTurnoService tipoTurnoService;
		@Autowired
		AsignacionRecursoTipoTurnoService asignacionService;
		@Autowired
		ReservanteService reservanteService;
		
		@Autowired
		private UsuarioRepository usuarioRepo;
		
		//CRUD RECURSO
		@PostMapping("/recurso/add")
		public ResponseEntity<?> guardarRecurso(/*@Valid */@RequestBody Recurso recursoStr) throws JsonProcessingException {
			try {
				recursoStr.setUsuario(getUser());
				ApiResponse<Recurso> resp = recursoService.guardarRecurso(recursoStr);
				if(resp.isSuccess()) {
					return new ResponseEntity<>("Se guardó correctamente el Recurso.", HttpStatus.CREATED);	
				}
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
			}catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
		}

		@PutMapping("/recurso/edit")
		@Transactional
		public ResponseEntity<?> editarRecurso(/*@Valid */@RequestBody Recurso recursoStr) throws JsonProcessingException {
			try {
				recursoStr.setUsuario(getUser());
				ApiResponse<Recurso> resp = recursoService.editarRecurso(recursoStr);
				if(resp.isSuccess()) {
					return new ResponseEntity<>("Se editó correctamente el Recurso.", HttpStatus.OK);	
				}
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
			}catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
			//return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}
		
		@GetMapping("/recurso")
		public ResponseEntity</*List<Recurso>*/?> listarRecursos(){
			try {
				//recursoStr.setUsuario(getUser());
				ApiResponse<List<Recurso>> resp = recursoService.listarRecurso(getUser());
				if(resp.isSuccess()) {
					return new ResponseEntity<>(resp.getData(), HttpStatus.OK);	
				}
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
			}catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
			//return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}
		
		
		//CRUD TIPO TURNO
		@PostMapping("/tipo-turno/add")
		public ResponseEntity</*TipoTurno*/?> guardarTipoTurno(/*@Valid*/ @RequestBody TipoTurno tipoTurnoStr) throws JsonProcessingException {
			try {
				tipoTurnoStr.setUsuario(getUser());
				ApiResponse<TipoTurno> resp = tipoTurnoService.guardarTipoTurno(tipoTurnoStr);
				if(resp.isSuccess()) {
					return new ResponseEntity<>("Se guardó correctamente el Tipo de Turno.", HttpStatus.CREATED);	
				}
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
			}catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
			//return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}

		@PutMapping("/tipo-turno/edit")
		public ResponseEntity<?> editarTipoTurno(/*@Valid*/@RequestBody TipoTurno tipoTurnoStr) throws JsonProcessingException {
			try {
				tipoTurnoStr.setUsuario(getUser());
				ApiResponse<TipoTurno> resp = tipoTurnoService.editarTipoTurno(tipoTurnoStr);
				if(resp.isSuccess()) {
					return new ResponseEntity<>("Se editó correctamente el Tipo de Turno.", HttpStatus.OK);	
				}
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
			}catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
			//return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}
		
		@GetMapping("/tipo-turno")
		public ResponseEntity<?/*List<TipoTurno>*/> listarTipoTurno(){
			try {
				ApiResponse<List<TipoTurno>> resp = tipoTurnoService.listarTipoTurnoDeUsuario(getUser());
				if(resp.isSuccess()) {
					return new ResponseEntity<>(resp.getData(), HttpStatus.OK);	
				}
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
			}catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
			//return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}
		
		
		//CRUD ASIGNACION
		@PostMapping("/asignacion/{idTipoTurno}/{idRecurso}")
		public ResponseEntity<?> guardarAsignacion(@PathVariable Long idTipoTurno,@PathVariable Long idRecurso) throws JsonProcessingException {
			try {
				ApiResponse<AsignacionRecursoTipoTurno> resp = asignacionService.guardarAsignacion(idTipoTurno, idRecurso, getUserId());
				if(resp.isSuccess()) {
					return new ResponseEntity<>(resp.getData(), HttpStatus.CREATED);	
				}
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
			}catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
		}
		
		@GetMapping("/asignacion")
		public ResponseEntity<?> listarAsignaciones(){
			try {
				ApiResponse<List<AsignacionRecursoTipoTurno>> resp = asignacionService.listarAsignacionPorUsuario(getUserId());
				if(resp.isSuccess()) {
					return new ResponseEntity<>(resp.getData(), HttpStatus.OK);	
				}
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
			}catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
		}
		
		@PutMapping("/asignacion/edit")
		@Transactional
		public ResponseEntity<?> editarAsignacion(/*@Valid */@RequestBody AsignacionRecursoTipoTurno asigStr) throws JsonProcessingException {
			try {
				ApiResponse<AsignacionRecursoTipoTurno> resp = asignacionService.editarAsignacion(asigStr,getUserId());
				if(resp.isSuccess()) {
					return new ResponseEntity<>(resp.getData(), HttpStatus.OK);	
				}
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
			}catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
		}
		
		
		//CRUD RESERVAS
		@PostMapping("/reservas/{idAsignacion}/add")
		public ResponseEntity<?> registrarReserva(@PathVariable Long idAsignacion,/*@Valid*/ @RequestBody Reserva reservaStr) throws JsonProcessingException {
			try {
				ApiResponse<Reserva> resp = reservaService.nuevaReserva(reservaStr,idAsignacion);
				if(resp.isSuccess()) {
					return new ResponseEntity<>(resp.getData(), HttpStatus.CREATED);	
				}
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
			}catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
			/*
			
			ApiResponse<Reserva> resp = reservaService.nuevaReserva(reservaStr, idAsignacion);
			return addReserva(resp.getData()) ? new ResponseEntity<>("BIEN", HttpStatus.CREATED) : 
				new ResponseEntity<>("MAL", HttpStatus.CREATED);*/
			//return new ResponseEntity<>(resp.getMessage(), HttpStatus.CREATED);
		}
		
		private boolean addReserva(@Valid Reserva reservaStr) {
			return true;
		}
		
		@PutMapping("/reservas/{idReserva}/edit")
		public ResponseEntity<?> editarReserva(@PathVariable Long idReserva,/*@Valid*/ @RequestBody Reserva reservaStr) throws JsonProcessingException {
			try {
				ApiResponse<Reserva> resp = reservaService.editarReserva(reservaStr,getUserId());
				if(resp.isSuccess()) {
					return new ResponseEntity<>(resp.getData(), HttpStatus.OK);	
				}
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
			}catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
			//return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}
		
		@GetMapping("/reservas")
		@Transactional
		public ResponseEntity</*List<Reserva>*/?> listarReservas() throws JsonProcessingException {
			try {
				ApiResponse<List<Reserva>> resp = reservaService.listarReservaPorUsuario(getUserId());
				if(resp.isSuccess()) {
					return new ResponseEntity<>(resp.getData(), HttpStatus.OK);	
				}
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
			}catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
			//return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}
		
		@DeleteMapping("/reservas/{idReserva}/delete")//solo lo puede hacer el administrador
		public ResponseEntity<?> borrarReservas(@PathVariable Long idReserva) throws JsonProcessingException {
			try {
				ApiResponse<Reserva> resp = reservaService.eliminarReservaPorId(idReserva,getUserId());
				if(resp.isSuccess()) {
					return new ResponseEntity<>(resp.getData(), HttpStatus.OK);	
				}
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
			}catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
			//return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
		}
		
		//CRUD CONFIGURACION
		//AHORA NO
		
		//"CRUD" RESERVANTE
		@GetMapping("/clientes")
		public ResponseEntity</*List<Reservante>*/?> listarClientes() throws JsonProcessingException {
			try {
				ApiResponse<List<Reservante>> resp = reservanteService.listarReservanteDeUsuario(getUser());
				if(resp.isSuccess()) {
					return new ResponseEntity<>(resp.getData(), HttpStatus.OK);	
				}
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
			}catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
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
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
			}catch(Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
			//return new ResponseEntity<>("Esta BIEN", HttpStatus.CREATED);
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
