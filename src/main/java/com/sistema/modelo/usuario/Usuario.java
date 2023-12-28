package com.sistema.modelo.usuario;

import java.time.LocalDate;
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
import com.sistema.anterior.modelo.Notificacion;
import com.sistema.anterior.modelo.Recurso;
import com.sistema.anterior.modelo.Reservante;
import com.sistema.anterior.modelo.TipoTurno;
import com.sistema.modelo.pagosMP.Pago;

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
    private boolean telefonoVerificado = false;
    //private boolean esFree = false;
    
    private String perfil;
    
    private String linkIg;
    private String linkFb;
    private String linkX;
    private String linkYT;
    
    private String emailAtencionCliente;
    private String telefonoAtencionCliente;
    
    private String direccion;
    
    //private boolean autopago;
    //private boolean deshabilitarAutopagoSiCambiaElPrecio;
    private String tokenMP;
    
    //CONFIGURACION
    private boolean requiereReservanteConDni=false;
    private boolean requiereReservanteConTelefono=false;
    private boolean requiereReservanteConNombreYApellido=false;
    
    //cantidad de dias con antelacion que se puede reservar (es para que no se reserve de aca a 1 año)
    private Integer CantSemanasDeAntelacionParaReservar = 5;
    
    @Column(unique = true, nullable = true)
    private String nombreEspacioPersonal;
    
    private LocalDate fechaRegistro = LocalDate.now();
    
    private LocalDate fechaVtoLicencia = LocalDate.now().plusMonths(1);//esto si queremos darle 1 mes gratis al nuevo usuario
    //private String dbUsername;
    //private String dbPassword;
    private Long notificarMinutosAntes = 15L;
    
    
    
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

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Notificacion> notificacion;
    
    
	public Usuario(){

    }
/*
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
*/



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



	public String getTelefono() {
		return telefono;
	}



	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}



	public boolean isTelefonoVerificado() {
		return telefonoVerificado;
	}



	public void setTelefonoVerificado(boolean telefonoVerificado) {
		this.telefonoVerificado = telefonoVerificado;
	}



	public String getLinkIg() {
		return linkIg;
	}



	public void setLinkIg(String linkIg) {
		this.linkIg = linkIg;
	}



	public String getLinkFb() {
		return linkFb;
	}



	public void setLinkFb(String linkFb) {
		this.linkFb = linkFb;
	}



	public String getLinkX() {
		return linkX;
	}



	public void setLinkX(String linkX) {
		this.linkX = linkX;
	}



	public String getLinkYT() {
		return linkYT;
	}



	public void setLinkYT(String linkYT) {
		this.linkYT = linkYT;
	}



	public String getEmailAtencionCliente() {
		return emailAtencionCliente;
	}



	public void setEmailAtencionCliente(String emailAtencionCliente) {
		this.emailAtencionCliente = emailAtencionCliente;
	}



	public String getTelefonoAtencionCliente() {
		return telefonoAtencionCliente;
	}



	public void setTelefonoAtencionCliente(String telefonoAtencionCliente) {
		this.telefonoAtencionCliente = telefonoAtencionCliente;
	}



	public String getDireccion() {
		return direccion;
	}



	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}



	public String getTokenMP() {
		return tokenMP;
	}



	public void setTokenMP(String tokenMP) {
		this.tokenMP = tokenMP;
	}



	public Integer getCantSemanasDeAntelacionParaReservar() {
		return CantSemanasDeAntelacionParaReservar;
	}



	public void setCantSemanasDeAntelacionParaReservar(Integer CantSemanasDeAntelacionParaReservar) {
		this.CantSemanasDeAntelacionParaReservar = CantSemanasDeAntelacionParaReservar;
	}



	public String getNombreEspacioPersonal() {
		return nombreEspacioPersonal;
	}



	public void setNombreEspacioPersonal(String nombreEspacioPersonal) {
		this.nombreEspacioPersonal = nombreEspacioPersonal;
	}



	public List<Notificacion> getNotificacion() {
		return notificacion;
	}



	public void setNotificacion(List<Notificacion> notificacion) {
		this.notificacion = notificacion;
	}



	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}



	public void setFechaRegistro(LocalDate fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}



	public LocalDate getFechaVtoLicencia() {
		return fechaVtoLicencia;
	}



	public Long getNotificarMinutosAntes() {
		return notificarMinutosAntes;
	}



	public void setNotificarMinutosAntes(Long notificarMinutosAntes) {
		this.notificarMinutosAntes = notificarMinutosAntes;
	}



	public void setFechaVtoLicencia(LocalDate fechaVtoLicencia) {
		this.fechaVtoLicencia = fechaVtoLicencia;
	}
	
	public void addMesesLicencia(Long meses) {
		if(this.fechaVtoLicencia.isBefore(LocalDate.now())) {
			setFechaVtoLicencia(LocalDate.now().plusMonths(meses));
		}else {
			setFechaVtoLicencia(this.fechaVtoLicencia.plusMonths(meses));
		}
	}



	public Usuario editar(Usuario u) {
        //this.email = u.getEmail();
        this.password = u.getPassword();
        this.nombre = u.getNombre();
        this.apellido = u.getApellido();
        this.dni = u.getDni();
        this.telefono = u.getTelefono();
        //this.enabled = enabled;
        //this.emailVerificado = emailVerificado;
        //this.telefonoVerificado = telefonoVerificado;
        this.perfil = u.getPerfil();
        this.linkIg = u.getLinkIg();
        this.linkFb = u.getLinkFb();
        this.linkX = u.getLinkX();
        this.linkYT = u.getLinkYT();
        this.emailAtencionCliente = u.getEmailAtencionCliente();
        this.telefonoAtencionCliente = u.getTelefonoAtencionCliente();
        this.direccion = u.getDireccion();
        this.tokenMP = u.getTokenMP();
        this.requiereReservanteConDni = u.isRequiereReservanteConDni();
        this.requiereReservanteConTelefono = u.isRequiereReservanteConTelefono();
        this.requiereReservanteConNombreYApellido = u.isRequiereReservanteConNombreYApellido();
        this.CantSemanasDeAntelacionParaReservar = u.getCantSemanasDeAntelacionParaReservar();
        this.nombreEspacioPersonal = u.getNombreEspacioPersonal();
        //this.fechaRegistro = u.getFechaRegistro();
        //this.fechaVtoLicencia = u.getFechaVtoLicencia();
        this.notificarMinutosAntes = u.getNotificarMinutosAntes();
        return this;
	}
}