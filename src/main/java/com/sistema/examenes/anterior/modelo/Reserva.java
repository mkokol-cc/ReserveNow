
package com.sistema.examenes.anterior.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * @author MATIAS
 * @version 1.0
 * @created 17-ene-2023 02:55:43 p.m.
 */

@Entity
@Table(name = "reserva")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
//@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "id")
public class Reserva {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",unique=true)
	private Long id;
	
	@Column(name="tipoReserva")
	private String tipoReserva;
	
	@NotNull(message = "La reserva debe tener un estado.")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "estado", referencedColumnName = "id", nullable = false, unique = false)
	private Estado estado;
	
	@NotNull(message = "Debes ingresar la fecha.")
	@Column(name = "fecha", nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate fecha;

	@NotNull(message = "Debes ingresar la hora.")
	@Column(name = "hora",nullable = false)
	private LocalTime hora;
	
	@NotNull(message = "Debes ingresar la hora de fin.")
	@Column(name = "horaFin",nullable = false)
	private LocalTime horaFin;

	@NotNull(message = "Debes seleccionar un recurso y tipo de turno.")	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "asignacionTipoTurno", referencedColumnName = "id", nullable = false, unique = false)
	private AsignacionRecursoTipoTurno asignacionTipoTurno;
	
	
	@OneToMany(mappedBy="reserva")
	@Column(name = "cambioEstado",nullable=true)
	public List<CambioEstado> cambioEstado;
	
	@Column(name="nota")
	private String nota;
	
	@NotNull(message = "Debes ingresar los datos de la persona que va a reservar.")	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "reservante", referencedColumnName = "id", nullable = false, unique = false)
	@JsonManagedReference
	private Reservante reservante;
	
	public Reserva(){

	}

	public void finalize() throws Throwable {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}



	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}
	/*
	public AsignacionRecursoTipoTurno getTipoTurno() {
		return asignacionTipoTurno;
	}

	public void setTipoTurno(AsignacionRecursoTipoTurno asignacionTipoTurno) {
		this.asignacionTipoTurno = asignacionTipoTurno;
	}*/

	public String getTipoReserva() {
		return tipoReserva;
	}

	public void setTipoReserva(String tipoReserva) {
		this.tipoReserva = tipoReserva;
	}

	public AsignacionRecursoTipoTurno getAsignacionTipoTurno() {
		return asignacionTipoTurno;
	}

	public void setAsignacionTipoTurno(AsignacionRecursoTipoTurno asignacionTipoTurno) {
		this.asignacionTipoTurno = asignacionTipoTurno;
	}

	public List<CambioEstado> getCambioEstado() {
		return cambioEstado;
	}

	public void setCambioEstado(List<CambioEstado> cambioEstado) {
		this.cambioEstado = cambioEstado;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public Reservante getReservante() {
		return reservante;
	}

	public void setReservante(Reservante reservante) {
		this.reservante = reservante;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}
	
	
	//VALIDACIONES

	public boolean tieneLosDatosMinimos() {
		if(this.fecha != null && this.asignacionTipoTurno != null && this.hora != null
				&& this.reservante != null && this.horaFin != null && this.estado != null) {
			return true;
		}
		return false;
	}
	
	public boolean sonValidosLosDatos() {
		return validarHorarios() && validarUsuario() && validarFecha();
	}
	
	private boolean validarUsuario() {
		return(this.asignacionTipoTurno.getRecurso().getUsuario() == this.reservante.getUsuario());
	}
	
	private boolean validarHorarios(){
		return (this.hora.isBefore(this.horaFin) || this.hora.equals(this.horaFin));
	}
	
	private boolean validarFecha() {
		if(this.fecha.isAfter(LocalDate.now())) {
			return true;
		}else {
			return (this.fecha.equals(LocalDate.now()) ? this.hora.isBefore(LocalTime.now()) : false);
		}
	}
	
	private boolean validarHorarioDeTurno() {
		for(Horario)
	}
	
}//end Reserva