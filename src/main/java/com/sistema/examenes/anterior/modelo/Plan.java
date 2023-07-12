package com.sistema.examenes.anterior.modelo;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author paulina
 * @version 1.0
 * @created 17-ene-2023 02:55:42 p.m.
 */
@Entity
@Table(name = "plan")
//@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Plan {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "meses", nullable=false)
	private int duracionPlanMeses;
	
	@Column(name = "desde")
	private Date desde;
	
	@Column(name = "hasta")
	private Date hasta;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id")
	private TipoPlan tipoPlan;
	/*
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "empresa", referencedColumnName = "id", nullable = false, unique = true)
	@JsonManagedReference
	private Empresa empresa;
	*/
	public Plan() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getDuracionPlanMeses() {
		return duracionPlanMeses;
	}

	public void setDuracionPlanMeses(int duracionPlanMeses) {
		this.duracionPlanMeses = duracionPlanMeses;
	}

	public Date getDesde() {
		return desde;
	}

	public void setDesde(Date desde) {
		this.desde = desde;
	}

	public Date getHasta() {
		return hasta;
	}

	public void setHasta(Date hasta) {
		this.hasta = hasta;
	}

	public TipoPlan getTipoPlan() {
		return tipoPlan;
	}

	public void setTipoPlan(TipoPlan tipoPlan) {
		this.tipoPlan = tipoPlan;
	}
	/*
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	*/
	public void finalize() throws Throwable {

	}
}//end Plan