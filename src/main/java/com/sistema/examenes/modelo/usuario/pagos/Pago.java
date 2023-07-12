package com.sistema.examenes.modelo.usuario.pagos;

import java.sql.Date;

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
import com.sistema.examenes.modelo.usuario.Usuario;

@Entity
@Table(name = "pago")
public class Pago {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
	private Date fecha;
	private Date fechaVto;
	private int monto;
	private int estado;
	
	@Column(nullable=true)
	private String mpPreferenceId;
	@Column(nullable=true)
	private String mpPayerId;
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
	public Pago(Long id, Date fecha, int monto, Licencia licencia, Usuario usuario) {
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
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

	public Date getFechaVto() {
		return fechaVto;
	}
	
	public void setFechaVto(Date fechaVto) {
		this.fechaVto = fechaVto;
	}
	public String getMpPreferenceId() {
		return mpPreferenceId;
	}
	public void setMpPreferenceId(String mpPreferenceId) {
		this.mpPreferenceId = mpPreferenceId;
	}
	public String getMpPayerId() {
		return mpPayerId;
	}
	public void setMpPayerId(String mpPayerId) {
		this.mpPayerId = mpPayerId;
	}
	
	

}
