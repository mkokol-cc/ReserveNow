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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author MATIAS
 * @version 1.0
 * @created 17-ene-2023 02:55:42 p.m.
 */
@Entity
@Table(name = "HorarioEspecial")
public class HorarioEspecial {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "desde", nullable=true)
	private LocalTime desde;
	
	@Column(name = "hasta", nullable=true)
	private LocalTime hasta;
	
	@Column(name = "cerrado")
	private boolean cerrado;
	
	@Column(name = "fecha",nullable = false)
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate fecha;
	
	@Column(name = "motivo", nullable=true)
	private String motivo;
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asignacion_id")
    @JsonIgnore
    private AsignacionRecursoTipoTurno asignacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recurso_id")
    //@JsonBackReference
    @JsonIgnore
    private Recurso recurso;
	
	public HorarioEspecial(){

	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}





	public LocalTime getDesde() {
		return desde;
	}


	public void setDesde(LocalTime desde) {
		this.desde = desde;
	}


	public LocalTime getHasta() {
		return hasta;
	}


	public void setHasta(LocalTime hasta) {
		this.hasta = hasta;
	}


	public LocalDate getFecha() {
		return fecha;
	}


	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}


	public String getMotivo() {
		return motivo;
	}


	public boolean isCerrado() {
		return cerrado;
	}


	public void setCerrado(boolean cerrado) {
		this.cerrado = cerrado;
	}


	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}


	public AsignacionRecursoTipoTurno getAsignacion() {
		return asignacion;
	}


	public void setAsignacion(AsignacionRecursoTipoTurno asignacion) {
		this.asignacion = asignacion;
	}

	

	public Recurso getRecurso() {
		return recurso;
	}


	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}


	public void finalize() throws Throwable {

	}
}//end Horario