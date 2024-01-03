
package com.sistema.anterior.modelo;



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
	@Column(name = "fechaHoraInicio", nullable = false)
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime fechaHoraInicio;
	
	@NotNull(message = "Debes ingresar la hora de fin.")
	@Column(name = "fechaHoraFin",nullable = false)
	private LocalDateTime fechaHoraFin;

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
	//@JsonManagedReference
	//@JsonBackReference
	private Reservante reservante;
	
	@Column(nullable=true)
	private String linkPago;
	private boolean esTurnoFijo;
	private Long cadaCuantasSemanas;
	
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

	public String getLinkPago() {
		return linkPago;
	}

	public void setLinkPago(String linkPago) {
		this.linkPago = linkPago;
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

	
	/*
	
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
	}*/
	/*
	private boolean validarHorarioDeTurno() {
		for(Horario)
	}*/
	
	public LocalDateTime getFechaHoraInicio() {
		return fechaHoraInicio;
	}

	public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
		this.fechaHoraInicio = fechaHoraInicio;
	}

	public LocalDateTime getFechaHoraFin() {
		return fechaHoraFin;
	}

	public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
		this.fechaHoraFin = fechaHoraFin;
	}

	public boolean isEsTurnoFijo() {
		return esTurnoFijo;
	}

	public void setEsTurnoFijo(boolean esTurnoFijo) {
		this.esTurnoFijo = esTurnoFijo;
	}

	public Long getCadaCuantasSemanas() {
		return cadaCuantasSemanas;
	}

	public void setCadaCuantasSemanas(Long cadaCuantasSemanas) {
		this.cadaCuantasSemanas = cadaCuantasSemanas;
	}

	/*
	 * PARA VALIDAR SI SE PUEDE RESERVAR
	 * -EL RESERVANTE DEBE ESTAR PERMITIDO
	 * -LOS HORARIOSESTAN CORRECTOS 
	 * -LA CONCURRENCIA ES CORRECTA
	 * */
	@AssertTrue(message="Se intento reservar para un dia y hora que ya pasaron.")
	public boolean isValidaFechaYHora() {
		return fechaHoraInicio.isAfter(LocalDateTime.now());
	}
	
	@AssertTrue(message="No tiene permitido reservas a su nombre.")
	private boolean isReservanteValido() {
		return this.reservante.isHabilitado();
	}
	

	
	
	
	
	
	
	
	
	
	//------------------------------------------
	//primero hay que setear la AsignacionRecursoTipoTurno a la Reserva
	//------------------------------------------	
	
	@AssertTrue(message="Horario ocupado.")
	public boolean isOcupadoElRecurso() {
		for (Map.Entry<LocalDateTime, Integer> entry : this.asignacionTipoTurno.obtenerTurnosLibres().entrySet()) {
            LocalDateTime hora = entry.getKey();
            Integer disponible = entry.getValue();
            System.out.println(hora+" - "+disponible);
            if(hora.equals(this.fechaHoraInicio) && disponible>0) {
            	return true;
            }
        }
		return false;//true - libre | false - ocupado
	}

	/*
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
	*/
	
	@AssertTrue(message="La hora fin esta mal.")
	private boolean isValidaHoraFin() {
		//System.out.println("ENTRE 3");
		this.fechaHoraFin = this.fechaHoraInicio.plusMinutes((long)this.asignacionTipoTurno.getDuracionEnMinutos());
		return this.fechaHoraInicio.plusMinutes((long)this.asignacionTipoTurno.getDuracionEnMinutos()).equals(this.fechaHoraFin);
	}
	
	
	
	@AssertTrue(message="Recurso inhabilitado para reserva.")
	private boolean isHabilitado() {
		return (!this.asignacionTipoTurno.isEliminado() && !this.asignacionTipoTurno.getRecurso().isEliminado()
				&& !this.asignacionTipoTurno.getTipoTurno().isEliminado());
	}
	
	public boolean estaEnHorario() {
		if(this.getEstado().isEsEstadoFinal()) {
			return true;
		}
		List<LocalTime> horarios = this.getAsignacionTipoTurno().getHorariosTurnos(this.fechaHoraInicio.toLocalDate());
		return horarios.contains(this.fechaHoraInicio.toLocalTime()) && horarios.contains(this.fechaHoraFin.toLocalTime());
	}

	public Reserva editarReserva(Reserva r) {
		this.estado = r.getEstado();
		this.fechaHoraInicio = r.getFechaHoraInicio();
		this.fechaHoraFin = r.getFechaHoraFin();
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
	
	/*9.12*/
	public boolean esOcupado(LocalDateTime desde, LocalDateTime hasta) {
		if(this.esTurnoFijo) {
			int auxiliar = 0;
			while(!this.fechaHoraInicio.toLocalDate().plusWeeks(this.cadaCuantasSemanas*auxiliar).isAfter(desde.toLocalDate())) {
				if(this.fechaHoraInicio.plusWeeks(this.cadaCuantasSemanas*auxiliar).toLocalDate().equals(desde.toLocalDate())) {
					return !(this.fechaHoraFin.plusWeeks(this.cadaCuantasSemanas*auxiliar).isBefore(desde) 
							|| hasta.isBefore(this.fechaHoraInicio.plusWeeks(this.cadaCuantasSemanas*auxiliar)));
				}
				auxiliar++;
			}
			return false;
		}else {
			return !(this.fechaHoraFin.isBefore(desde) || hasta.isBefore(this.fechaHoraInicio));
		}
	}
	
}//end Reserva