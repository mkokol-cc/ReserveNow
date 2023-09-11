package com.sistema.examenes.anterior.modelo;

import java.util.HashSet;
import java.util.Set;

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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "AsignacionRecursoTipoTurno")
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
	
	
	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "asignacion_recurso_tipo_turno_simultaneo",
	    joinColumns = @JoinColumn(name = "id_asignacion_recurso_tipo_turno"),
	    inverseJoinColumns = @JoinColumn(name = "id_asignacion_recurso_tipo_turno_simultaneo"))
	@JsonIgnore
	private Set<AsignacionRecursoTipoTurno> simultaneos = new HashSet<>();//un recurso especifico puede tener tipos de turnos que sean simultaneos

	@OneToMany(mappedBy="asignacionTipoTurno")
	@Column(name = "reservas",nullable=true)
	@JsonIgnore
	//@JsonManagedReference
	//@JsonBackReference
	public Set<Reserva> reservas;
	
	@Min(value = 1, message = "La duración debe ser al menos 1 minuto.")
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

	public Set<AsignacionRecursoTipoTurno> getSimultaneos() {
		return simultaneos;
	}

	public void setSimultaneos(Set<AsignacionRecursoTipoTurno> simultaneos) {
		this.simultaneos = simultaneos;
	}

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
	
}
