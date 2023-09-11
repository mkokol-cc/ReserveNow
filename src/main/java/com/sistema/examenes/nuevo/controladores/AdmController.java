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
import com.sistema.examenes.anterior.repositorios.AsignacionRecursoTipoTurnoRepository;
import com.sistema.examenes.anterior.repositorios.EstadoRepository;
import com.sistema.examenes.anterior.repositorios.HorarioEspecialRepository;
import com.sistema.examenes.anterior.repositorios.HorarioRepository;
import com.sistema.examenes.anterior.repositorios.RecursoRepository;
import com.sistema.examenes.anterior.repositorios.ReservaRepository;
import com.sistema.examenes.anterior.repositorios.ReservanteRepository;
import com.sistema.examenes.anterior.repositorios.TipoTurnoRepository;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.nuevo.servicios.ApiResponse;
import com.sistema.examenes.nuevo.servicios_interfaces.AsignacionRecursoTipoTurnoService;
import com.sistema.examenes.nuevo.servicios_interfaces.RecursoService;
import com.sistema.examenes.nuevo.servicios_interfaces.ReservaService;
import com.sistema.examenes.nuevo.servicios_interfaces.ReservanteService;
import com.sistema.examenes.nuevo.servicios_interfaces.TipoTurnoService;
import com.sistema.examenes.repositorios.UsuarioRepository;

@RestController
@RequestMapping("/v1")
@CrossOrigin("*")
public class AdmController {
	@Autowired
	private RecursoRepository recursoRepo;
	
	@Autowired
	private TipoTurnoRepository tipoTurnoRepo;
	
	@Autowired
	private HorarioRepository horarioRepo;
	
	@Autowired
	private HorarioEspecialRepository horarioEspRepo;
	
	@Autowired
	private AsignacionRecursoTipoTurnoRepository asignacionRepo;
	
	@Autowired
	private ReservaRepository reservaRepo;
	
	@Autowired
	private EstadoRepository estadoRepo;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private ReservanteRepository reservanteRepo;
	
	@Autowired
	private AsignacionRecursoTipoTurnoService asignacionService;
	
	@Autowired
	private ReservaService reservaService;
	
	@Autowired
	private RecursoService recursoService;
	
	@Autowired
	private TipoTurnoService tipoTurnoService;
	
	@Autowired
	private ReservanteService reservanteService;
	
	
	//CRUD RECURSO
	@PostMapping("/recurso/add")
	public ResponseEntity<?> guardarRecurso(@Valid @RequestBody Recurso recursoStr) throws JsonProcessingException {
		ApiResponse<Recurso> resp = recursoService.guardarRecurso(recursoStr);
		if(resp.isSuccess()) {
			return new ResponseEntity<>("Se guardó correctamente el Recurso.", HttpStatus.CREATED);	
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
	}

	@PutMapping("/recurso/edit")
	@Transactional
	public ResponseEntity<?> editarRecurso(@Valid @RequestBody Recurso recursoStr) throws JsonProcessingException {
		ApiResponse<Recurso> resp = recursoService.editarRecurso(recursoStr, getUserId());
		if(resp.isSuccess()) {
			return new ResponseEntity<>("Se actualizó correctamente el Recurso.", HttpStatus.OK);
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
	}
	
	@GetMapping("/recurso")
	public ResponseEntity</*List<Recurso>*/?> listarRecursos(){
		ApiResponse<List<Recurso>> resp = recursoService.listarRecurso(usuarioRepo.getById(getUserId()));
		if(resp.isSuccess()) {
			return ResponseEntity.ok(resp.getData());
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
	}
	
	
	//CRUD TIPO TURNO
	@PostMapping("/tipo-turno/add")
	public ResponseEntity</*TipoTurno*/?> guardarTipoTurno(@Valid @RequestBody TipoTurno tipoTurnoStr) throws JsonProcessingException {
		ApiResponse<TipoTurno> resp = tipoTurnoService.guardarTipoTurno(tipoTurnoStr);
		if(resp.isSuccess()) {
			return new ResponseEntity<>("Se guardó correctamente el Tipo de Turno.", HttpStatus.CREATED);	
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
	}

	@PutMapping("/tipo-turno/edit")
	public ResponseEntity<?> editarTipoTurno(@Valid @RequestBody TipoTurno tipoTurnoStr) throws JsonProcessingException {
		ApiResponse<TipoTurno> resp = tipoTurnoService.editarTipoTurno(tipoTurnoStr,getUserId());
		if(resp.isSuccess()) {
			return new ResponseEntity<>("Se editó correctamente el Tipo de Turno.", HttpStatus.CREATED);	
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
	}
	
	@GetMapping("/tipo-turno")
	public ResponseEntity<?/*List<TipoTurno>*/> listarTipoTurno(){
		ApiResponse<List<TipoTurno>> resp = tipoTurnoService.listarTipoTurnoDeUsuario(usuarioRepo.getById(getUserId()));
		if(resp.isSuccess()) {
			return ResponseEntity.ok(resp.getData());
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, resp.getMessage());
	}
	
	
	//CRUD ASIGNACION
	@PostMapping("/asignacion/{idTipoTurno}/{idRecurso}")
	public ResponseEntity<?> guardarAsignacion(@PathVariable Long idTipoTurno,@PathVariable Long idRecurso) throws JsonProcessingException {
		ApiResponse<AsignacionRecursoTipoTurno> resp = asignacionService.guardarAsignacion(idTipoTurno, idRecurso, getUserId());
		if(resp.isSuccess()) {
			return ResponseEntity.ok(resp.getData());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
	}
	
	@GetMapping("/asignacion")
	public ResponseEntity<?> listarAsignaciones(){
		ApiResponse<List<AsignacionRecursoTipoTurno>> resp = asignacionService.listarAsignacionPorUsuario(getUserId());
		if(resp.isSuccess()) {
			return ResponseEntity.ok(resp.getData());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
	}
	
	@PutMapping("/asignacion/edit")
	@Transactional
	public ResponseEntity<?> editarAsignacion(@Valid @RequestBody AsignacionRecursoTipoTurno asigStr) throws JsonProcessingException {
		ApiResponse<AsignacionRecursoTipoTurno> resp = asignacionService.editarAsignacion(asigStr, getUserId());
		if(resp.isSuccess()) {
			return ResponseEntity.ok(resp.getData());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
	}
	
	
	//CRUD RESERVAS
	@PostMapping("/reservas/{idAsignacion}/add")
	public ResponseEntity<?> registrarReserva(@PathVariable Long idAsignacion,@Valid @RequestBody Reserva reservaStr) throws JsonProcessingException {
		Reservante r = reservaStr.getReservante();
		Usuario u = usuarioRepo.getById(getUserId());
		r.setUsuario(u);
		ApiResponse<Reserva> resp = reservaService.guardarReserva(reservaStr,getUserId());
		if(resp.isSuccess()) {
			return ResponseEntity.ok(resp.getData());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
	}
	
	@PutMapping("/reservas/{idReserva}/edit")
	public ResponseEntity<?> editarReserva(@PathVariable Long idReserva,@Valid @RequestBody Reserva reservaStr) throws JsonProcessingException {
		try {
			reservaStr.setId(idReserva);
			ApiResponse<Reserva> resp = reservaService.editarReserva(reservaStr);
			if(resp.isSuccess()) {
				return ResponseEntity.ok(resp.getData());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
		}
		/*
		Reserva r = reservaRepo.getById(idReserva);
		//solo la puede editar el adminisrtador
		if(r.getAsignacionTipoTurno().getRecurso().getUsuario()==usuarioRepo.getById(getUserId())){
			reservaStr.setReservante(r.getReservante());
			reservaStr.setAsignacionTipoTurno(r.getAsignacionTipoTurno());
			reservaStr.setId(idReserva);
			return ResponseEntity.ok(reservaRepo.save(reservaStr));
		}else{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario No Autorizado");
		}*/
	}
	
	@GetMapping("/reservas")
	@Transactional
	public ResponseEntity</*List<Reserva>*/?> listarReservas() throws JsonProcessingException {
		
		try {
			ApiResponse<List<Reserva>> resp = reservaService.listarReservaPorUsuario(getUserId());
			if(resp.isSuccess()) {
				return ResponseEntity.ok(resp.getData());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
		}/*
		List<Reserva> listaFinal = new ArrayList<>();
		//buscamos todas las reservas de las asignaciones que contengan los recursos del usuario
		List<Recurso> recursosDelUsuario = recursoRepo.findByUsuario(usuarioRepo.getById(getUserId()));
		for(Recurso r : recursosDelUsuario) {
			List<AsignacionRecursoTipoTurno> asignaciones = r.getRecursosTipoTurno();
			for(AsignacionRecursoTipoTurno a : asignaciones) {
				listaFinal.addAll(a.getReservas());
			}
		}
		return ResponseEntity.ok(listaFinal);*/
	}
	
	/* ESTA POR SI SE QUIERE HACER PUBLICA
	@GetMapping("/reservas/{idUsuario}")
	public ResponseEntity<List<Reserva>> listarReservas(@PathVariable Long idUsuario) throws JsonProcessingException {
		List<Reserva> listaFinal = new ArrayList<>();
		List<Recurso> recursosDelUsuario = recursoRepo.findByUsuario(usuarioRepo.getById(idUsuario));
		for(Recurso r : recursosDelUsuario) {
			List<AsignacionRecursoTipoTurno> asignaciones = r.getRecursosTipoTurno();
			for(AsignacionRecursoTipoTurno a : asignaciones) {
				listaFinal.addAll(a.getReservas());
			}
		}
		return ResponseEntity.ok(listaFinal);
	}
	*/
	
	@DeleteMapping("/reservas/{idReserva}/delete")//solo lo puede hacer el administrador
	public ResponseEntity<?> borrarReservas(@PathVariable Long idReserva) throws JsonProcessingException {
		
		try {
			ApiResponse<Reserva> resp = reservaService.eliminarReservaPorId(idReserva, getUserId());
			if(resp.isSuccess()) {
				return ResponseEntity.ok("Se elimino la reserva");
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
		}
		/*
		Reserva r = reservaRepo.getById(idReserva);
		if(r==null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Reserva No Encontrada");
		}else {
			if(r.getAsignacionTipoTurno().getRecurso().getUsuario()==usuarioRepo.getById(getUserId())) {
				reservaRepo.delete(r);
				return (ResponseEntity<?>) ResponseEntity.ok();	
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario No Autorizado");
			}
		}*/
	}
	
	//CRUD CONFIGURACION
	//AHORA NO
	
	//"CRUD" RESERVANTE
	@GetMapping("/clientes")
	public ResponseEntity</*List<Reservante>*/?> listarClientes() throws JsonProcessingException {
		
		try {
			ApiResponse<List<Reservante>> resp = reservanteService.listarReservanteDeUsuario(usuarioRepo.getById(getUserId()));
			if(resp.isSuccess()) {
				return ResponseEntity.ok(resp.getData());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
		}
		
		/*
		//buscarReservantePorCliente
		List<Reservante> listaClientes = reservanteRepo.findByUsuario(usuarioRepo.getById(getUserId()));
		return ResponseEntity.ok(listaClientes);*/
	}
	
	@PutMapping("/clientes/{idCliente}/edit")
	public ResponseEntity<?> editarCliente(@PathVariable Long idCliente,@Valid @RequestBody Reservante reservanteStr) throws JsonProcessingException {
		try {
			ApiResponse<Reservante> resp = reservanteService.editarReservante(reservanteStr, getUserId());
			if(resp.isSuccess()) {
				return ResponseEntity.ok(resp.getData());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
		}
		
		/*
		Reservante r = reservanteRepo.getById(idCliente);
		if(r.getUsuario()==usuarioRepo.getById(getUserId())) {
			reservanteStr.setId(idCliente);
			reservanteStr.setReservas(r.getReservas());
			return ResponseEntity.ok(reservanteRepo.save(reservanteStr));	
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario No Autorizado");
		}*/
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
}
