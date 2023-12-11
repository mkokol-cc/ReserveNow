package com.sistema.anterior.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sistema.modelo.usuario.Usuario;

@Entity
@Table(name = "AsignacionRecursoTipoTurno", uniqueConstraints = @UniqueConstraint(columnNames = {"id_recurso", "id_tipoTurno"}))
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AsignacionRecursoTipoTurno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Debes seleccionar el recurso.")
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_recurso")
    private Recurso recurso;

    @NotNull(message = "Debes seleccionar el tipo de turno.")
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_tipoTurno")
    //@JsonManagedReference
    //@JsonBackReference
    private TipoTurno tipoTurno;
    
    @OneToMany(mappedBy = "asignacion", fetch = FetchType.EAGER/*, cascade = CascadeType.ALL, orphanRemoval = true*/)
    @JsonIgnore
    private Set<Horario> horarios = new HashSet<>();
	
    @OneToMany(mappedBy = "asignacion", fetch = FetchType.EAGER/*, cascade = CascadeType.ALL, orphanRemoval = true*/)
    @JsonIgnore
	private Set<HorarioEspecial> horariosEspeciales = new HashSet<>();

    @Min(value = 1, message = "La concurrencia debe ser de al menos 1 persona.")
	@Column(name = "cantidadConcurrencia",nullable=false)
	private int cantidadConcurrencia;
	
	@Column(name = "eliminado")
	private boolean eliminado;
	
	/*
	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "asignacion_recurso_tipo_turno_simultaneo",
	    joinColumns = @JoinColumn(name = "id_asignacion_recurso_tipo_turno"),
	    inverseJoinColumns = @JoinColumn(name = "id_asignacion_recurso_tipo_turno_simultaneo"))
	@JsonIgnore
	private Set<AsignacionRecursoTipoTurno> simultaneos = new HashSet<>();//un recurso especifico puede tener tipos de turnos que sean simultaneos
	*/
	@OneToMany(mappedBy="asignacionTipoTurno")
	@Column(name = "reservas",nullable=true)
	@JsonIgnore
	//@JsonManagedReference
	//@JsonBackReference
	public Set<Reserva> reservas;
	
	@Min(value = 10, message = "La duración debe ser al menos 10 minutos.")
	@Max(value = 1440, message = "La duración no puede ser mayor de 1440 minutos (24 horas).")
	@Column(name = "duracionEnMinutos",nullable=false)
	private Integer duracionEnMinutos;
	
	@Min(value = 0, message = "La seña no puede ser negativa.")
	@Column(name = "seniaCtvs",nullable=true)
	private Integer seniaCtvos;
	
	@Min(value = 0, message = "El precio estimado desde no puede ser negativa.")
	@Column(name = "precioEstimadoDesdeCtvos",nullable=true)
	private Integer precioEstimadoDesdeCtvos;
	
	@Min(value = 0, message = "El precio estimado hasta no puede ser negativa.")
	@Column(name = "precioEstimadoHastaCtvos",nullable=true)
	private Integer precioEstimadoHastaCtvos;

	public AsignacionRecursoTipoTurno(Recurso r, TipoTurno t) {
		this.cantidadConcurrencia = 1;
		this.duracionEnMinutos = t.getDuracionEnMinutos();
		this.eliminado = false;
		//this.horarios
		//this.horariosEspeciales
		this.precioEstimadoDesdeCtvos = t.getPrecioEstimadoDesdeCtvos();
		this.precioEstimadoHastaCtvos = t.getPrecioEstimadoHastaCtvos();
		this.recurso = r;
		this.seniaCtvos = t.getSeniaCtvos();
		this.tipoTurno = t;
	}
	
	public AsignacionRecursoTipoTurno() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public TipoTurno getTipoTurno() {
		return tipoTurno;
	}

	public void setTipoTurno(TipoTurno tipoTurno) {
		this.tipoTurno = tipoTurno;
	}

	public Set<Horario> getHorarios() {
		return horarios;
	}

	public void setHorarios(Set<Horario> horarios) {
		this.horarios = horarios;
	}

	public Set<HorarioEspecial> getHorariosEspeciales() {
		return horariosEspeciales;
	}

	public void setHorariosEspeciales(Set<HorarioEspecial> horariosEspeciales) {
		this.horariosEspeciales = horariosEspeciales;
	}

	public int getCantidadConcurrencia() {
		return cantidadConcurrencia;
	}

	public void setCantidadConcurrencia(int cantidadConcurrencia) {
		this.cantidadConcurrencia = cantidadConcurrencia;
	}
	/*
	public Set<AsignacionRecursoTipoTurno> getSimultaneos() {
		return simultaneos;
	}

	public void setSimultaneos(Set<AsignacionRecursoTipoTurno> simultaneos) {
		this.simultaneos = simultaneos;
	}
	 */
	public Set<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(Set<Reserva> reservas) {
		this.reservas = reservas;
	}

	public boolean isEliminado() {
		return eliminado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}

	public Integer getDuracionEnMinutos() {
		return duracionEnMinutos != null ? duracionEnMinutos.intValue() : 0;
	}

	public void setDuracionEnMinutos(int duracionEnMinutos) {
		this.duracionEnMinutos = duracionEnMinutos;
	}

	public int getSeniaCtvos() {
		return seniaCtvos;
	}

	public void setSeniaCtvos(int seniaCtvos) {
		this.seniaCtvos = seniaCtvos;
	}

	public int getPrecioEstimadoDesdeCtvos() {
		return precioEstimadoDesdeCtvos;
	}

	public void setPrecioEstimadoDesdeCtvos(int precioEstimadoDesdeCtvos) {
		this.precioEstimadoDesdeCtvos = precioEstimadoDesdeCtvos;
	}

	public int getPrecioEstimadoHastaCtvos() {
		return precioEstimadoHastaCtvos;
	}

	public void setPrecioEstimadoHastaCtvos(int precioEstimadoHastaCtvos) {
		this.precioEstimadoHastaCtvos = precioEstimadoHastaCtvos;
	}
	
	
	@AssertTrue(message="Recurso inhabilitado.")
	private boolean isHabilitadoRecurso() {
		return !this.recurso.isEliminado();
	}
	@AssertTrue(message="Tipo de Turno inhabilitado.")
	private boolean isHabilitadoTipoTurno() {
		return !this.tipoTurno.isEliminado();
	}
	
	//validar cantidadConcurrencia
	
	public boolean tieneLosDatosMinimos() {
		return(this.tipoTurno!=null && this.recurso!=null);
	}
	
	public boolean sonValidosLosDatos() {
		return (validarConcurrencia() && validarDuracion() && validarPrecioEstimado() && validarSenia() && validarUsuarios());
	}
	
	private boolean validarConcurrencia(){
		return (getCantidadConcurrencia()>=0);
	}
	
	private boolean validarDuracion(){
		return (getDuracionEnMinutos()>0);
	}
	
	private boolean validarUsuarios() {
		return (this.recurso.getUsuario()==this.tipoTurno.getUsuario());
	}
	
	private boolean validarPrecioEstimado() {
		return (this.precioEstimadoDesdeCtvos != null && 
				this.precioEstimadoHastaCtvos != null &&
				this.precioEstimadoDesdeCtvos >= 0 &&
				this.precioEstimadoDesdeCtvos <= this.precioEstimadoHastaCtvos);
	}
	
	private boolean validarSenia() {
		return (this.seniaCtvos == null && this.seniaCtvos >= 0);
	}
	
	
	
	
	
	
	public List<LocalTime> turnosParaLaFecha(LocalDate fecha){
		List<LocalTime> times = new ArrayList<>();
		HorarioEspecial horarioEspecial = obtenerHorarioEspecialPorFecha(fecha);
		for(Horario h : obtenerTurnosPorDia(fecha)) {
			times.addAll(h.obtenerHorarioCadaXMinutos(this.duracionEnMinutos));
		}
		if(horarioEspecial != null) {
			for(LocalTime t : times) {
				if(t.isBefore(horarioEspecial.getDesde()) || 
						t.plusMinutes(this.duracionEnMinutos).isAfter(horarioEspecial.getHasta())) {
					times.remove(t);
				}
			}
		}
		return times;
	}
	
	
	private HorarioEspecial obtenerHorarioEspecialPorFecha(LocalDate fecha) {
		for(HorarioEspecial he : this.horariosEspeciales) {
			if(he.getFecha().isEqual(fecha)) {
				return he; 
			}
		}
		return null;
	}
	
	private List<Horario> obtenerTurnosPorDia(LocalDate fecha) {
		List<Horario> horarios = new ArrayList<>();
		Dias d = Dias.valueOf(fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase());
		for(Horario h : this.horarios) {
			if(h.getDia().equals(d)) {
				horarios.add(h);
			}
		}
		return horarios;
	}
	
	
	
	
	
	
	
	
	
	public List<Reserva> obtenerReservasPorFecha(LocalDate fecha){
		List<Reserva> reservas = new ArrayList<>();
		for(Reserva r : this.reservas) {
			if(r.getFechaHoraInicio().toLocalDate().isEqual(fecha)) {
				reservas.add(r);
			}
		}
		return reservas;
	}
	
	
	
	@AssertTrue(message="El precio desde debe ser menor o igual al precio hasta.")
	public boolean isValidaSenia() {
		if(this.precioEstimadoDesdeCtvos <= this.precioEstimadoHastaCtvos) {
			return true;
		}
		return false;
	}
	
	
	
	/*
	public boolean editarDuracion() {
		for(Reserva r : this.getReservas()) {
			if(r.getEstado().isProximo()) {
				r.setHoraFin(null);
			}
		}
	}
	
	public boolean reacomodarReservas() {
		for(Reserva r : this.getReservas()) {
			if(r.getEstado().isProximo()) {
				
			}
		}
	}*/
	
	public Map<LocalTime, Boolean> getHorariosDisponibles(LocalDate fecha){		
		List<Reserva> reservas = this.getRecurso().obtenerReservasPorFecha(fecha);
		List<Horario> horarios = this.getRecurso().getHorariosPorFecha(fecha);
		List<HorarioEspecial> horariosEspeciales = this.getRecurso().getHorariosEspecialesPorFecha(fecha);
		if(horariosEspeciales.isEmpty()) {
			return armarMapTurnosDisponiblesPorHorarios(horarios,reservas);
		}
		return armarMapTurnosDisponiblesPorHorariosEspeciales(horariosEspeciales,reservas);
	}
	
	private Map<LocalTime, Boolean> armarMapTurnosDisponiblesPorHorarios(List<Horario> horarios, List<Reserva> reservas){
		Map<LocalTime, Boolean> turnos = new HashMap<>();
		for(Horario h : horarios) {
			turnos.putAll(armarMapTurnosDisponibles(h.getDesde(),h.getHasta(),reservas));
		}
		return turnos;
	}
	private Map<LocalTime, Boolean> armarMapTurnosDisponiblesPorHorariosEspeciales(List<HorarioEspecial> horarios, List<Reserva> reservas){
		Map<LocalTime, Boolean> turnos = new HashMap<>();
		for(HorarioEspecial h : horarios) {
			if(h.isCerrado()) {
				return null;
			}
			turnos.putAll(armarMapTurnosDisponibles(h.getDesde(),h.getHasta(),reservas));
		}
		return turnos;
	}
	
	private Map<LocalTime, Boolean> armarMapTurnosDisponibles(LocalTime desde, LocalTime hasta, List<Reserva> reservas){
		Map<LocalTime, Boolean> turnos = new HashMap<>();
		while(desde.isBefore(hasta)) {
			LocalTime finTurno = desde.plusMinutes(this.getDuracionEnMinutos());
			turnos.put(desde, comprobarDisponibilidadTurno(reservas, desde, finTurno));
			desde = finTurno;
		}
		return turnos;
	}
	
	private Boolean comprobarDisponibilidadTurno(List<Reserva> reservas, LocalTime horaInicio, LocalTime horaFin) {
		List<Reserva> reservasDelRango = reservas.stream().filter(r -> 
		(!r.getFechaHoraInicio().toLocalTime().isBefore(horaInicio) && r.getFechaHoraInicio().toLocalTime().isBefore(horaFin)) || 
		(!r.getFechaHoraFin().toLocalTime().isAfter(horaFin) && r.getFechaHoraInicio().toLocalTime().isAfter(horaInicio))).collect(Collectors.toList());
		
		boolean sonDeEstaAsignacion = reservasDelRango.stream().allMatch(r -> r.getAsignacionTipoTurno().equals(this));
		
		return (sonDeEstaAsignacion && reservasDelRango.size()<this.cantidadConcurrencia) || reservasDelRango.isEmpty();
	}
	
	
	
	public List<LocalTime> getHorariosTurnos(LocalDate fecha){
		List<Horario> horarios = this.getRecurso().getHorariosPorFecha(fecha);
		List<HorarioEspecial> horariosEspeciales = this.getRecurso().getHorariosEspecialesPorFecha(fecha);
		if(horariosEspeciales.isEmpty()) {
			return getHorariosTurnosPorHorariosComunes(horarios);
		}
		return getHorariosTurnosPorHorariosEspeciales(horariosEspeciales);
	}
	
	
	private List<LocalTime> getHorariosTurnosPorHorariosComunes(List<Horario> horariosComunes){
		List<LocalTime> horarios = new ArrayList<>();
		for(Horario h : horariosComunes) {
			horarios.addAll(getHorariosTurnos(h.getDesde(),h.getHasta()));
		}
		return horarios;
	}
	private List<LocalTime> getHorariosTurnosPorHorariosEspeciales(List<HorarioEspecial> horariosEspeciales){
		List<LocalTime> horarios = new ArrayList<>();
		for(HorarioEspecial h : horariosEspeciales) {
			if(h.isCerrado()) {
				return null;
			}
			horarios.addAll(getHorariosTurnos(h.getDesde(),h.getHasta()));
		}
		return horarios;
	}
	private List<LocalTime> getHorariosTurnos(LocalTime desde, LocalTime hasta){
		List<LocalTime> horarios = new ArrayList<>();
		while(desde.isBefore(hasta)) {
			LocalTime finTurno = desde.plusMinutes(this.getDuracionEnMinutos());
			horarios.add(finTurno);
			desde = finTurno;
		}
		return horarios;
	}

	public AsignacionRecursoTipoTurno editarAsignacionRecursoTipoTurno(AsignacionRecursoTipoTurno asig) {
		this.cantidadConcurrencia = asig.getCantidadConcurrencia();
		this.duracionEnMinutos = asig.getDuracionEnMinutos();
		//this.eliminado = asig.isEliminado();
		//this.horarios
		//this.horariosEspeciales
		this.precioEstimadoDesdeCtvos = asig.getPrecioEstimadoDesdeCtvos();
		this.precioEstimadoHastaCtvos = asig.getPrecioEstimadoHastaCtvos();
		this.seniaCtvos = asig.getSeniaCtvos();
		return this;
	}
	
	
	/*IMPLEMENTACION 9.12*/
	public Map<LocalDateTime,Integer> obtenerTurnosLibres(){
		Map<LocalDateTime,Integer> grillaHorarios = new HashMap<>();
		Usuario u = getRecurso().getUsuario();
		LocalDateTime inicio = LocalDateTime.now();
		LocalDateTime fin = inicio.plusWeeks(u.getCantSemanasDeAntelacionParaReservar());
		while(fin.isAfter(inicio)) {
			for(LocalTime t : getHorariosTurnos(inicio.toLocalDate())) {
				grillaHorarios.put(LocalDateTime.of(inicio.toLocalDate(),t), getCantidadConcurrencia());
			}
			inicio.plusDays(1);
		}
		return modificarDisponibilidadGrilla(grillaHorarios);
	}
	
	public Map<LocalDateTime, Integer> modificarDisponibilidadGrilla(Map<LocalDateTime, Integer> grillaHorarios){
        for (Map.Entry<LocalDateTime, Integer> entry : grillaHorarios.entrySet()) {
        	int cantReservas = getRecurso().obtenerReservas().stream().filter(r -> r.isEsTurnoFijo()).collect(Collectors.toList()).size();
            grillaHorarios.put(entry.getKey(), entry.getValue() - cantReservas); 
        }
        return grillaHorarios;
	}

}
