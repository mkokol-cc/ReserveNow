package com.sistema.examenes.modelo.usuario.pagos;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mercadopago.resources.preference.Preference;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.repositorios.PagoRepository;

@Service
public class PagoServiceImpl implements PagoService{
	@Autowired
	private MPService mpService;
	
	@Autowired
	private PagoRepository pagoRepo;
	
	@Override
	public Pago nuevoPago(Licencia l, Usuario u) throws Exception{
		Pago nuevo = new Pago();
		nuevo.setMonto(l.getMonto());
		nuevo.setLicencia(l);
		nuevo.setUsuario(u);
		nuevo.setFecha(LocalDate.now());
		nuevo.setFechaVto(LocalDate.now().plusDays(1));
		nuevo.setEstado(0);
		Pago guardado = save(nuevo);
		Preference pagoMP = mpService.crearPagoLicencia(u, l, guardado);
		nuevo.setMpPreferenceId(pagoMP.getId());
		
		System.out.println("El link de pago es: ");
		System.out.println(pagoMP.getInitPoint());
		
		return save(nuevo);
	}
	
	@Override
	public Pago cambiarEstadoPago(int estado, Long id) throws Exception{
		Pago p = pagoRepo.getById(id);
		p.setEstado(estado);
		return save(p);
	}
	
	private Pago save(Pago p) {
		return pagoRepo.save(p);
	}
}
