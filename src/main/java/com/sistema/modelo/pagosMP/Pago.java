package com.sistema.modelo.pagosMP;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sistema.modelo.usuario.Usuario;

@Entity
@Table(name = "pago")
public class Pago {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
	private LocalDate fecha;
	private LocalDate fechaVto;
	private int monto;
	private int estado;//0 ptePago 1 pagado 2 rechazado
	
	@Column(nullable=true)
	private String mpPreferenceId;
	@Column(nullable=true)
	private String linkPago;
	/*
	@Column(nullable=true)
	private String mpApplicationId;
	*/
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "licencia_id")
	@JsonIgnore
	private Licencia licencia;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
	@JsonIgnore
	private Usuario usuario;

	
	public Pago() {}
	public Pago(Long id, LocalDate fecha, int monto, Licencia licencia, Usuario usuario) {
		this.id = id;
		this.fecha = fecha;
		this.monto = monto;
		this.licencia = licencia;
		this.usuario = usuario;
	}
	
	

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

	public int getMonto() {
		return monto;
	}

	public void setMonto(int monto) {
		this.monto = monto;
	}

	public Licencia getLicencia() {
		return licencia;
	}

	public void setLicencia(Licencia licencia) {
		this.licencia = licencia;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public int getEstado() {
		return estado;
	}
	
	public void setEstado(int estado) {
		this.estado = estado;
	}

	public LocalDate getFechaVto() {
		return fechaVto;
	}
	
	public void setFechaVto(LocalDate fechaVto) {
		this.fechaVto = fechaVto;
	}
	public String getMpPreferenceId() {
		return mpPreferenceId;
	}
	public void setMpPreferenceId(String mpPreferenceId) {
		this.mpPreferenceId = mpPreferenceId;
	}
	public String getLinkPago() {
		return linkPago;
	}
	public void setLinkPago(String linkPago) {
		this.linkPago = linkPago;
	}
}
