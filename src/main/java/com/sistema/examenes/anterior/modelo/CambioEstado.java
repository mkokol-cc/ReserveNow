package com.sistema.examenes.anterior.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "cambioEstado")
public class CambioEstado {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "fecha",nullable = false)
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate fecha;
	
	@Column(name = "hora",nullable = false)
	private LocalTime hora;
	
	@NotNull(message = "Debes ingresar el estado anterior.")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "estadoAnterior", referencedColumnName = "id", nullable = false)
	//@JsonManagedReference
	private Estado estadoAnterior;
	
	@NotNull(message = "Debes ingresar el estado actual.")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "estadoNuevo", referencedColumnName = "id", nullable = false)
	//@JsonManagedReference
	private Estado estadoNuevo;
	
	@NotNull(message = "Debes ingresar la reserva asociada.")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reserva", referencedColumnName = "id", nullable = false)
	@JsonIgnore
	private Reserva reserva;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Estado getEstadoAnterior() {
		return estadoAnterior;
	}

	public void setEstadoAnterior(Estado estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}

	public Estado getEstadoNuevo() {
		return estadoNuevo;
	}

	public void setEstadoNuevo(Estado estadoNuevo) {
		this.estadoNuevo = estadoNuevo;
	}

	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}
	
	
	
	
}
