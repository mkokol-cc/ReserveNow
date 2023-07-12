package com.sistema.examenes.anterior.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author paulina
 * @version 1.0
 * @created 17-ene-2023 02:55:45 p.m.
 */
@Entity
@Table(name = "tipoPlan")
public class TipoPlan {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "nombre", length=25, nullable=false)
	private String nombre;
	
	@Column(name = "precioAnual", length=25, nullable=false)
	private String precioAnual;
	
	@Column(name = "precioMensual", length=25, nullable=false)
	private int precioMensual;
	
	@Column(name = "precioSemestral", length=25, nullable=false)
	private int precioSemestral;

	public TipoPlan(){

	}

	public void finalize() throws Throwable {

	}
}//end TipoPlan