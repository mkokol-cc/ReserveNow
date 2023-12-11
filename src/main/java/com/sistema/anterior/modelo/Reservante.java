package com.sistema.anterior.modelo;

import java.util.HashSet;
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
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sistema.modelo.usuario.Usuario;

@Entity
@Table
public class Reservante {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	//@NotNull(message = "Debes ingresar el nombre.")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres.")
	@Column(name="nombre")
	private String nombre;
	
	//@NotNull(message = "Debes ingresar el apellido.")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres.")
    @Column(name = "apellido")
	private String apellido;
	
	//@NotNull(message = "Debes ingresar el DNI.")
	@Size(min = 7, max = 8, message = "El DNI debe tener entre 7 y 8 caracteres.")
	@Column(name="dni")
	private String dni;
	
	@NotNull(message = "Debes ingresar el telefono.")
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{3,14}$", message = "El número de teléfono esta en formato incorrecto.")
	@Column(name="telefono",unique=false)
	private String telefono;
	
	@Column(name="habilitado")
	private boolean habilitado;
	
	@OneToMany(mappedBy = "reservante", fetch = FetchType.EAGER/*, cascade = CascadeType.ALL, orphanRemoval = true*/)
    @JsonIgnore
    private Set<Reserva> reservas = new HashSet<>();
	
	//RELACIONADO CON EL DUEÑO
	@NotNull(message = "El usuario no puede ser nulo.")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuario", referencedColumnName = "id", nullable = false, unique = false)
    private Usuario usuario;

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

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Set<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(Set<Reserva> reservas) {
		this.reservas = reservas;
	}

	public boolean isHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
	//VALIDACIONES
	
	public boolean tieneLosDatosMinimos() {
		if(this.dni == null && this.usuario.isRequiereReservanteConDni()) {
			return false;
		}
		if((this.nombre == null || this.apellido == null || this.nombre.isBlank() || this.apellido.isBlank()) 
				&& this.usuario.isRequiereReservanteConNombreYApellido()) {
			return false;
		}
		if((this.telefono.isBlank() || this.telefono == null) 
				&& this.usuario.isRequiereReservanteConTelefono()) {
			return false;
		}
		return true;
	}
	
	public boolean sonValidosLosDatos() {
		return (this.usuario.isRequiereReservanteConTelefono() ? validarTelefono() : true);
	}
	
	private boolean validarTelefono() {/*
		String regex = "^(\\+\\d{1,3}[- ]?)?\\d{3,14}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(this.telefono);
        return matcher.matches();*/
        return true;
	}
	
	private boolean validarDni() {
        return (!this.dni.isBlank() && !this.dni.isEmpty() && this.dni != null);
	}
	
	private boolean validarNombre() {
		return (this.nombre != null && this.apellido != null && !this.nombre.isBlank() && !this.apellido.isBlank());
	}
	

	@AssertTrue(message = "El usuario requiere datos de DNI")
	public boolean isDni() {
		if(this.usuario==null) {
			return false;
		}
		if(this.usuario.isRequiereReservanteConDni()) {
			return this.dni!=null;
		}
		return true;
	}
	
	@AssertTrue(message = "El usuario requiere nombre y apellido")
	public boolean isNombreYApellido() {
		if(this.usuario==null) {
			return false;
		}
		if(this.usuario.isRequiereReservanteConNombreYApellido()) {
			return this.nombre!=null && this.apellido!=null;
		}
		return true;
	}

	public Reservante editarReservante(Reservante r) {
		this.habilitado = r.isHabilitado();
		this.nombre = r.getNombre();
		this.apellido = r.getApellido();
		this.dni = r.getDni();
		return this;
	}
}
