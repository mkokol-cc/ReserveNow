package com.sistema.examenes.anterior.modelo;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sistema.examenes.modelo.usuario.Usuario;

@Entity
@Table(name = "comentario")
//@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class Comentario {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "id")
		private Long id;
		
		@Column(name = "hora", nullable=false)
		private Time hora;
		
		@Column(name = "fecha",nullable = false)
		@JsonFormat(pattern="yyyy-MM-dd")
		private Date fecha;
		
		@Column(name = "comentario",nullable = false)
		private String comentario;
		
		@ManyToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "usuario", referencedColumnName = "id", nullable = false, unique = false)
		private Usuario usuario;
		
		@ManyToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "comentario_id", referencedColumnName = "id")
		private Comentario respuesta;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Time getHora() {
			return hora;
		}

		public void setHora(Time hora) {
			this.hora = hora;
		}

		public Date getFecha() {
			return fecha;
		}

		public void setFecha(Date fecha) {
			this.fecha = fecha;
		}

		public String getComentario() {
			return comentario;
		}

		public void setComentario(String comentario) {
			this.comentario = comentario;
		}

		public Usuario getUsuario() {
			return usuario;
		}

		public void setUsuario(Usuario usuario) {
			this.usuario = usuario;
		}

		public Comentario getRespuesta() {
			return respuesta;
		}

		public void setRespuesta(Comentario respuesta) {
			this.respuesta = respuesta;
		}
		
		
}