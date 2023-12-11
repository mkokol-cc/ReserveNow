package com.sistema.anterior.modelo;

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
	private String nombre;
	
	
	@Column(name = "descripcion",nullable=true)
	private String descripcion;
	
	@OneToMany(mappedBy="estado")
	@Column(name = "reservas",nullable=true)
	@JsonBackReference
	@JsonIgnore
	private List<Reserva> reservas;
	
	@OneToMany(mappedBy="estado")
	@Column(name = "cambiosDeEstado",nullable=true)
	@JsonBackReference
	@JsonIgnore
	private List<Reserva> cambiosDeEstado;
	
	@Column(name = "esEstadoFinal")
	private boolean esEstadoFinal;
	
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
	public Estado(String nombre, String descripcion, Long id, boolean esEstadoFinal){
		this.descripcion=descripcion;
		this.nombre=nombre;
		this.id=id;
		this.esEstadoFinal=esEstadoFinal;
	}

	public void finalize() throws Throwable {

	}
	
	public boolean esEstadoFinal() {
		return !(this.nombre.equals("Activa") || this.nombre.equals("Pendiente de Pago"));
	}
	public boolean esEstadoParaRecordatorio() {
		return this.nombre.equals("Activa");
	}
}//end Estado