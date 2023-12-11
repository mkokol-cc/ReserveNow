package com.sistema.modelo.pagosMP;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mercadopago.resources.preference.Preference;
import com.sistema.modelo.usuario.Usuario;
import com.sistema.repositorios.PagoRepository;
import com.sistema.servicios.UsuarioService;

@Service
public class PagoServiceImpl implements PagoService{
	@Autowired
	private MPService mpService;
	
	@Autowired
	private PagoRepository pagoRepo;
	
	@Autowired
	private UsuarioService usuarioService;
	
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
		nuevo.setLinkPago(pagoMP.getInitPoint());
		return save(nuevo);
	}
	
	@Override
	public Pago cambiarEstadoPago(int estado, Long id) throws Exception{
		//notificar si es PAGADO
		Pago p = pagoRepo.getById(id);
		if(EstadoPago.PAGADO.ordinal()==estado) {
			Usuario u = p.getUsuario();
			u.addMesesLicencia(p.getLicencia().getMeses());
			usuarioService.editarUsuario(u);
		}
		p.setEstado(estado);
		return save(p);
	}
	
	private Pago save(Pago p) {
		return pagoRepo.save(p);
	}
}
