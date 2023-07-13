package com.sistema.examenes.nuevo.controladores;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Dias;
import com.sistema.examenes.anterior.modelo.Horario;
import com.sistema.examenes.anterior.modelo.HorarioEspecial;
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
import com.sistema.examenes.nuevo.servicios_interfaces.ReservaService;
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
	private ReservaService reservaService;
	
	
	//CRUD RECURSO
	@PostMapping("/recurso/add")
	public ResponseEntity<?> guardarRecurso(@RequestBody Recurso recursoStr) throws JsonProcessingException {
		recursoStr.setUsuario(usuarioRepo.getById(getUserId()));
		//los horarios del Recurso van a ser OBLIGATORIOS
		if(!recursoStr.getHorarios().isEmpty()) {
			Set<Horario> horariosDelRecurso = new HashSet<>();
			for(Horario h : recursoStr.getHorarios()) {
				horariosDelRecurso.add(horarioRepo.save(h));
			}
			recursoStr.setHorarios(horariosDelRecurso);
			return ResponseEntity.ok(recursoRepo.save(recursoStr));
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo Horarios Vacío");
		}
	}

	@PutMapping("/recurso/{idRecurso}/edit")
	@Transactional
	public ResponseEntity<?> editarRecurso(@PathVariable Long idRecurso,@RequestBody Recurso recursoStr) throws JsonProcessingException {
		Recurso r = recursoRepo.getById(idRecurso);
		if(r.getUsuario()==usuarioRepo.getById(getUserId())) {
			recursoStr.setUsuario(r.getUsuario());
			
			
			if(!recursoStr.getHorarios().isEmpty()) {
				horarioRepo.deleteByRecurso(r);
				Set<Horario> horarios = new HashSet<>();
				for(Horario h : recursoStr.getHorarios()) {
					horarios.add(horarioRepo.save(h));
				}
				recursoStr.setHorarios(horarios);
			}
			if(!recursoStr.getHorariosEspeciales().isEmpty()) {
				horarioEspRepo.deleteByRecurso(r);
				Set<HorarioEspecial> horariosespeciales = new HashSet<>();
				for(HorarioEspecial h : recursoStr.getHorariosEspeciales()) {
					horariosespeciales.add(horarioEspRepo.save(h));
				}
				recursoStr.setHorariosEspeciales(horariosespeciales);
			}
			
			
			
			Recurso recurso = recursoRepo.save(recursoStr);
			
			
			
			return ResponseEntity.ok(recurso);	
		}else {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario No Autorizado");
		}
	}
	
	@GetMapping("/recurso")
	public ResponseEntity<List<Recurso>> listarRecursos(){
		List<Recurso> recursos = recursoRepo.findByUsuario(usuarioRepo.getById(getUserId()));
		return ResponseEntity.ok(recursos);
	}
	
	
	//CRUD TIPO TURNO
	@PostMapping("/tipo-turno/add")
	public ResponseEntity<TipoTurno> guardarTipoTurno(@RequestBody TipoTurno tipoTurnoStr) throws JsonProcessingException {
		tipoTurnoStr.setUsuario(usuarioRepo.getById(getUserId()));
		TipoTurno tipoTurno = tipoTurnoRepo.save(tipoTurnoStr);
		return ResponseEntity.ok(tipoTurno);
	}

	@PutMapping("/tipo-turno/{idTipoTurno}/edit")
	public ResponseEntity<?> editarTipoTurno(@PathVariable Long idTipoTurno,@RequestBody TipoTurno tipoTurnoStr) throws JsonProcessingException {
		TipoTurno t = tipoTurnoRepo.getById(idTipoTurno);
		if(t.getUsuario()==usuarioRepo.getById(getUserId())) {
			tipoTurnoStr.setUsuario(t.getUsuario());
			TipoTurno tipoTurno = tipoTurnoRepo.save(tipoTurnoStr);
			return ResponseEntity.ok(tipoTurno);	
		}else {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario No Autorizado");
		}
	}
	
	@GetMapping("/tipo-turno")
	public ResponseEntity<List<TipoTurno>> listarTipoTurno(){
		List<TipoTurno> tiposTurno = tipoTurnoRepo.findByUsuario(usuarioRepo.getById(getUserId()));
		return ResponseEntity.ok(tiposTurno);
	}
	
	
	//CRUD ASIGNACION
	@PostMapping("/asignacion/{idTipoTurno}/{idRecurso}")
	public ResponseEntity<?> guardarAsignacion(@PathVariable Long idTipoTurno,@PathVariable Long idRecurso) throws JsonProcessingException {
		Recurso r = recursoRepo.getById(idRecurso);
		TipoTurno t = tipoTurnoRepo.getById(idTipoTurno);
		//si tienen el mismo usuario y son del usuario que hizo el POST
		if(t.getUsuario()==r.getUsuario() && t.getUsuario()==usuarioRepo.getById(getUserId())) {	
			AsignacionRecursoTipoTurno existente = asignacionRepo.findByRecursoAndTipoTurno(r, t);
			if(existente==null) {
				AsignacionRecursoTipoTurno a = new AsignacionRecursoTipoTurno();
				a.setRecurso(r);
				a.setTipoTurno(t);
				a.setEliminado(false);
				a.setHorarios(r.getHorarios());
				a.setHorariosEspeciales(r.getHorariosEspeciales());
				return ResponseEntity.ok(asignacionRepo.save(a));	
			}else {
				existente.setEliminado(false);
				return ResponseEntity.ok(existente);
			}
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario No Autorizado");
		}		
	}
	
	@GetMapping("/asignacion")
	public ResponseEntity<List<AsignacionRecursoTipoTurno>> listarAsignaciones(){
		List<Recurso> recursosDeUsuario = recursoRepo.findByUsuario(usuarioRepo.getById(getUserId()));
		List<AsignacionRecursoTipoTurno> asignaciones = new ArrayList<>();
		for(Recurso r : recursosDeUsuario) {
			asignaciones.addAll(r.getRecursosTipoTurno());
			//podriamos comparar si existe alguna asignacion que tenga recursos y tiposdeturno de diferentes usuarios
		}
		return ResponseEntity.ok(asignaciones);
	}
	
	@PutMapping("/asignacion/{idAsig}/edit")
	@Transactional
	public ResponseEntity<?> editarAsignacion(@PathVariable Long idAsig ,
			@RequestBody AsignacionRecursoTipoTurno asigStr) throws JsonProcessingException {
		AsignacionRecursoTipoTurno a =  asignacionRepo.getById(idAsig);
		asigStr.setRecurso(a.getRecurso());
		asigStr.setTipoTurno(a.getTipoTurno());
		asigStr.setId(idAsig);
		if(a.getRecurso().getUsuario()==a.getTipoTurno().getUsuario()) {
			if(a.getRecurso().getUsuario()==usuarioRepo.getById(getUserId())) {
				AsignacionRecursoTipoTurno savedAsig = asignacionRepo.save(asigStr);
				if(!asigStr.getHorarios().isEmpty()) {
					horarioRepo.deleteByAsignacion(a);
					Set<Horario> horarios = new HashSet<>();
					for(Horario h : asigStr.getHorarios()) {
						h.setAsignacion(savedAsig);
						horarios.add(horarioRepo.save(h));
					}
					savedAsig.setHorarios(horarios);
				}
				if(!asigStr.getHorariosEspeciales().isEmpty()) {
					horarioEspRepo.deleteByAsignacion(a);
					Set<HorarioEspecial> horariosespeciales = new HashSet<>();
					for(HorarioEspecial h : asigStr.getHorariosEspeciales()) {
						h.setAsignacion(savedAsig);
						horariosespeciales.add(horarioEspRepo.save(h));
					}
					savedAsig.setHorariosEspeciales(horariosespeciales);
				}
				return ResponseEntity.ok(asignacionRepo.save(savedAsig));	
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario No Autorizado");
			}
		}else {
			asignacionRepo.delete(a);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Recurso y Tipo de Turno son de diferentes usuarios, se ha eliminado el registro");
		}
	}
	
	
	//CRUD RESERVAS
	@PostMapping("/reservas/{idAsignacion}/add")
	public ResponseEntity<?> registrarReserva(@PathVariable Long idAsignacion,@RequestBody Reserva reservaStr) throws JsonProcessingException {
		try {
			//System.out.println("LLEGUE");
			Reservante r = reservaStr.getReservante();
			//System.out.println("LLEGUE");
			Usuario u = usuarioRepo.getById(getUserId());
			//System.out.println("LLEGUE");
			r.setUsuario(u);
			//System.out.println("LLEGUE");
			//reservaStr.setReservante(r);)
			ApiResponse<Reserva> resp = reservaService.guardarReserva(reservaStr);
			if(resp.isSuccess()) {
				return ResponseEntity.ok(resp.getData());
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp.getMessage());
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+e.getMessage());
		}
		/*
		Reservante existente = reservanteRepo.findByTelefono(reservaStr.getReservante().getTelefono()); 
		if(existente==null) {
			existente = reservanteRepo.save(reservaStr.getReservante());
		}
		reservaStr.setReservante(existente);
		AsignacionRecursoTipoTurno a = asignacionRepo.getById(idAsignacion);
		//comprobar si el dia y hora es valido
		//if(comprobarHorario(a,reservaStr.getHora(), reservaStr.getFecha())) {
			reservaStr.setAsignacionTipoTurno(a);
			if(reservaStr.getAsignacionTipoTurno().getSeniaCtvos()>0) {
				reservaStr.setEstado(estadoRepo.getById((long)1));// id del estado RESERVADO - CON SEÑA
			}else{
				reservaStr.setEstado(estadoRepo.getById((long)2));// id del estado RESERVADO - SIN SEÑA
			}
			return ResponseEntity.ok(reservaRepo.save(reservaStr));	
		//}else {
			//return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Horario o Fecha Incorrecto o Ocupado");
		//}
			*/
	}
	
	@PutMapping("/reservas/{idReserva}/edit")
	public ResponseEntity<?> editarReserva(@PathVariable Long idReserva,@RequestBody Reserva reservaStr) throws JsonProcessingException {
		Reserva r = reservaRepo.getById(idReserva);
		//solo la puede editar el adminisrtador
		if(r.getAsignacionTipoTurno().getRecurso().getUsuario()==usuarioRepo.getById(getUserId())){
			reservaStr.setReservante(r.getReservante());
			reservaStr.setAsignacionTipoTurno(r.getAsignacionTipoTurno());
			reservaStr.setId(idReserva);
			return ResponseEntity.ok(reservaRepo.save(reservaStr));
		}else{
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario No Autorizado");
		}
	}
	
	@GetMapping("/reservas")
	public ResponseEntity<List<Reserva>> listarReservas() throws JsonProcessingException {
		List<Reserva> listaFinal = new ArrayList<>();
		//buscamos todas las reservas de las asignaciones que contengan los recursos del usuario
		List<Recurso> recursosDelUsuario = recursoRepo.findByUsuario(usuarioRepo.getById(getUserId()));
		for(Recurso r : recursosDelUsuario) {
			List<AsignacionRecursoTipoTurno> asignaciones = r.getRecursosTipoTurno();
			for(AsignacionRecursoTipoTurno a : asignaciones) {
				listaFinal.addAll(a.getReservas());
			}
		}
		return ResponseEntity.ok(listaFinal);
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
		}
	}
	
	//CRUD CONFIGURACION
	//AHORA NO
	
	//"CRUD" RESERVANTE
	@GetMapping("/clientes")
	public ResponseEntity<List<Reservante>> listarClientes() throws JsonProcessingException {
		//buscarReservantePorCliente
		List<Reservante> listaClientes = reservanteRepo.findByUsuario(usuarioRepo.getById(getUserId()));
		return ResponseEntity.ok(listaClientes);
	}
	
	@PutMapping("/clientes/{idCliente}/edit")
	public ResponseEntity<?> editarCliente(@PathVariable Long idCliente,@RequestBody Reservante reservanteStr) throws JsonProcessingException {
		Reservante r = reservanteRepo.getById(idCliente);
		if(r.getUsuario()==usuarioRepo.getById(getUserId())) {
			reservanteStr.setId(idCliente);
			reservanteStr.setReservas(r.getReservas());
			return ResponseEntity.ok(reservanteRepo.save(reservanteStr));	
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario No Autorizado");
		}
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

	

	
	/*
	
	
	public boolean comprobarHorario(AsignacionRecursoTipoTurno asig,Time hora, Date fecha) {
		Horario h = comprobarSiElDiaEstaCorrecto(asig,fecha);
		//paso 1 - comprobar si la fecha es correcta | devuelve el objeto Horario si 
		if(h==null) {
			return false;
			
			//paso 2 - comprobar que no coincide con un horario especial de tipo FERIADO
		}else if(comprobarSiElDiaEsFeriado(asig,fecha,hora)) {
			return false;
			
			//paso 3 - comprobar si el horario es correcto
		}else if(comprobarSiElHorarioEstaEnElRango(h.getDesde(), h.getHasta(), hora, asig.getDuracionEnMinutos())) {
			return true;
		}else {
			return false;
		}		
	}
	
	public Horario comprobarSiElDiaEstaCorrecto(AsignacionRecursoTipoTurno asig,Date fecha) {
		//SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        //Date fechaFormateada = sdf.parse(fecha);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Mapa de días de la semana, donde el valor 1 representa el domingo
        String[] diasSemana = {"DOMINGO", "LUNES","MARTES","MIÉRCOLES","JUEVES","VIERNES","SÁBADO"};
        String diaSemana = diasSemana[dayOfWeek - 1];
        System.out.println("El día de la semana es: " + diaSemana);
        
        for(Horario h : asig.getHorarios()) {
			if(h.getDia().name().equals(diaSemana)) {
				return h;
			}
		}
        return null;
	}
	
	public boolean comprobarSiElDiaEsFeriado(AsignacionRecursoTipoTurno asig,Date fecha,Time hora) {
		//SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        //Date fechaFormateada = sdf.parse(fecha);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Mapa de días de la semana, donde el valor 1 representa el domingo
        String[] diasSemana = {"DOMINGO", "LUNES","MARTES","MIÉRCOLES","JUEVES","VIERNES","SÁBADO"};
        String diaSemana = diasSemana[dayOfWeek - 1];
        System.out.println("El día de la semana es: " + diaSemana);
        
        for(HorarioEspecial h : asig.getHorariosEspeciales()) {
			if(h.getFecha().compareTo(fecha)==0 && (h.isCerrado() || 
					comprobarSiElHorarioEstaEnElRango(h.getDesde(), h.getHasta(), hora, asig.getDuracionEnMinutos()))) {
				return true;
			}
		}
        return false;
	}
	
	//retorna true si el string esta entre los parametro desde y hasta
	public boolean comprobarSiElHorarioEstaEnElRango(Time desde, Time hasta, Time horarioAComparar, int duracionEnMin) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
      //Time hora = new Time(sdf.parse(horarioAComparar).getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(horarioAComparar);
        calendar.add(Calendar.MINUTE, duracionEnMin);
        Time nuevaHora = new Time(calendar.getTimeInMillis());
        
        if ((horarioAComparar.compareTo(desde) >= 0 && horarioAComparar.compareTo(hasta) <= 0)&&
        		(nuevaHora.compareTo(desde) >= 0 && nuevaHora.compareTo(hasta) <= 0)) {
            return true;//System.out.println("La hora está dentro del rango especificado.");
        } else {
            return false;//System.out.println("La hora está fuera del rango especificado.");
        }
	}
	
	*/
	
	@GetMapping("prueba")
	public String comprobar(@RequestBody Reserva reservaStr) {
		//List<Horario> h = horarioRepo.obtenerPorHoraYAsignacion(d, reservaStr.getHora(), reservaStr.getAsignacionTipoTurno());
		//List<HorarioEspecial> he = horarioEspRepo.obtenerPorFechaHoraYAsignacion(reservaStr.getFecha(), reservaStr.getHora(), reservaStr.getAsignacionTipoTurno());
		//List<HorarioEspecial> he2 = horarioEspRepo.findByAsignacionAndFecha(asig, reservaStr.getFecha());
		//List<Reserva> r = reservaRepo.buscarPorFechaHoraRecurso(reservaStr.getFecha(), reservaStr.getHora(), reservaStr.getAsignacionTipoTurno().getRecurso().getId());
		
		//if horario no menor a actual
		if(reservaStr.getFecha().isBefore(LocalDate.now())) {
			System.out.println("La fecha es anterior a la actual");
		}else if(!reservaStr.getFecha().isBefore(LocalDate.now()) && reservaStr.getHora().isBefore(LocalTime.now())){
			System.out.println("La hora es anterior a la actual");
		}
		//if !HorarioEspecial
		/*List<HorarioEspecial> he2 = horarioEspRepo.findByAsignacionAndFecha(reservaStr.getAsignacionTipoTurno(), reservaStr.getFecha());
		if(he2.size()==1) {
			for(HorarioEspecial he : he2) {
				if(he.isCerrado()) {
					System.out.println("El dia "+he.getFecha()+" el negocio esta cerrado. Motivo: "+he.getMotivo());
				}
			}
		}*/
		//if Horario especial
		List<HorarioEspecial> he = horarioEspRepo.obtenerPorFechaHoraYAsignacion(reservaStr.getFecha(), reservaStr.getHora(), reservaStr.getAsignacionTipoTurno());
		if(he.size()==1) {
			System.out.println("Puede ser posible dentro del horario");
		}else if(he.size()<1){
			//if Horario
			Dias d = Dias.valueOf(reservaStr.getFecha().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase()); 
			List<Horario> h = horarioRepo.obtenerPorHoraYAsignacion(d, reservaStr.getHora(), reservaStr.getAsignacionTipoTurno());
			if(h.size()==1) {
				System.out.println("El horario esta correcto");
			}	
		}else {
			System.out.println("Hay registrados mas de un HorarioEspecial para la misma fecha y misma hora");
		}
		//if !Reserva
		List<Reserva> r = reservaRepo.buscarPorFechaHoraRecurso(reservaStr.getFecha(), reservaStr.getHora(), reservaStr.getAsignacionTipoTurno().getRecurso().getId());
		if(!r.isEmpty()) {
			System.out.println("El recurso ya tiene una reserva para el dia y la fecha especificada");
		}
		return "";
	}
	
	
}
