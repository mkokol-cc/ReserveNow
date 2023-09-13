package com.sistema.examenes.modelo.usuario;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sistema.examenes.anterior.modelo.Recurso;
import com.sistema.examenes.anterior.modelo.Reservante;
import com.sistema.examenes.anterior.modelo.TipoTurno;
import com.sistema.examenes.modelo.usuario.pagos.Pago;

@Entity
@Table(name = "usuario")
//@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Debes ingresar el email.")
    @NotBlank(message = "El email no puede estar en blanco.")
    @Email(message = "El email debe ser válido.")
    private String email;
    
    @NotNull(message = "Debes ingresar la contraseña.")
    @NotBlank(message = "La contraseña no puede estar en blanco.")
    @Size(min = 8, max = 24, message = "La contraseña debe tener entre 8 y 24 caracteres.")
    private String password;
    
    @NotNull(message = "Debes ingresar el nombre.")
    @NotBlank(message = "El nombre no puede estar en blanco.")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres.")
    private String nombre;
    
    @NotNull(message = "Debes ingresar el apellido.")
    @NotBlank(message = "El apellido no puede estar en blanco.")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres.")
    private String apellido;
    
    @NotNull(message = "Debes ingresar el DNI.")
    @NotBlank(message = "El DNI no puede estar en blanco.")
    private String dni;
    
    @NotNull(message = "Debes ingresar el telefono.")
    @NotBlank(message = "El telefono no puede estar en blanco.")
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{3,14}$", message = "El número de teléfono esta en formato incorrecto.")
    private String telefono;
    private boolean enabled = true;
    private boolean emailVerificado = false;
    private String perfil;
    
    private String fbEmpresarial;
    private String wppEmpresarial;
    private String igEmpresarial;
    private String emailEmpresarial;
    private String twitterEmpresarial;
    private String ubicacionEmpresarial;
    
    private boolean autopago;
    private boolean deshabilitarAutopagoSiCambiaElPrecio;
    
    //CONFIGURACION
    private boolean requiereReservanteConDni=false;
    private boolean requiereReservanteConTelefono=false;
    private boolean requiereReservanteConNombreYApellido=false;
    
    @Column(unique = true, nullable = true)
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
  /*  
    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    private List<Pago> pagos;
    
    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    private List<Recurso> recursos;
    
    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    private List<TipoTurno> tiposDeTurno;
*/
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Pago> pagos;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    //@JsonManagedReference
    @JsonIgnore
    private List<Recurso> recursos;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    //@JsonManagedReference
    @JsonIgnore
    private List<TipoTurno> tiposDeTurno;
    
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    //@JsonManagedReference
    @JsonIgnore
    private List<Reservante> clientes;


    
    
	public Usuario(){

    }

    public Usuario(Long id, String email, String password, String nombre, String apellido, String dni, String telefono,
			boolean enabled, String perfil, String fbEmpresarial, String wppEmpresarial, String igEmpresarial,
			String emailEmpresarial, String twitterEmpresarial, String ubicacionEmpresarial, boolean autopago,
			boolean deshabilitarAutopagoSiCambiaElPrecio, String dbUrl, String dbUsername, String dbPassword,
			List<Pago> pagos) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.telefono = telefono;
		this.enabled = enabled;
		this.perfil = perfil;
		this.fbEmpresarial = fbEmpresarial;
		this.wppEmpresarial = wppEmpresarial;
		this.igEmpresarial = igEmpresarial;
		this.emailEmpresarial = emailEmpresarial;
		this.twitterEmpresarial = twitterEmpresarial;
		this.ubicacionEmpresarial = ubicacionEmpresarial;
		this.autopago = autopago;
		this.deshabilitarAutopagoSiCambiaElPrecio = deshabilitarAutopagoSiCambiaElPrecio;
		this.dbUrl = dbUrl;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
		this.pagos = pagos;
	}




	@Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Authority> autoridades = new HashSet<>();
        //this.usuarioRoles.forEach(usuarioRol -> {
        autoridades.add(new Authority("ADMIN"));
        //});*/
        return autoridades;
    }
    
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.email;
	}
    
    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getFbEmpresarial() {
		return fbEmpresarial;
	}

	public void setFbEmpresarial(String fbEmpresarial) {
		this.fbEmpresarial = fbEmpresarial;
	}

	public String getWppEmpresarial() {
		return wppEmpresarial;
	}

	public void setWppEmpresarial(String wppEmpresarial) {
		this.wppEmpresarial = wppEmpresarial;
	}

	public String getIgEmpresarial() {
		return igEmpresarial;
	}

	public void setIgEmpresarial(String igEmpresarial) {
		this.igEmpresarial = igEmpresarial;
	}

	public String getEmailEmpresarial() {
		return emailEmpresarial;
	}

	public void setEmailEmpresarial(String emailEmpresarial) {
		this.emailEmpresarial = emailEmpresarial;
	}

	public String getTwitterEmpresarial() {
		return twitterEmpresarial;
	}

	public void setTwitterEmpresarial(String twitterEmpresarial) {
		this.twitterEmpresarial = twitterEmpresarial;
	}

	public String getUbicacionEmpresarial() {
		return ubicacionEmpresarial;
	}

	public void setUbicacionEmpresarial(String ubicacionEmpresarial) {
		this.ubicacionEmpresarial = ubicacionEmpresarial;
	}

	public boolean isAutopago() {
		return autopago;
	}

	public void setAutopago(boolean autopago) {
		this.autopago = autopago;
	}

	public boolean isDeshabilitarAutopagoSiCambiaElPrecio() {
		return deshabilitarAutopagoSiCambiaElPrecio;
	}

	public void setDeshabilitarAutopagoSiCambiaElPrecio(boolean deshabilitarAutopagoSiCambiaElPrecio) {
		this.deshabilitarAutopagoSiCambiaElPrecio = deshabilitarAutopagoSiCambiaElPrecio;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public List<Pago> getPagos() {
		return pagos;
	}

	public void setPagos(List<Pago> pagos) {
		this.pagos = pagos;
	}

	public List<Recurso> getRecursos() {
		return recursos;
	}

	public void setRecursos(List<Recurso> recursos) {
		this.recursos = recursos;
	}

	public List<TipoTurno> getTiposDeTurno() {
		return tiposDeTurno;
	}

	public void setTiposDeTurno(List<TipoTurno> tiposDeTurno) {
		this.tiposDeTurno = tiposDeTurno;
	}

	public boolean isEmailVerificado() {
		return emailVerificado;
	}

	public void setEmailVerificado(boolean emailVerificado) {
		this.emailVerificado = emailVerificado;
	}

	public List<Reservante> getClientes() {
		return clientes;
	}

	public void setClientes(List<Reservante> clientes) {
		this.clientes = clientes;
	}

	public boolean isRequiereReservanteConDni() {
		return requiereReservanteConDni;
	}

	public void setRequiereReservanteConDni(boolean requiereReservanteConDni) {
		this.requiereReservanteConDni = requiereReservanteConDni;
	}

	public boolean isRequiereReservanteConTelefono() {
		return requiereReservanteConTelefono;
	}

	public void setRequiereReservanteConTelefono(boolean requiereReservanteConTelefono) {
		this.requiereReservanteConTelefono = requiereReservanteConTelefono;
	}

	public boolean isRequiereReservanteConNombreYApellido() {
		return requiereReservanteConNombreYApellido;
	}

	public void setRequiereReservanteConNombreYApellido(boolean requiereReservanteConNombreYApellido) {
		this.requiereReservanteConNombreYApellido = requiereReservanteConNombreYApellido;
	}
	
	//VALIDACIONES
	
	/*
	 *     private String email;
    private String password;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;*/
	
	public boolean tieneLosDatosMinimos() {
		return (this.nombre != null && this.apellido != null && this.dni != null 
				&& this.telefono != null && this.email != null && this.password != null);
	}
	
	public boolean sonValidosLosDatos() {
		return ( validarNombre() && validarDni() &&  validarPassword());
	}
	

	private boolean validarDni() {
        return (!this.dni.isBlank() && !this.dni.isEmpty() && this.dni != null);
	}
	
	private boolean validarNombre() {
		return (this.nombre != null && this.apellido != null && !this.nombre.isBlank() && !this.apellido.isBlank());
	}
	

    private boolean validarPassword() {
    	return (this.password.length()>7 && this.password.length()<25);
    }
	
	
}