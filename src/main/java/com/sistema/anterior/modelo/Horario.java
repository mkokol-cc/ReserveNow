package com.sistema.anterior.modelo;

import java.time.LocalTime;
import java.util.ArrayList;
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
import javax.validation.constraints.NotNull;

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
	
	@NotNull(message = "Debes ingresar la hora de inicio (desde).")
	@Column(name = "desde", nullable=false)
	private LocalTime desde;
	
	@NotNull(message = "Debes ingresar la hora de inicio (hasta).")
	@Column(name = "hasta", nullable=false)
	private LocalTime hasta;
	
	@NotNull(message = "Debes ingresar el día.")
	@Column(name = "dia", nullable=false)
	private Dias dia;
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recurso_id")
    //@JsonBackReference
    @JsonIgnore
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
	
	
	//VALIDACIONES
	
	@AssertTrue(message = "La hora desde debe ser mayor que la hora hasta.")
	private boolean isDesdeMenorQueHoraHasta() {
		return this.desde.isBefore(this.hasta);
	}
	
	@AssertTrue(message = "El horario debe tener al menos 60 minutos.")
	private boolean is60Minutos() {
		return !this.desde.plusMinutes(60L).isAfter(this.hasta);
	}
	
	@AssertTrue(message = "El horario debe estar asociado a un recurso o a una asignacion.")
	private boolean estaAsociado() {
		return (this.asignacion != null || this.recurso != null) 
				&& !(this.asignacion != null && this.recurso != null);
	}
	
	@AssertFalse(message = "El horario se pisa con otros horarios ya registrados.")
	private boolean sePisaConAlgunHorarioRegistrado() {
		Set<Horario> horarios = this.asignacion != null ? this.asignacion.getHorarios() : this.recurso.getHorarios();
		return sePisaConAlgunoDeEstos(horarios);
	}
	
	
	
	public boolean sePisaConAlgunoDeEstos(Set<Horario> horarios) {
		boolean sePisa = false;
		for(Horario h : horarios) {
			if(!(this.hasta.isBefore(h.getDesde()) || this.hasta.equals(h.getDesde())) 
					|| !(this.desde.isAfter(h.getHasta()) || this.hasta.equals(h.getDesde())) ) {
				sePisa = true;
			}
		}
		return sePisa;
	}
	
	public boolean tieneLosDatosMinimos() {
		if(this.desde != null && this.hasta != null && this.dia != null) {
			return true;
		}
		return false;
	}
	
	public boolean sonValidosLosDatos() {
		return validarHorarios();
	}
	
	public boolean sePisaConAlgunoDeEstos(List<Horario> horarios) {
		boolean sePisa = false;
		for(Horario h : horarios) {
			if(!(this.hasta.isBefore(h.getDesde()) || this.hasta.equals(h.getDesde())) 
					|| !(this.desde.isAfter(h.getHasta()) || this.hasta.equals(h.getDesde())) ) {
				sePisa = true;
			}
		}
		return sePisa;
	}
	
	private boolean validarHorarios(){
		return this.desde.isBefore(this.hasta);
	}
	

	
	public List<LocalTime> obtenerHorarioCadaXMinutos(int minutos){
		List<LocalTime> listaHorarios = new ArrayList<>();
		LocalTime aux = this.getDesde();
		//mientras el auxiliar + la duracion no supere el horarioHasta
		while(!aux.plusMinutes((long) minutos).isAfter(this.getHasta())) {
			listaHorarios.add(aux);
			aux = aux.plusMinutes((long) minutos);
		}
		return listaHorarios;
	}
	
}//end Horario