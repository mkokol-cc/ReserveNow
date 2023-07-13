package com.sistema.examenes.anterior.modelo;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "estadoAnterior", referencedColumnName = "id", nullable = false)
	//@JsonManagedReference
	private Estado estadoAnterior;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "estadoNuevo", referencedColumnName = "id", nullable = false)
	//@JsonManagedReference
	private Estado estadoNuevo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reserva", referencedColumnName = "id", nullable = false)
	//@JsonManagedReference
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
