package com.sistema.examenes.anterior.controladores;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sistema.examenes.anterior.modelo.AsignacionRecursoTipoTurno;
import com.sistema.examenes.anterior.modelo.CambioEstado;
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
import com.sistema.examenes.anterior.repositorios.TipoTurnoRepository;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin("*")
public class AdminController {
	@Autowired
	private RecursoRepository repo;
	
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
	
	@GetMapping("/reservas")
	public String listarReservas() throws JsonProcessingException {
		//List<Reserva> listaReservas = reservaRepo.findAll();
		//ObjectMapper mapper = new ObjectMapper();
		//String json = mapper.writeValueAsString(listaReservas);
		//return json;
		
		
		
		
		
	    List<ObjectNode> listaReservas = new ArrayList<>();
	    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");

	    ObjectMapper mapper = new ObjectMapper();
	    for(Reserva r : reservaRepo.findAll()) {
	    	ObjectNode reserva = JsonNodeFactory.instance.objectNode();
	    	reserva.put("id",r.getId());
	    	reserva.put("nota", r.getNota());
	    	reserva.put("tipoReserva", r.getTipoReserva());
	    	reserva.put("fecha", formatoFecha.format(r.getFecha()));
	    	reserva.put("hora", formatter.format(r.getHora()));
	    	
	    	//reservante
	    	ObjectNode reservante = JsonNodeFactory.instance.objectNode();
	    	reservante.put("id", r.getReservante().getId());
	    	reservante.put("nombre", r.getReservante().getNombre());
	    	reservante.put("apellido",r.getReservante().getApellido());
	    	reservante.put("dni", r.getReservante().getDni());
	    	reservante.put("telefono", r.getReservante().getTelefono());
	    	reservante.put("habilitado", r.getReservante().isHabilitado());
	    	reserva.set("reservante", reservante);
	    	
	    	
	    	//estado
	    	ObjectNode estado = JsonNodeFactory.instance.objectNode();
	    	estado.put("id", r.getEstado().getId());
	    	estado.put("nombre", r.getEstado().getNombre());
	    	reserva.set("estado", estado);
	    	
	    	//cambiosDeEstado
	    	List<ObjectNode> cambiosEstado = new ArrayList<>();
	    	for(CambioEstado c : r.getCambioEstado()) {
		    	ObjectNode cambioEstado = JsonNodeFactory.instance.objectNode();
		    	//cambiosDeEstado estadoAnterior
		    	ObjectNode e = JsonNodeFactory.instance.objectNode();
		    	estado.put("id", c.getEstadoAnterior().getId());
		    	estado.put("nombre", c.getEstadoAnterior().getNombre());
		    	cambioEstado.set("estadoAnterior", e);
		    	//cambiosDeEstado estadonuevo
		    	ObjectNode e2 = JsonNodeFactory.instance.objectNode();
		    	estado.put("id", c.getEstadoNuevo().getId());
		    	estado.put("nombre", c.getEstadoNuevo().getNombre());
		    	cambioEstado.set("estadoNuevo", e2);
		    	//cambiosDeEstado datos
		    	cambioEstado.put("fecha", formatoFecha.format(c.getFecha()));
		    	cambioEstado.put("hora", formatter.format(c.getHora()));
		    	cambiosEstado.add(cambioEstado);
	    	}
	    	reserva.set("cambioEstado", mapper.valueToTree(cambiosEstado));
	    	
	    	
	    	//asignacion
	    	ObjectNode asignacion = JsonNodeFactory.instance.objectNode();
	    	asignacion.put("id",r.getAsignacionTipoTurno().getId());
	    	asignacion.put("cantidadConcurrencia",r.getAsignacionTipoTurno().getCantidadConcurrencia());
	    	asignacion.put("eliminado",r.getAsignacionTipoTurno().isEliminado());
	    	asignacion.put("senia",r.getAsignacionTipoTurno().getSeniaCtvos());
	    	asignacion.put("duracionEnMinutos",r.getAsignacionTipoTurno().getDuracionEnMinutos());
	    	asignacion.put("precioDesde",r.getAsignacionTipoTurno().getPrecioEstimadoDesdeCtvos());
	    	asignacion.put("precioHasta",r.getAsignacionTipoTurno().getPrecioEstimadoHastaCtvos());
	    	//asignacion - tipoturno
	    	ObjectNode tipoTurno = JsonNodeFactory.instance.objectNode();
	    	tipoTurno.put("id", r.getAsignacionTipoTurno().getTipoTurno().getId());
	    	tipoTurno.put("nombre", r.getAsignacionTipoTurno().getTipoTurno().getNombre());
	    	asignacion.set("tipoTurno", tipoTurno);
	    	//asignacion - recurso
	    	ObjectNode recurso = JsonNodeFactory.instance.objectNode();
	    	recurso.put("id", r.getAsignacionTipoTurno().getRecurso().getId());
	    	recurso.put("nombre", r.getAsignacionTipoTurno().getRecurso().getNombre());
	    	asignacion.set("recurso", recurso);
	    	
	    	
	    	reserva.set("asignacion", asignacion);
	    	
	    	
	    	//reserva.put("desde",formatter.format(h.getDesde()));
	    	//reserva.put("hasta",formatter.format(h.getHasta()));
	    	listaReservas.add(reserva);
	    }
	    String json = mapper.writeValueAsString(listaReservas);
	    return json;
	}
	
	
	@GetMapping(value="/clientes")
	private String getClientes() throws JsonProcessingException {
	    Map<Reservante, Long> reservasPorTelefono = reservaRepo.countReservasByReservante();
	    ObjectMapper mapper = new ObjectMapper();
	    return mapper.writeValueAsString(reservasPorTelefono);
	}

	

	/*
	{
	  "descripcion": "Descripción del recurso",
	  "nombre": "Nombre del recurso",
	  "recursosTipoTurno": [
	    {
	      "recurso": {
	        "id": 1
	      },
	      "tipoTurno": {
	        "id": 2
	      }
	    }
	  ],
	  "horarioEspecial": [
	    {
	      "id": 3
	    }
	  ],
	  "horarios": [
	    {
	      "id": 4
	    }
	  ],
	  "eliminado": false,
	}
	
--------------------------------------------------------------------------
--------------------------------------------------------------------------

	{
	  "descripcion": "Descripción del recurso",
	  "nombre": "Nombre del recurso",
	  "eliminado": false,
	}

	*/
	@PostMapping(value= "/recursos")
	public Recurso guardarRecurso(@RequestBody Recurso recursoStr) throws Exception {
		Recurso r = new Recurso();
		r.setNombre(recursoStr.getNombre());
		r.setDescripcion(recursoStr.getDescripcion());
		/*
		Set<Horario> horarios = new HashSet<>();
		for(Horario h : recursoStr.getHorarios()) {
	        Horario nuevo = new Horario();
	        nuevo.setDesde(h.getDesde());
	        nuevo.setHasta(h.getHasta());
	        nuevo.setDia(h.getDia());
	        nuevo.setRecurso(recursoStr);
		    horarios.add(nuevo);
		}
		horarioRepo.saveAll(horarios);
		recursoStr.setHorarios(new HashSet<>(horarios));
		*/
		Recurso nuevo = repo.save(recursoStr);
		asignar();
	    return nuevo;
	}
	
	
	@PostMapping(value= "/recursos-edit")
	public Recurso editarDatosRecurso(@RequestBody Recurso recursoStr) throws Exception {
		Recurso r = repo.getById(recursoStr.getId());
		r.setDescripcion(recursoStr.getDescripcion());
		r.setNombre(recursoStr.getNombre());
		r.setEliminado(recursoStr.isEliminado());
		return repo.save(r);
	}
	
	
	
	@PostMapping("/horarios")
	public Horario guardarHorario(@RequestBody Horario h) {
		return horarioRepo.save(h);
	}

	
	@PostMapping("/tipos-de-turno")
	public TipoTurno guardarTipoDeTurno(@RequestBody TipoTurno tipoTurno) {
		TipoTurno t = new TipoTurno();
		t.setNombre(tipoTurno.getNombre());
		t.setDescripcion(tipoTurno.getDescripcion());
		TipoTurno nuevo = tipoTurnoRepo.save(t);
		System.out.println(nuevo);
		asignar();
	    return nuevo;
	}
	
	@PostMapping("/asignar-recurso-a-tipo-turno")
	public AsignacionRecursoTipoTurno asignarRecursoTipoTurno(@RequestBody AsignacionRecursoTipoTurno asignacion) {
		try {
			asignar();//RECARGAR TODAS LAS ASIGNACIONES POR LAS DUDAS
			System.out.println("HOLA");
			Optional<Recurso> r = repo.findById(asignacion.getRecurso().getId());
			Optional<TipoTurno> t = tipoTurnoRepo.findById(asignacion.getTipoTurno().getId());
			if (r.isPresent() && t.isPresent()) {
			    Recurso recurso = r.get();
			    TipoTurno tipoTurno = t.get();
			    AsignacionRecursoTipoTurno asig = obtenerAsignacion(recurso,tipoTurno);
			    if(asig==null) {
			    	return null;//HUBO UN ERROR PORQUE DEBERIAN EXISTIR TODAS LAS ASIGNACIONES
			    }
			    asig.setEliminado(false);
			    
			    Set<Horario> horarios = new HashSet<>();
			    
			    System.out.println("-----asd--------"+asignacion.getHorarios());
			    
			    
			    if (asignacion.getHorarios() != null) {
			        for (Horario h : asig.getHorarios()) {
			            horarioRepo.delete(h); // Eliminar los horarios antiguos de la base de datos
			        }
			        asig.getHorarios().clear(); // Eliminar los horarios existentes en asig
			        for (Horario h : asignacion.getHorarios()) {
			            System.out.println("UN HORARIO QUE ME PASASTE ES DE "+h.getDesde()+" A "+h.getHasta()+" - "+h.getDia());
			            Horario nuevo = new Horario();
			            nuevo.setDesde(h.getDesde());
			            nuevo.setHasta(h.getHasta());
			            nuevo.setDia(h.getDia());
			            nuevo.setAsignacion(asig);
			            horarios.add(nuevo);
			        }
			        horarioRepo.saveAll(horarios); // Guardar los nuevos horarios en la base de datos
			        asig.setHorarios(horarios); // Actualizar la colección de horarios en la entidad asig
			        asig.getHorarios().forEach(h -> h.setAsignacion(asig)); // Establecer la nueva relación
			    }
			    //asig.setHorarios(horarios); // Reemplazar los horarios existentes en asig
			    System.out.println("LOS HORARIOS SON "+asig.getHorarios());
			    return asignacionRepo.save(asig);
			}else {
				return null;
			}
			
		} catch (NullPointerException | EntityNotFoundException ex) {
			// manejar la excepción aquí
			ex.printStackTrace(); // imprimir el seguimiento de la pila para fines de depuración
			return null;
		}
	}
	
	
	/*
	@PostMapping("/datos-asignacion")
	public AsignacionRecursoTipoTurno setDatosAsignacion(@RequestBody AsignacionRecursoTipoTurno asignacion) {
		AsignacionRecursoTipoTurno a = asignacionRepo.getById(asignacion.getId());
		a.setCantidadConcurrencia(a.getCantidadConcurrencia());
		a.setDuracionEnMinutos(a.getDuracionEnMinutos());
		a.setPrecioEstimadoDesdeCtvos(a.getPrecioEstimadoDesdeCtvos());
		a.setPrecioEstimadoHastaCtvos(a.getPrecioEstimadoHastaCtvos());
		a.setSeniaCtvos(a.getSeniaCtvos());
		return asignacionRepo.save(a);
	}*/
	
	
	@PostMapping("/recursos/{id}/eliminado")
	public Recurso habilitarRecurso(@PathVariable int id,@RequestBody JsonNode j) throws Exception {
		try {
			Recurso rec = repo.getById((long)id);
			rec.setEliminado(j.get("eliminado").asBoolean());
			return repo.save(rec);
		}catch(Exception e) {
			System.out.println(e);
			throw new Exception("Hubo un problema al cambiar el estado del Recurso");
		}
	}
	
	
	@PostMapping("/tipos-de-turno/{id}/eliminado")
	public TipoTurno habilitarTipoTurno(@PathVariable int id,@RequestBody JsonNode j) throws Exception {
		try {
			TipoTurno tt = tipoTurnoRepo.getById((long)id);
			tt.setEliminado(j.get("eliminado").asBoolean());
			return tipoTurnoRepo.save(tt);
		}catch(Exception e) {
			System.out.println(e);
			throw new Exception("Hubo un problema al cambiar el estado del Tipo de Turno");
		}
	}
	
	
	
	/*-------------------------------------------------------------------------------*/
	/*----------------------------cosas de asignaciones------------------------------*/
	/*-------------------------------------------------------------------------------*/
	@PostMapping("/recursos/{id}/asignaciones")
	@Transactional
	public void actualizarAsignacionesDeRecurso(@PathVariable int id, @RequestBody int[] idTiposDeTurno) {
		asignar();
		boolean seAsigno = false;
		Recurso r = repo.getById((long) id);
		for(TipoTurno t : tipoTurnoRepo.findAll()) {
			AsignacionRecursoTipoTurno asig = obtenerAsignacion(r,t);
			seAsigno =false;
			for (int i = 0; i < idTiposDeTurno.length; i++) {
				if(t.getId()==(long)idTiposDeTurno[i]) {
					asig.setEliminado(false);
					asignacionRepo.save(asig);
					seAsigno=true;
				}
			}
			if(!seAsigno) {
				asig.setEliminado(true);
				asignacionRepo.save(asig);
				seAsigno=false;
			}
		}
	}
	
	@PostMapping("/tipos-de-turno/{id}/asignaciones")
	@Transactional
	public void actualizarAsignacionesDeTipoTurno(@PathVariable int id, @RequestBody int[] idRecursos) {
		boolean seAsigno =false;
		TipoTurno t = tipoTurnoRepo.getById((long) id);
		for(Recurso r : repo.findAll()) {
			AsignacionRecursoTipoTurno asig = obtenerAsignacion(r,t);
			seAsigno =false;
			for (int i = 0; i < idRecursos.length; i++) {
				if(r.getId()==(long)idRecursos[i]) {
					asig.setEliminado(false);
					asignacionRepo.save(asig);
					seAsigno=true;
				}
			}
			if(!seAsigno) {
				asig.setEliminado(true);
				asignacionRepo.save(asig);
				seAsigno=false;
			}
		}
	}

	
	@PostMapping("/horarios-asignacion/{id}")
	public void setHorariosAsignacion(@PathVariable long id,@RequestBody Horario[] listaHorarios) {
		
		Optional<AsignacionRecursoTipoTurno> asignacion = asignacionRepo.findById(id);
		if(asignacion.isPresent()) {
			AsignacionRecursoTipoTurno a = asignacion.get();
			for (Horario h : a.getHorarios()) {
	            horarioRepo.delete(h); // Eliminar los horarios antiguos de la base de datos
	        }
			a.getHorarios().clear(); // Eliminar los horarios existentes en asig
			Set<Horario> horarios = new HashSet<>();
			for (Horario h : listaHorarios) {
	            System.out.println("UN HORARIO QUE ME PASASTE ES DE "+h.getDesde()+" A "+h.getHasta()+" - "+h.getDia());
	            Horario nuevo = new Horario();
	            nuevo.setDesde(h.getDesde());
	            nuevo.setHasta(h.getHasta());
	            nuevo.setDia(h.getDia());
	            nuevo.setAsignacion(a);
	            horarios.add(nuevo);
	        }
			horarioRepo.saveAll(horarios); // Guardar los nuevos horarios en la base de datos
	        a.setHorarios(horarios); // Actualizar la colección de horarios en la entidad asig
	        a.getHorarios().forEach(h -> h.setAsignacion(a)); // Establecer la nueva relación
	        asignacionRepo.save(a);
		}
	}
	
	@PostMapping("/horarios-especiales-asignacion/{id}")
	@Transactional
	public void setHorariosEspecialesAsignacion(@PathVariable long id,@RequestBody JsonNode[] listaHorarios) throws ParseException {
		System.out.println("Entre------------");
		
		Date fecha = new SimpleDateFormat("dd-MM-yyyy").parse(listaHorarios[0].get("fecha").asText());
		AsignacionRecursoTipoTurno a = asignacionRepo.getById(id);
		Set<HorarioEspecial> horariosAnteriores = a.getHorariosEspeciales();
		Set<HorarioEspecial> nuevos = new HashSet<>();
		for(JsonNode h : listaHorarios) {
			nuevos.add(armarHorarioDesdeJson(a,h));
		}
		horarioEspRepo.saveAll(nuevos);
		for(HorarioEspecial he : horariosAnteriores) {
			if(he.getFecha().compareTo(fecha) != 0) {
				nuevos.add(he);
			}else {
				horarioEspRepo.delete(he);
			}
		}
		a.getHorariosEspeciales().clear();
        a.setHorariosEspeciales(nuevos); // Actualizar la colección de horarios en la entidad asig
        asignacionRepo.save(a);
	}
	
	private HorarioEspecial armarHorarioDesdeJson(AsignacionRecursoTipoTurno a, JsonNode h) throws ParseException {
		HorarioEspecial esp = new HorarioEspecial();
		esp.setAsignacion(a);
		esp.setMotivo(h.get("motivo").asText());
		esp.setFecha(new SimpleDateFormat("dd-MM-yyyy").parse(h.get("fecha").asText()));
		esp.setCerrado(h.get("feriado").asBoolean());
		if(h.get("feriado").asBoolean()) {
			esp.setDesde(null);
			esp.setHasta(null);
		}else {
			esp.setDesde(Time.valueOf(LocalTime.parse(h.get("desde").asText())));
			esp.setHasta(Time.valueOf(LocalTime.parse(h.get("hasta").asText())));
		}
		return esp;
	}
	
	
	
	@PostMapping("/registrar-feriado/{fecha}")
	public void registrarFeriado(@PathVariable String fecha,@RequestBody long[] asignaciones) {
		System.out.println("ENTRE");
        try {
    		//transformar fecha
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date date = formatter.parse(fecha);
            
            //antes le voy a restar un dia porque el metodo siguiente le suma un dia siempre
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            date = calendar.getTime();
            
            System.out.println("#########  FECHA TRANSFORMADA  ########");
            System.out.println(date);// - 1 dia
            System.out.println("#######################################");
            
    		HorarioEspecial feriado = new HorarioEspecial();
    		feriado.setCerrado(true);
    		feriado.setDesde(null);
    		feriado.setHasta(null);
    		feriado.setFecha(date);
            
    		HorarioEspecial[] horarios = {feriado};
    		
    		Set<AsignacionRecursoTipoTurno> listaAsig = new HashSet<>();
    		for(long i : asignaciones) {
    			System.out.println("HOLA "+i);
    			listaAsig.add(asignacionRepo.getById(i));
    		}
    		for(AsignacionRecursoTipoTurno a : listaAsig) {
    			//setHorariosEspecialesAsignacion(a.getId(),horarios);
    		}
    		
        } catch (ParseException e) {
            e.printStackTrace();
        }
	}
	
	
	
	@PostMapping("/datos-asignacion")
	public AsignacionRecursoTipoTurno datosAsignacion(@RequestBody JsonNode a) {
		AsignacionRecursoTipoTurno asig = asignacionRepo.getById(a.get("id").asLong());
		asig.setCantidadConcurrencia(a.get("cantidadConcurrencia").asInt());
		asig.setDuracionEnMinutos(a.get("duracionEnMinutos").asInt());
		asig.setEliminado(a.get("eliminado").asBoolean());
		asig.setSeniaCtvos(a.get("senia").asInt());
		asig.setPrecioEstimadoDesdeCtvos(a.get("precioDesde").asInt());
		asig.setPrecioEstimadoHastaCtvos(a.get("precioHasta").asInt());
		return asignacionRepo.save(asig);
	}
	
	/*-------------------------------------------------------------------------------------------------------------------*/
	/*------------------------------------------------COSAS QUE NO SON DE WEB--------------------------------------------*/
	/*-------------------------------------------------------------------------------------------------------------------*/
	
	
	private void asignar() {//nose como hacerlo proceso automatico
		for(TipoTurno t : tipoTurnoRepo.findAll()) {
			for(Recurso r : repo.findAll()) {
				if(!crearAsignacionSiNoExiste(t,r)) {
					System.out.println("HUBO UN ERROR AL CREAR LAS ASIGNACIONES AUTOMATICAMENTE");
				}
			}
		}
		System.out.println("SE ASIGNARON");
	}
	
	private boolean crearAsignacionSiNoExiste(TipoTurno t, Recurso r) {
		if(asignacionRepo.findByRecursoAndTipoTurno(r, t)==null) {
			return crearAsignacion(r,t,true)!=null;
		}
		return true;
	}
	
	private AsignacionRecursoTipoTurno obtenerAsignacion(Recurso r, TipoTurno t) {
		for(AsignacionRecursoTipoTurno a : asignacionRepo.findAll()) {
			if(a.getRecurso().getId()==r.getId() && a.getTipoTurno().getId() == t.getId()) {
				return a;
			}
		}
		return null;
	}
	
	
	
	private AsignacionRecursoTipoTurno crearAsignacion(Recurso r, TipoTurno t, boolean eliminado) {
	    AsignacionRecursoTipoTurno a = new AsignacionRecursoTipoTurno();
	    a.setRecurso(r);
	    a.setTipoTurno(t);
	    a.setEliminado(eliminado);
	    Set<Horario> horarios = new HashSet<>();
	    
	    
	    System.out.println("VOY A GUARDAR LA ASIGNACION R:"+r.getId()+" - T:"+t.getId());
	    AsignacionRecursoTipoTurno asignacion = asignacionRepo.save(a);
	    
	    
	    
	    for (Horario h : r.getHorarios()) {
	        Hibernate.initialize(h);
	        Horario nuevo = new Horario();
	        nuevo.setDesde(h.getDesde());
	        nuevo.setHasta(h.getHasta());
	        nuevo.setDia(h.getDia());
	        nuevo.setAsignacion(asignacion);
	        horarios.add(nuevo);
	    }
	    
	    if(!horarios.isEmpty()) {
	    	for(Horario h:horarios) {
	    		System.out.println(h.getDia().name());
	    		System.out.println(h.getDesde());
	    		System.out.println(h.getHasta());
	    		horarioRepo.save(h);
	    		System.out.println("guarde el primero");
	    	}
		    //horarioRepo.saveAll(horarios);
		    System.out.println("2");
		    Hibernate.initialize(asignacion.getHorarios());
		    //a.getHorarios().clear();
		    asignacion.setHorarios(new HashSet<>(horarios));
		    System.out.println("3");
	    }
	    
	    //System.out.println("VOY A GUARDAR LA ASIGNACION R:"+r.getId()+" - T:"+t.getId());
	    return asignacion;
	}


}
