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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sistema.examenes.modelo.usuario.Usuario;

/**
 * @author MATIAS
 * @version 1.0
 * @created 17-ene-2023 02:55:46 p.m.
 */
@Entity
@Table(name = "tipoTurno")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TipoTurno {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "eliminado",nullable=true)
	private boolean eliminado;
	
	@NotNull(message = "Debes ingresar el nombre.")
    @NotBlank(message = "El nombre no puede estar en blanco.")
	@Size(min=2,max=30, message = "El nombre debe tener entre 2 y 30 caracteres.")
	@Column(name = "nombre",length=100,nullable=false)
	private String nombre;
	
	@Column(name = "descripcion")
	private String descripcion;
	

	@OneToMany(mappedBy = "tipoTurno", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	//@JsonBackReference
	//@JsonManagedReference
	@JsonIgnore
	private List<AsignacionRecursoTipoTurno> recursosTipoTurno;

	
	//RELACIONADO CON EL DUEÑO
	//@NotNull(message = "El usuario no puede ser nulo.")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuario", referencedColumnName = "id", nullable = false, unique = false)
	//@JsonBackReference
	@JsonIgnore
    private Usuario usuario;
	
	@Min(value = 1, message = "La duración debe ser al menos 1 minuto.")
	@Max(value = 1440, message = "La duración no puede ser mayor de 1440 minutos (24 horas).")
	@Column(name = "duracionEnMinutos",nullable=true)
	private Integer duracionEnMinutos;
	
	@Min(value = 0, message = "La seña no puede ser negativa.")
	@Column(name = "seniaCtvs",nullable=true)
	private Integer seniaCtvos;
	
	@Min(value = 0, message = "El precio estimado desde no puede ser negativa.")
	@Column(name = "precioEstimadoDesdeCtvos",nullable=true)
	private Integer precioEstimadoDesdeCtvos;
	
	@Min(value = 0, message = "El precio estimado hasta no puede ser negativa.")
	@Column(name = "precioEstimadoHastaCtvos",nullable=true)
	private Integer precioEstimadoHastaCtvos;
	
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

	public int getDuracionEnMinutos() {
		return duracionEnMinutos != null ? duracionEnMinutos.intValue() : 0;
	}

	public void setDuracionEnMinutos(int duracionEnMinutos) {
		this.duracionEnMinutos = duracionEnMinutos;
	}

	public int getSeniaCtvos() {
		return seniaCtvos != null ? seniaCtvos.intValue() : 0;
	}

	public void setSeniaCtvos(int seniaCtvos) {
		this.seniaCtvos = seniaCtvos;
	}

	public int getPrecioEstimadoDesdeCtvos() {
		return precioEstimadoDesdeCtvos != null ? precioEstimadoDesdeCtvos.intValue() : 0;
	}

	public void setPrecioEstimadoDesdeCtvos(int precioEstimadoDesdeCtvos) {
		this.precioEstimadoDesdeCtvos = precioEstimadoDesdeCtvos;
	}

	public int getPrecioEstimadoHastaCtvos() {
		return precioEstimadoHastaCtvos != null ? precioEstimadoHastaCtvos.intValue() : 0;
	}

	public void setPrecioEstimadoHastaCtvos(int precioEstimadoHastaCtvos) {
		this.precioEstimadoHastaCtvos = precioEstimadoHastaCtvos;
	}
	
	//VALIDACIONES
	
	public boolean tieneLosDatosMinimos() {
		if(this.nombre != null && this.usuario != null) {
			return true;
		}
		return false;
	}
	
	public boolean sonValidosLosDatos() {
		return (validarNombre() && validarPrecioEstimado() && validarSenia());
	}
	
	private boolean validarNombre(){
		if(this.nombre.length()>=2 && this.nombre.length()<=30) {
			return true;
		}
		return false;
	}
	
	private boolean validarPrecioEstimado() {
		return (this.precioEstimadoDesdeCtvos != null && 
				this.precioEstimadoHastaCtvos != null &&
				this.precioEstimadoDesdeCtvos >= 0 &&
				this.precioEstimadoDesdeCtvos <= this.precioEstimadoHastaCtvos);
	}
	
	private boolean validarSenia() {
		return (this.seniaCtvos == null && this.seniaCtvos >= 0);
	}
	
	
}//end TipoTurno