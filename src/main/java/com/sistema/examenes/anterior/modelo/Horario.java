package com.sistema.examenes.anterior.modelo;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author MATIAS
 * @version 1.0
 * @created 17-ene-2023 02:55:42 p.m.
 */
@Entity
@Table(name = "horario")
//@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Horario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "desde", nullable=false)
	private Time desde;
	
	@Column(name = "hasta", nullable=false)
	private Time hasta;
	
	@Column(name = "dia", nullable=false)
	private Dias dia;
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recurso_id")
    @JsonBackReference
    private Recurso recurso;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asignacion_id")
    @JsonIgnore
    private AsignacionRecursoTipoTurno asignacion;

	
	public Horario(){

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Time getDesde() {
		return desde;
	}

	public void setDesde(Time desde) {
		this.desde = desde;
	}

	public Time getHasta() {
		return hasta;
	}

	public void setHasta(Time hasta) {
		this.hasta = hasta;
	}

	public Dias getDia() {
		return dia;
	}

	public void setDia(Dias dia) {
		this.dia = dia;
	}
	



	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public AsignacionRecursoTipoTurno getAsignacion() {
		return asignacion;
	}

	public void setAsignacion(AsignacionRecursoTipoTurno asignacion) {
		this.asignacion = asignacion;
	}

	public void finalize() throws Throwable {

	}
}//end Horario