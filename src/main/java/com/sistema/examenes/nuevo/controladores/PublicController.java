package com.sistema.examenes.nuevo.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Estado;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios.ApiResponse;
import com.sistema.examenes.nuevo.servicios_interfaces.AsignacionRecursoTipoTurnoService;
import com.sistema.examenes.nuevo.servicios_interfaces.EstadoService;
import com.sistema.examenes.nuevo.servicios_interfaces.RecursoService;
import com.sistema.examenes.nuevo.servicios_interfaces.ReservaService;
import com.sistema.examenes.repositorios.UsuarioRepository;



@RestController
@RequestMapping("/v1/public")
@CrossOrigin("*")
public class PublicController {
	
	@Autowired
	UsuarioRepository usuarioRepo;
	/*
	@Autowired
	RecursoService recursoService;	
	*/
	@Autowired
	AsignacionRecursoTipoTurnoService asignacionService;	
	
	@Autowired
	EstadoService estadoService;	
	
	@Autowired
	ReservaService reservaService;
	/*
	@GetMapping("/{idUserPage}/recurso")
	public ResponseEntity<?> listarRecursosPorPageId(@PathVariable String idUserPage) {
		try {
			Usuario u = getUserByPageId(idUserPage);
			ApiResponse<List<Recurso>> resp = recursoService.listarRecurso(u);
			if(resp.isSuccess()) {
				return ResponseEntity.ok(resp.getData());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
		}
	}*/
	
	@GetMapping("/{idUserPage}/asignacion")
	public ResponseEntity<?> listarAsignacionesPorPageId(@PathVariable String idUserPage) {
		try {
			Usuario u = getUserByPageId(idUserPage);
			ApiResponse<List<AsignacionRecursoTipoTurno>> resp = asignacionService.listarAsignacionPorUsuario(u.getId());
			if(resp.isSuccess()) {
				return ResponseEntity.ok(resp.getData());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
		}
	}

	
	@GetMapping("/estado")
	public ResponseEntity<?> listarEstados() {
		try {
			ApiResponse<List<Estado>> resp = estadoService.listarEstados();
			if(resp.isSuccess()) {
				return ResponseEntity.ok(resp.getData());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
		}
	}
	
	
	@PostMapping("/{idUserPage}/reservas/{idAsignacion}/add")
	public ResponseEntity<?> registrarReserva(@PathVariable String idUserPage,@PathVariable Long idAsignacion,@RequestBody Reserva reservaStr) throws JsonProcessingException {
		try {
			Reservante r = reservaStr.getReservante();
			Usuario u = getUserByPageId(idUserPage);
			r.setUsuario(u);
			ApiResponse<Reserva> resp = reservaService.guardarReserva(reservaStr,u.getId());
			if(resp.isSuccess()) {
				return ResponseEntity.ok(resp.getData());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
		}
	}
	
	
	private Usuario getUserByPageId(String url){
		try {
			Usuario u = usuarioRepo.findByDbUrl(url);
			return u;
		}catch(Exception e) {
			return null;
		}
	}
}