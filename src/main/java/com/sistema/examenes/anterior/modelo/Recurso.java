package com.sistema.examenes.anterior.modelo;

import java.util.HashSet;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sistema.examenes.modelo.usuario.Usuario;

/**
 * @author MATIAS
 * @version 1.0
 * @created 17-ene-2023 02:55:43 p.m.
 */

@Entity
@Table(name = "recurso")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class Recurso {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String descripcion;
	
	@NotNull(message = "Debes ingresar el nombre.")
    @NotBlank(message = "El nombre no puede estar en blanco.")
	@Size(min=2,max=30, message = "El nombre debe tener entre 2 y 30 caracteres.")
	private String nombre;
	
	@OneToMany(mappedBy = "recurso"/*, cascade = CascadeType.ALL, orphanRemoval = true*/)
	@Fetch(FetchMode.SUBSELECT)
	@JsonIgnore
	private List<AsignacionRecursoTipoTurno> recursosTipoTurno;
	
	
    @OneToMany(mappedBy = "recurso", fetch = FetchType.EAGER)
    //@JsonManagedReference
    private Set<Horario> horarios = new HashSet<>();
    
    
    @OneToMany(mappedBy = "recurso", fetch = FetchType.EAGER)
    //@JsonManagedReference
    private Set<HorarioEspecial> horariosEspeciales = new HashSet<>();

	
	@Column(name = "eliminado",nullable=true)
	private boolean eliminado;

	//RELACIONADO CON EL DUEÑO
	@NotNull(message = "El usuario no puede ser nulo.")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuario", referencedColumnName = "id", nullable = false, unique = false)
	//@JsonBackReference
	@JsonIgnore
    private Usuario usuario;
	
	public Recurso(){

	}
	
	public Recurso(String nombre, String descripcion){
		this.nombre=nombre;
		this.descripcion=descripcion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
	
	public List<AsignacionRecursoTipoTurno> getRecursosTipoTurno() {
		return recursosTipoTurno;
	}

	public void setRecursosTipoTurno(List<AsignacionRecursoTipoTurno> recursosTipoTurno) {
		this.recursosTipoTurno = recursosTipoTurno;
	}


	public Set<Horario> getHorarios() {
		return horarios;
	}

	public void setHorarios(Set<Horario> horarios) {
		this.horarios = horarios;
	}

	public boolean isEliminado() {
		return eliminado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}
	
	

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Set<HorarioEspecial> getHorariosEspeciales() {
		return horariosEspeciales;
	}

	public void setHorariosEspeciales(Set<HorarioEspecial> horariosEspeciales) {
		this.horariosEspeciales = horariosEspeciales;
	}

	public void finalize() throws Throwable {

	}
	
	//validaciones
	
	public boolean tieneLosDatosMinimos() {
		if(this.nombre != null && this.usuario != null) {
			return true;
		}
		return false;
	}
	
	public boolean sonValidosLosDatos() {
		return validarNombre();
	}
	
	private boolean validarNombre(){
		if(this.nombre.length()>=2 && this.nombre.length()<=30) {
			return true;
		}
		return false;
	}
}//end Recurso