package com.sistema.examenes.anterior.modelo;




import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

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
	
	@NotNull(message = "Debes ingresar si estará cerrado.")
	@Column(name = "cerrado")
	private boolean cerrado;
	
    @NotNull(message = "Debes ingresar una fecha.")
    @Future(message = "La fecha debe ser en el futuro.")
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
	
	//VALIDACIONES
	
	@AssertTrue(message = "El horario debe tener al menos 60 minutos.")
	private boolean isCantidadMinimaDeMinutos() {
		if(this.cerrado) {
			return true;
		}
		return !this.desde.plusMinutes(60L).isAfter(this.hasta);
	}
	
	@AssertTrue(message = "La hora desde debe ser mayor que la hora hasta.")
	private boolean isHoraDesdeMenorQueHoraHasta() {
		if(!this.cerrado) {
			return this.desde.isBefore(this.hasta);	
		}
		return true;
	}
	
	@AssertTrue(message = "La fecha debe ser mayor a la actual.")
	private boolean isValidaFecha() {
		return (this.fecha.isAfter(LocalDate.now()));
	}
	
	@AssertTrue(message = "El horario debe estar asociado a un recurso o a una asignacion.")
	private boolean estaAsociado() {
		return (this.asignacion != null || this.recurso != null) 
				&& !(this.asignacion != null && this.recurso != null);
	}
	
	@AssertFalse(message = "El horario se pisa con otros horarios ya registrados.")
	private boolean sePisaConAlgunHorarioRegistrado() {
		Set<HorarioEspecial> horarios = this.asignacion != null ? this.asignacion.getHorariosEspeciales() 
				: this.recurso.getHorariosEspeciales();
		return sePisaConAlgunoDeEstos(horarios);
	}
	
	
	
	public boolean tieneLosDatosMinimos() {
		if(this.fecha != null) {
			if(!this.cerrado) {
				if(this.desde == null || this.hasta == null) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean sonValidosLosDatos() {
		return validarHorarios() && isValidaFecha();
	}
	
	public boolean sePisaConAlgunoDeEstos(Set<HorarioEspecial> horarios) {
		boolean sePisa = false;
		for(HorarioEspecial h : horarios) {
			if(this.fecha.isEqual(h.getFecha())) {
				sePisa = true;
			}
		}
		return sePisa;
	}
	
	private boolean validarHorarios(){
		if(!this.cerrado) {
			return this.desde.isBefore(this.hasta);	
		}
		return true;
	}
	

}//end Horario