package com.sistema.examenes.anterior.modelo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sistema.examenes.modelo.usuario.Usuario;

/**
 * @author MATIAS
 * @version 1.0
 * @created 17-ene-2023 02:55:46 p.m.
 */
@Entity
@Table(name = "tipoTurno")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TipoTurno {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "eliminado",nullable=true)
	private boolean eliminado;
	
	@Column(name = "nombre",length=100,nullable=false)
	private String nombre;
	
	@Column(name = "descripcion")
	private String descripcion;
	

	@OneToMany(mappedBy = "tipoTurno", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<AsignacionRecursoTipoTurno> recursosTipoTurno;

	
	//RELACIONADO CON EL DUEÑO
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuario", referencedColumnName = "id", nullable = false, unique = false)
	@JsonBackReference
    private Usuario usuario;
	
	public TipoTurno(){

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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



	public boolean isEliminado() {
		return eliminado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}

	public List<AsignacionRecursoTipoTurno> getRecursosTipoTurno() {
		return recursosTipoTurno;
	}

	public void setRecursosTipoTurno(List<AsignacionRecursoTipoTurno> recursosTipoTurno) {
		this.recursosTipoTurno = recursosTipoTurno;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void finalize() throws Throwable {

	}
}//end TipoTurno