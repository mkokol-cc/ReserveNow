package com.sistema.examenes.anterior.modelo;




import java.sql.Time;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
	private Time desde;
	
	@Column(name = "hasta", nullable=true)
	private Time hasta;
	
	@Column(name = "cerrado")
	private boolean cerrado;
	
	@Column(name = "fecha",nullable = false)
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date fecha;
	
	@Column(name = "motivo", nullable=true)
	private String motivo;
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asignacion_id")
    @JsonIgnore
    private AsignacionRecursoTipoTurno asignacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recurso_id")
    @JsonBackReference
    private Recurso recurso;
	
	public HorarioEspecial(){

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


	public Date getFecha() {
		return fecha;
	}


	public void setFecha(Date fecha) {
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