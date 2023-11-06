
package com.sistema.examenes.anterior.modelo;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

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
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * @author MATIAS
 * @version 1.0
 * @created 17-ene-2023 02:55:43 p.m.
 */

@Entity
@Table(name = "reserva")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
//@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "id")
public class Reserva {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",unique=true)
	private Long id;
	
	@Column(name="tipoReserva")
	private String tipoReserva;
	
	//@NotNull(message = "La reserva debe tener un estado.") no lo pongo poque si es nueva reserva no tiene estado y se le asigna automaticamente el estado correspondiente
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "estado", referencedColumnName = "id", nullable = false, unique = false)
	private Estado estado;
	
	@NotNull(message = "Debes ingresar la fecha.")
	@Column(name = "fecha", nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate fecha;

	@NotNull(message = "Debes ingresar la hora.")
	@Column(name = "hora",nullable = false)
	private LocalTime hora;
	
	@NotNull(message = "Debes ingresar la hora de fin.")
	@Column(name = "horaFin",nullable = false)
	private LocalTime horaFin;

	@NotNull(message = "Debes seleccionar un recurso y tipo de turno.")	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "asignacionTipoTurno", referencedColumnName = "id", nullable = false, unique = false)
	private AsignacionRecursoTipoTurno asignacionTipoTurno;
	
	
	@OneToMany(mappedBy="reserva")
	@Column(name = "cambioEstado",nullable=true)
	public List<CambioEstado> cambioEstado;
	
	@Column(name="nota")
	private String nota;
	
	@Valid
	@NotNull(message = "Debes ingresar los datos de la persona que va a reservar.")	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "reservante", referencedColumnName = "id", nullable = false, unique = false)
	@JsonManagedReference
	//@JsonBackReference
	private Reservante reservante;
	
	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDate fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public LocalTime getHoraCreacion() {
		return horaCreacion;
	}

	public void setHoraCreacion(LocalTime horaCreacion) {
		this.horaCreacion = horaCreacion;
	}

	@Column(name="fechaCreacion")
	private LocalDate fechaCreacion;
	
	@Column(name="horaCreacion")
	private LocalTime horaCreacion;
	
	public Reserva(){

	}

	public void finalize() throws Throwable {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
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
	/*
	public AsignacionRecursoTipoTurno getTipoTurno() {
		return asignacionTipoTurno;
	}

	public void setTipoTurno(AsignacionRecursoTipoTurno asignacionTipoTurno) {
		this.asignacionTipoTurno = asignacionTipoTurno;
	}*/

	public String getTipoReserva() {
		return tipoReserva;
	}

	public void setTipoReserva(String tipoReserva) {
		this.tipoReserva = tipoReserva;
	}

	public AsignacionRecursoTipoTurno getAsignacionTipoTurno() {
		return asignacionTipoTurno;
	}

	public void setAsignacionTipoTurno(AsignacionRecursoTipoTurno asignacionTipoTurno) {
		this.asignacionTipoTurno = asignacionTipoTurno;
	}

	public List<CambioEstado> getCambioEstado() {
		return cambioEstado;
	}

	public void setCambioEstado(List<CambioEstado> cambioEstado) {
		this.cambioEstado = cambioEstado;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public Reservante getReservante() {
		return reservante;
	}

	public void setReservante(Reservante reservante) {
		this.reservante = reservante;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}
	
	
	//VALIDACIONES

	public boolean tieneLosDatosMinimos() {
		if(this.fecha != null && this.asignacionTipoTurno != null && this.hora != null
				&& this.reservante != null && this.horaFin != null && this.estado != null) {
			return true;
		}
		return false;
	}
	
	public boolean sonValidosLosDatos() {
		return validarHorarios() && validarUsuario() && validarFecha();
	}
	
	private boolean validarUsuario() {
		return(this.asignacionTipoTurno.getRecurso().getUsuario() == this.reservante.getUsuario());
	}
	
	private boolean validarHorarios(){
		return (this.hora.isBefore(this.horaFin) || this.hora.equals(this.horaFin));
	}
	
	private boolean validarFecha() {
		if(this.fecha.isAfter(LocalDate.now())) {
			return true;
		}else {
			return (this.fecha.equals(LocalDate.now()) ? this.hora.isAfter(LocalTime.now()) : false);
		}
	}
	/*
	private boolean validarHorarioDeTurno() {
		for(Horario)
	}*/
	
	/*
	 * PARA VALIDAR SI SE PUEDE RESERVAR
	 * -EL RESERVANTE DEBE ESTAR PERMITIDO
	 * -LOS HORARIOSESTAN CORRECTOS 
	 * -LA CONCURRENCIA ES CORRECTA
	 * */
	@AssertTrue(message="Se intento reservar para un dia y hora que ya pasaron.")
	private boolean isValidaFechaYHora() {
		LocalDateTime fechaHoraReserva = LocalDateTime.of(this.fecha, this.hora);
		return fechaHoraReserva.isAfter(LocalDateTime.now());
	}
	
	@AssertTrue(message="No tiene permitido reservas a su nombre.")
	private boolean isReservanteValido() {
		return this.reservante.isHabilitado();
	}
	

	
	
	
	
	
	
	
	
	
	//------------------------------------------
	//primero hay que setear la AsignacionRecursoTipoTurno a la Reserva
	//------------------------------------------	
	
	@AssertTrue(message="Recurso ocupado.")
	public boolean isOcupadoElRecurso() {
		for (Map.Entry<LocalTime, Boolean> entry : this.asignacionTipoTurno.getHorariosDisponibles(this.fecha).entrySet()) {
            LocalTime hora = entry.getKey();
            Boolean disponible = entry.getValue();
            System.out.println(hora+" - "+disponible);
            if(hora.equals(this.hora)) {
            	return disponible;
            }
        }
		return false;//true - libre | false - ocupado
	}

	@AssertTrue(message="Concurrencia llena para el horario.")
	private boolean isValidaConcurrencia() {
		System.out.println("ENTRE 2");
		//int concurrencias = 0;
		//List<Reserva> reservasDelRecursoParaElDia = this.asignacionTipoTurno.getRecurso().obtenerReservasPorFecha(this.fecha);
		//for(Reserva r : reservasDelRecursoParaElDia) {
		//	if(r.getHora().equals(this.hora) && r.getAsignacionTipoTurno().equals(this.asignacionTipoTurno) && r.getHoraFin().equals(this.horaFin)) {
		//		concurrencias++;
		//		if(concurrencias == this.asignacionTipoTurno.getCantidadConcurrencia()) {
		//			return false;
		//		}
		//	}
		//}
		return true;//libre
	}
	
	
	@AssertTrue(message="La hora fin esta mal.")
	private boolean isValidaHoraFin() {
		//System.out.println("ENTRE 3");
		return this.hora.plusMinutes((long)this.asignacionTipoTurno.getDuracionEnMinutos()).equals(this.horaFin);
	}
	
	
	
	@AssertTrue(message="Horario no aceptado.")
	private boolean isHorarioValido() {
		//System.out.println("ENTRE 4");
		//return this.asignacionTipoTurno.turnosParaLaFecha(this.fecha).contains(this.hora);
		return true;
	}
	
	public String validarReserva() {
		if(isHorarioValido()) {
			if(isValidaHoraFin()) {
				if(isOcupadoElRecurso()) {
					if(isValidaConcurrencia()) {
						return ""; 
					}
					return "Concurrencia llena para el horario.";
				}else {
					return "Recurso ocupado.";
				}
			}else {
				return "La hora fin esta mal.";
			}
		}else {
			return "Horario no aceptado.";
		}
	}
	
	private boolean sePisaLosHorariosConEstaReserva(Reserva r) {
		//hd hh HD HH - (hd>=HH || hh<=HD)
		//hd this.hora - hh this.horaFin
		if( this.hora.compareTo(r.getHoraFin()) >= 0 || this.horaFin.compareTo(r.getHora()) <= 0 ) {
			return false;
		}
		return true;
	}
	//public boolean estaEn
	
	
	public boolean estaEnHorario() {
		if(this.getEstado().isEsEstadoFinal()) {
			return true;
		}
		List<LocalTime> horarios = this.getAsignacionTipoTurno().getHorariosTurnos(fecha);
		return horarios.contains(this.getHora()) && horarios.contains(this.getHoraFin());
	}

	public Reserva editarReserva(Reserva r) {
		this.estado = r.getEstado();
		this.fecha = r.getFecha();
		this.hora = r.getHora();
		this.horaFin = r.getHoraFin();
		this.nota = r.getNota();
		return this;
	}
	
	public CambioEstado obtenerUltimoCambioEstado() {
		CambioEstado ultimo = new CambioEstado();
		LocalDateTime fechaHoraAux = LocalDateTime.MIN;
		for(CambioEstado c : this.cambioEstado) {
			LocalDateTime delCambioEstado = c.getFecha().atTime(c.getHora());
			if(fechaHoraAux.isBefore(delCambioEstado)) {
				fechaHoraAux = delCambioEstado;
				ultimo = c;
			}
		}
		return ultimo;
	}
	
}//end Reserva