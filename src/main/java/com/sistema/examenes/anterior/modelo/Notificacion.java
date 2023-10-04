package com.sistema.examenes.anterior.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sistema.examenes.modelo.usuario.Usuario;

@Entity
@Table
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private boolean visto = false;
	private String titulo;
	private String mensaje;
	private LocalDate fecha = LocalDate.now();
	private LocalTime hora = LocalTime.now();
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
	private Usuario usuario;
	
	public Notificacion(String titulo, String mensaje, Usuario usuario) {
		this.titulo = titulo;
		this.mensaje = mensaje;
		this.usuario = usuario;
	}

	public boolean isVisto() {
		return visto;
	}

	public void setVisto(boolean visto) {
		this.visto = visto;
	}

	public String getTitulo() {
		return titulo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}



	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
