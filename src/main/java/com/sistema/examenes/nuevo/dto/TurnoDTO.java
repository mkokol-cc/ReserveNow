package com.sistema.examenes.nuevo.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class TurnoDTO {

	private LocalDate fecha;
	private LocalTime hora;
	private int duracion;
	private LocalTime hasta;
	private boolean ocupado;
	
	public TurnoDTO(LocalDate fecha,LocalTime hora,int duracion,boolean ocupado) {
		this.fecha = fecha;
		this.hora = hora;
		this.duracion = duracion;
		this.hasta = hora.plusMinutes(duracion);
		this.ocupado = ocupado;
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

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public LocalTime getHasta() {
		return hasta;
	}

	public void setHasta(LocalTime hasta) {
		this.hasta = hasta;
	}

	public boolean isOcupado() {
		return ocupado;
	}

	public void setOcupado(boolean ocupado) {
		this.ocupado = ocupado;
	}
	
	
}
