package com.sistema.examenes.anterior.modelo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author MATIAS
 * @version 1.0
 * @created 17-ene-2023 02:55:41 p.m.
 */
@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Estado {
	@Id
	private long id;
	
	@Column(name = "nombre",nullable=false)
	public String nombre;
	
	
	@Column(name = "descripcion",nullable=true)
	public String descripcion;
	
	@OneToMany(mappedBy="estado")
	@Column(name = "reservas",nullable=true)
	@JsonBackReference
	@JsonIgnore
	public List<Reserva> reservas;
	
	@OneToMany(mappedBy="estado")
	@Column(name = "cambiosDeEstado",nullable=true)
	@JsonBackReference
	@JsonIgnore
	public List<Reserva> cambiosDeEstado;
	
	@Column(name = "esEstadoFinal")
	public boolean esEstadoFinal;
	
	public long getId() {
		return id;
	}

	public boolean isEsEstadoFinal() {
		return esEstadoFinal;
	}

	public void setEsEstadoFinal(boolean esEstadoFinal) {
		this.esEstadoFinal = esEstadoFinal;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Estado(){

	}

	public void finalize() throws Throwable {

	}/*
	public void cancelarReserva(){
		
	}

	public void confirmarReserva(){

	}

	public void darDeBajaReserva(){

	}

	public void postergarTurno(){

	}

	public void registrarReserva(){

	}

	public boolean sosCancelable(){
		return false;
	}

	public boolean sosReservado(){
		return false;
	}

	public void verificarHoraReserva(){

	}*/
}//end Estado