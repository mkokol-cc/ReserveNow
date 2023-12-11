package com.sistema.modelo.pagosMP;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "licencia")
public class Licencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
	private int monto;
	private String nombre;
	private String descripcion;
	private Long meses;
	
	@OneToMany(mappedBy = "licencia", fetch = FetchType.EAGER)
	private List<Pago> pagos;

	
	
	public Licencia() {}
	public Licencia(Long id, int monto, String nombre, String descripcion, List<Pago> pagos) {
		this.id = id;
		this.monto = monto;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.pagos = pagos;
	}

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getMonto() {
		return monto;
	}

	public void setMonto(int monto) {
		this.monto = monto;
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

	public List<Pago> getPagos() {
		return pagos;
	}

	public void setPagos(List<Pago> pagos) {
		this.pagos = pagos;
	}
	public Long getMeses() {
		return meses;
	}
	public void setMeses(Long meses) {
		this.meses = meses;
	}
	

	
	
	
	
	
}
