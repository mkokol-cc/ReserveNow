package com.sistema.examenes.anterior.controladores;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.Comentario;
import com.sistema.examenes.anterior.modelo.Dias;
import com.sistema.examenes.anterior.modelo.Estado;
import com.sistema.examenes.anterior.modelo.Horario;
import com.sistema.examenes.anterior.modelo.HorarioEspecial;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.anterior.repositorios.AsignacionRecursoTipoTurnoRepository;
import com.sistema.examenes.anterior.repositorios.ComentarioRepository;
import com.sistema.examenes.anterior.repositorios.EstadoRepository;
import com.sistema.examenes.anterior.repositorios.HorarioEspecialRepository;
import com.sistema.examenes.anterior.repositorios.HorarioRepository;
import com.sistema.examenes.anterior.repositorios.RecursoRepository;
import com.sistema.examenes.anterior.repositorios.ReservaRepository;
import com.sistema.examenes.anterior.repositorios.ReservanteRepository;
import com.sistema.examenes.anterior.repositorios.TipoTurnoRepository;

@RestController
@RequestMapping("/api/v1/public")
@CrossOrigin("*")
public class PublicController {

	@Autowired
	private ComentarioRepository repo;
	
	@Autowired
	private ReservaRepository reservaRepo;
	
	@Autowired
	private RecursoRepository repoRecurso;
	
	@Autowired
	private TipoTurnoRepository repoTipoTurno;
	
	@Autowired
	private AsignacionRecursoTipoTurnoRepository asignacionRepo;
	
	@Autowired
	private HorarioRepository horarioRepo;
	
	@Autowired
	private HorarioEspecialRepository horarioEspRepo;
	
	@Autowired
	private EstadoRepository estadoRepo;
	
	@Autowired
	private ReservanteRepository reservanteRepo;
	

	@GetMapping("/comentarios")
	public String listarComentarios() throws JsonProcessingException {
		List<Comentario> listaComentarios = repo.findAll();
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(listaComentarios);
		return json;
	}
	
	
	@PostMapping("/comentarios")
	public Comentario registrarComentario(@RequestBody Comentario comentario, @RequestParam("numero") int numero) {
		Optional<Comentario> respuestaOptional = repo.findById((long) numero);
	    Comentario respuesta = respuestaOptional.orElse(null);
	    comentario.setRespuesta(respuesta);
	    return repo.save(comentario);
	}
	
	@PostMapping(value= "/reserva")
	public Reserva guardarReserva(@RequestBody JsonNode reservaStr) throws Exception {
		System.out.println("VAMOS A LEER LA RESERGVA QUE ME PASASTE");
		System.out.println("ID ASIGNACION: "+reservaStr.get("asignacion").get("id").asLong());
		System.out.println("FECHA: "+reservaStr.get("fecha").asText());
		System.out.println("HORA: "+reservaStr.get("hora").asText());
		System.out.println("NOTA: "+reservaStr.get("nota").asText());
		System.out.println("TELEFONO RESERVANTE: "+reservaStr.get("reservante").get("telefono").asText());

		//obtenemos la asignacion por su id
		AsignacionRecursoTipoTurno asig = asignacionRepo.getById(reservaStr.get("asignacion").get("id").asLong());
		Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(reservaStr.get("fecha").asText());
		LocalTime hora = LocalTime.parse(reservaStr.get("hora").asText());
		Time time = Time.valueOf(hora);
		Estado e = estadoRepo.getById((long) 2);//guardado porque es directamente de admin

		//armamos una reserva
		Reserva r = new Reserva();
		r.setFecha(fecha);
		r.setHora(time);
		r.setNota(reservaStr.get("nota").asText());
		
		Reservante reservante = reservanteRepo.findByTelefono(reservaStr.get("reservante").get("telefono").asText());
		if(reservante!=null) {
			if(reservante.isHabilitado()) {
				r.setReservante(reservante);
			}else {
				return null;
			}
		}else {
			Reservante res = new Reservante();/*
			if (reservaStr.get("reservante").get("nombre") != null) {
				res.setNombre(reservaStr.get("reservante").get("nombre").asText());
			}
			if (reservaStr.get("reservante").get("apellido") != null) {
				res.setNombre(reservaStr.get("reservante").get("apellido").asText());
			}
			if (reservaStr.get("reservante").get("dni") != null) {
				res.setNombre(reservaStr.get("reservante").get("dni").asText());
			}
			res.setTelefono(reservaStr.get("reservante").get("telefono").asText());*/
			res.setHabilitado(true);
			r.setReservante(reservanteRepo.save(res));
		}
		
		r.setAsignacionTipoTurno(asig);
		r.setEstado(e);
		r.setCambioEstado(null);
		r.setTipoReserva(reservaStr.get("tipoReserva").asText());
		
		
		if(comprobarHorarioReserva(asig,time,fecha)) {
			return reservaRepo.save(r);
		}else {
			throw new Exception("La hora seleccionada no está disponible para esta asignación de turno");
		}
	}
	
	
	@PostMapping("/reserva/{id}/cambiar-estado")
	public Reserva cambiarEstadoReserva(@PathVariable int id,@RequestBody int idEstado) throws Exception {
		try {
			Reserva r = reservaRepo.getById((long)id);
			Estado e = estadoRepo.getById((long)idEstado);
			r.setEstado(e);
			return reservaRepo.save(r);
		}catch(Exception e) {
			System.out.println(e);
			throw new Exception("Hubo un error al tratar de cambiar el estado de la reserva");
		}
	}
	
	
	
	@GetMapping("/recursos")
	public String listarRecursos() throws JsonProcessingException {
		List<Recurso> listaRecursos = repoRecurso.findAll();
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(listaRecursos);
		return json;
	}

	@GetMapping("/tipos-de-turno")
	public String listarTiposDeTurno() throws JsonProcessingException {
		List<TipoTurno> listaTiposDeTurno = repoTipoTurno.findAll();
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(listaTiposDeTurno);
		return json;
	}
	
	@GetMapping("/recursos/tipo-turno-{id}")
	public String listarRecursosPorTipoTurno(@PathVariable Long id) throws JsonProcessingException {
		TipoTurno t = repoTipoTurno.getById(id);
		List<ObjectNode> listaAsignaciones = new ArrayList<>();
		for(AsignacionRecursoTipoTurno a : t.getRecursosTipoTurno()) {
			if(!a.isEliminado()) {
				ObjectNode asignacion = JsonNodeFactory.instance.objectNode();
		        asignacion.put("nombre", a.getRecurso().getNombre());
		        asignacion.put("id", a.getId());
		        asignacion.put("eliminado", a.isEliminado());
		        listaAsignaciones.add(asignacion);
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(listaAsignaciones);
		return json;
	}

	@GetMapping("/tipos-de-turno/recurso-{id}")
	public String listarTiposDeTurnoPorRecurso(@PathVariable Long id) throws JsonProcessingException {
	    Recurso r = repoRecurso.getById(id);
	    List<ObjectNode> listaAsignaciones = new ArrayList<>();
	    for(AsignacionRecursoTipoTurno a : r.getRecursosTipoTurno()) {
	    	if(!a.isEliminado()) {
		        ObjectNode asignacion = JsonNodeFactory.instance.objectNode();
		        asignacion.put("nombre", a.getTipoTurno().getNombre());
		        asignacion.put("id", a.getId());
		        asignacion.put("eliminado", a.isEliminado());
		        listaAsignaciones.add(asignacion);
	    	}
	    }
	    ObjectMapper mapper = new ObjectMapper();
	    String json = mapper.writeValueAsString(listaAsignaciones);
	    return json;
	}
	
	@GetMapping("asignaciones")
	public String listarAsignaciones() throws JsonProcessingException {
		List<AsignacionRecursoTipoTurno> asignaciones = asignacionRepo.findAll();
	    List<ObjectNode> listaAsig = new ArrayList<>();
	    for(AsignacionRecursoTipoTurno a : asignaciones) {
	    	ObjectNode asignacion = JsonNodeFactory.instance.objectNode();
	    	asignacion.put("id",a.getId());
	    	asignacion.put("cantidadConcurrencia",a.getCantidadConcurrencia());
	    	asignacion.put("eliminado",a.isEliminado());
	    	asignacion.put("senia",a.getSeniaCtvos());
	    	asignacion.put("duracionEnMinutos",a.getDuracionEnMinutos());
	    	asignacion.put("precioDesde",a.getPrecioEstimadoDesdeCtvos());
	    	asignacion.put("precioHasta",a.getPrecioEstimadoHastaCtvos());

	    	ObjectNode tipoTurno = JsonNodeFactory.instance.objectNode();
	    	tipoTurno.put("id", a.getTipoTurno().getId());
	    	tipoTurno.put("nombre", a.getTipoTurno().getNombre());
	    	tipoTurno.put("eliminado", a.getTipoTurno().isEliminado());
	    	asignacion.set("tipoTurno", tipoTurno);
	    	
	    	ObjectNode recurso = JsonNodeFactory.instance.objectNode();
	    	recurso.put("id", a.getRecurso().getId());
	    	recurso.put("nombre", a.getRecurso().getNombre());
	    	recurso.put("eliminado", a.getRecurso().isEliminado());
	    	asignacion.set("recurso", recurso);
	    	
	    	listaAsig.add(asignacion);
	    }
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(listaAsig);
		return json;
	}
	
	
	@GetMapping("horarios-asignacion/{id}")
	public String verAsignacion(@PathVariable Long id) throws JsonProcessingException {
		AsignacionRecursoTipoTurno a = asignacionRepo.getById(id);
	    List<ObjectNode> listaHorarios = new ArrayList<>();
	    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	    for(Horario h : horarioRepo.findByAsignacion(a)) {
	    	ObjectNode horario = JsonNodeFactory.instance.objectNode();
	    	horario.put("dia",h.getDia().name());
	        horario.put("desde",formatter.format(h.getDesde()));
	        horario.put("hasta",formatter.format(h.getHasta()));
	        listaHorarios.add(horario);
	    }
	    ObjectMapper mapper = new ObjectMapper();
	    String json = mapper.writeValueAsString(listaHorarios);
	    return json;
	}
	
	
	
	@GetMapping("horarios-asignacion/{id}/{fecha}")
	public String verAsignacion(@PathVariable Long id,@PathVariable @DateTimeFormat(pattern="dd-MM-yyyy") Date fecha) throws JsonProcessingException {
		List<ObjectNode> listaHorarios = new ArrayList<>();
	    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		AsignacionRecursoTipoTurno a = asignacionRepo.getById(id);
		List<HorarioEspecial> horariosParaLaFecha = getHorariosEspecialesAsigPorFecha(a,fecha);
		if(horariosParaLaFecha.isEmpty()) {
			List<Horario> horarios = getHorariosAsigPorFecha(a,fecha);		    
		    for(Horario h : horarios) {
		    	ObjectNode horario = JsonNodeFactory.instance.objectNode();
		    	horario.put("dia",h.getDia().name());
		        horario.put("desde",formatter.format(h.getDesde()));
		        horario.put("hasta",formatter.format(h.getHasta()));
		        listaHorarios.add(horario);
		    }
		}else {		    
		    for(HorarioEspecial h : horariosParaLaFecha) {
		    	ObjectNode horario = JsonNodeFactory.instance.objectNode();
		    	if(h.isCerrado()) {
		    		horario.put("id",h.getId());
			        horario.putNull("desde");
			        horario.putNull("hasta");
			        String fechaFormateada = new SimpleDateFormat("dd-MM-yyyy").format(h.getFecha());
			        horario.put("fecha",fechaFormateada);
			        horario.put("motivo",h.getMotivo());
			        horario.put("feriado",h.isCerrado());
		    	}else {
		    		horario.put("id",h.getId());
			        horario.put("desde",formatter.format(h.getDesde()));
			        horario.put("hasta",formatter.format(h.getHasta()));
			        String fechaFormateada = new SimpleDateFormat("dd-MM-yyyy").format(h.getFecha());
			        horario.put("fecha",fechaFormateada);
			        horario.put("motivo",h.getMotivo());
			        horario.put("feriado",h.isCerrado());
		    	}
		        listaHorarios.add(horario);
		    }
		}
		ObjectMapper mapper = new ObjectMapper();
	    String json = mapper.writeValueAsString(listaHorarios);
	    return json;
	}
	
	
	private List<HorarioEspecial> getHorariosEspecialesAsigPorFecha(AsignacionRecursoTipoTurno a, Date fecha) {
		
        List<HorarioEspecial> hEspeciales = horarioEspRepo.findByAsignacion(a);
        List<HorarioEspecial> horariosParaLaFecha = new ArrayList<>();
        for(HorarioEspecial h : hEspeciales) {
            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
            String fechaFormateada1 = formato.format(fecha);
            String fechaFormateada2 = formato.format(h.getFecha());
        	if(fechaFormateada2.equals(fechaFormateada1)) {
        		horariosParaLaFecha.add(h);
        	}
        }
        return horariosParaLaFecha;
		//return horarioEspRepo.findByAsignacionAndFecha(a,fecha);
	}
	
	private List<Horario> getHorariosAsigPorFecha(AsignacionRecursoTipoTurno a, Date fecha){
		int numeroDia = Dias.DOMINGO.getEnumDiasDeDate(fecha);
        List<Horario> horarios = horarioRepo.findByAsignacion(a);
        List<Horario> horariosParaLaFecha = new ArrayList<>();
        for(Horario h : horarios) {
        	//System.out.println("Voy a comparar "+h.getDia().ordinal()+" con "+numeroDia);
        	if(h.getDia().ordinal()==numeroDia) {
        		horariosParaLaFecha.add(h);
        		//System.out.println("son iguales por lo tanto lo agrego");
        	}
        }
        return horariosParaLaFecha;
	}
	
	
	@GetMapping("horarios-disponibles-asignacion/{idAsig}/{fecha}")
	public String horarioDisponiblesParaAsignacion(@PathVariable Long idAsig,@PathVariable @DateTimeFormat(pattern="dd-MM-yyyy") Date fecha) throws JsonProcessingException {
		AsignacionRecursoTipoTurno a = asignacionRepo.getById(idAsig);
		ArrayList<Time> tiempos = new ArrayList<>();
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		List<ObjectNode> listaHorarios = new ArrayList<>();
		
		if(!getHorariosEspecialesPosiblesAsign(a,fecha).isEmpty()) {
			for(Time t : getHorariosEspecialesPosiblesAsign(a,fecha)) {
				ObjectNode horario = JsonNodeFactory.instance.objectNode();
				horario.put("hora", formatter.format(t));
				horario.put("disponible", a.getCantidadConcurrencia()>reservaRepo.findByAsignacionTipoTurnoAndFechaAndHora(a, fecha, t).size());
				listaHorarios.add(horario);
			}
		}else {
			for(Time t : getHorariosPosiblesAsign(a,fecha)) {
				ObjectNode horario = JsonNodeFactory.instance.objectNode();
				horario.put("hora", formatter.format(t));
				horario.put("disponible", a.getCantidadConcurrencia()>reservaRepo.findByAsignacionTipoTurnoAndFechaAndHora(a, fecha, t).size());
				listaHorarios.add(horario);
			}
		}
		
		ObjectMapper mapper = new ObjectMapper();
	    String json = mapper.writeValueAsString(listaHorarios);
	    return json;
	}
	
	
	private boolean comprobarHorarioReserva(AsignacionRecursoTipoTurno a, Time hora, Date fecha) {
		
		List<Reserva> reservasDelDia = reservaRepo.findByAsignacionTipoTurnoAndFecha(a,fecha);
		
		System.out.println("VAMOS A VER LAS RESERVAS DE LA ASIGNACION "+a.getId()+" PARA EL DIA "+fecha);
		
		for(Reserva r : reservasDelDia) {
			
			
			LocalTime localTime1 = r.getHora().toLocalTime(); // convertir el Time DE LA RESERVA a LocalTime
			LocalTime localTime2 = hora.toLocalTime(); // convertir el parametro Time a LocalTime
			
			System.out.println("HAY UNA RESERVA A LAS "+localTime1);
			
			//AGREGAMOS LA CANTIDAD DE MINUTOS
			LocalTime localTime1Plus30Mins = localTime1.plusMinutes(a.getDuracionEnMinutos());
			//SI ESTA CORRECTO EL HORARIO Y LA CONCURRENCIA DEL HORARIO LO PERMITE NO DEBERIA ENTRAR EN EL SGTE IF
			
			//casos para si es a la misma hora que otro turno
			if(localTime2.equals(localTime1)) {
				if(reservaRepo.findByAsignacionTipoTurnoAndFechaAndHora(a,fecha,hora).size()>=a.getCantidadConcurrencia()){
					System.out.println("ASIGNACION CON CONCURRENCIA LLENA");
					return false;
				}
			}
			//casos para si es a la hora que termina el otro turno
			if (localTime2.isBefore(localTime1Plus30Mins) && localTime2.isAfter(localTime1)) {
				if(!localTime2.equals(localTime1Plus30Mins) && !localTime2.equals(localTime1)) {
					System.out.println("EL HORARIO "+localTime2+" ESTA AL MEDIO DE OTRA RESERVA QUE VA DESDE LAS "+localTime1+" A LAS "+localTime1Plus30Mins);
					return false;
				}
			}
			/*
			if (localTime2.isBefore(localTime1Plus30Mins) && (localTime2.isAfter(localTime1) || localTime2.equals(localTime1))) {
				if(reservaRepo.findByAsignacionTipoTurnoAndFechaAndHora(a,fecha,hora).size()>=a.getCantidadConcurrencia()){
					System.out.println("ASIGNACION CON CONCURRENCIA LLENA");
					return false;
				}
				System.out.println("NO SE PUEDE REGISTRAR LA RESERVA PORQUE HAY UN TURNO QUE VA DESDE LAS:");
				System.out.println(localTime1);
				System.out.println("HASTA LAS:");
				System.out.println(localTime1Plus30Mins);
				System.out.println("ENTONCES NO ESTA DISPONIBLE PARA LA HORA: "+localTime2);
			    return false;
			}*/
		}
		return true;
	}
	
	
	
	
	private ArrayList<Time> getHorariosPosiblesAsign(AsignacionRecursoTipoTurno a,Date fecha) {
		ArrayList<Time> listaDeTiempos = new ArrayList<>();
		List<HorarioEspecial> horariosEsp = horarioEspRepo.findByAsignacionAndFecha(a, fecha);
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(fecha);
		int diaSemana = calendario.get(Calendar.DAY_OF_WEEK);
		int[] dias = {6, 0, 1, 2, 3, 4, 5};//LUNES,MARTES,MIÉRCOLES,JUEVES,VIERNES,SÁBADO,DOMINGO;
		List<Horario> horarios = horarioRepo.findByAsignacionAndDia(a,Dias.values()[dias[diaSemana-1]]);
		
		if(horarios.size()>0) {
			for(Horario h : horarios) {
				LocalTime tiempoAux = h.getDesde().toLocalTime(); // convertir el primer Time a LocalTime
				LocalTime hasta = h.getHasta().toLocalTime(); // convertir el segundo Time a LocalTime
				while (tiempoAux.isBefore(hasta)) {
					listaDeTiempos.add(Time.valueOf(tiempoAux));
					tiempoAux = tiempoAux.plusMinutes(a.getDuracionEnMinutos());//AGREGAMOS LA CANTIDAD DE MINUTOS
				}
			}
		}
		System.out.println("LOS HORARIOS DISPONIBLES SON");
		for(Time t : listaDeTiempos) {
			System.out.println(t);
		}
		return listaDeTiempos;
	}
	
	private ArrayList<Time> getHorariosEspecialesPosiblesAsign(AsignacionRecursoTipoTurno a,Date fecha) {
		ArrayList<Time> listaDeTiempos = new ArrayList<>();
		List<HorarioEspecial> horariosEsp = horarioEspRepo.findByAsignacionAndFecha(a, fecha);
		if(horariosEsp.size()>0) {
			for(HorarioEspecial he : horariosEsp) {
				if(!he.isCerrado()) {
					LocalTime tiempoAux = he.getDesde().toLocalTime(); // convertir el primer Time a LocalTime
					LocalTime hasta = he.getHasta().toLocalTime(); // convertir el segundo Time a LocalTime
					while (tiempoAux.isBefore(hasta)) {
						listaDeTiempos.add(Time.valueOf(tiempoAux));
						tiempoAux = tiempoAux.plusMinutes(a.getDuracionEnMinutos());//AGREGAMOS LA CANTIDAD DE MINUTOS
					}

				}
			}
		}
		System.out.println("LOS HORARIOS DISPONIBLES SON");
		for(Time t : listaDeTiempos) {
			System.out.println(t);
		}
		return listaDeTiempos;
	}

	
	
	
	
	@GetMapping(value="/estados")
	private String getEstados() throws JsonProcessingException {

		
		List<ObjectNode> listaEstados = new ArrayList<>();
	    ObjectMapper mapper = new ObjectMapper();

	    for(Estado e : estadoRepo.findAll()) {
	    	ObjectNode estado = JsonNodeFactory.instance.objectNode();
	    	estado.put("id", e.getId());
	    	estado.put("nombre", e.getNombre());
	    	estado.put("descripcion", e.getDescripcion());	
	    	listaEstados.add(estado);
	    }
	    return mapper.writeValueAsString(listaEstados);
	}
	
	
	

}
 