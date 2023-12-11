package com.sistema.modelo.pagosMP;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
import com.sistema.anterior.modelo.Reserva;
import com.sistema.modelo.usuario.Usuario;
import com.sistema.nuevo.servicios_interfaces.EstadoService;
import com.sistema.repositorios.LicenciaRepository;
import com.sistema.repositorios.UsuarioRepository;
import com.sistema.servicios_v2.ReservaServiceV2;

@RestController
@RequestMapping("/mp")
public class MPController {
	
	@Autowired
	private PagoService pagoService;
	
	@Autowired
	private ReservaServiceV2 reservaService;
	
	@Autowired
	private LicenciaRepository licRepo;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@Autowired
	private EstadoService estadoService;

	/* 
	Es el estado actual del pago. Puede ser de los siguientes tipos.
	pending: The user has not concluded the payment process (for example, by generating a payment by boleto, it will be concluded at the moment in which the user makes the payment in the selected place).
	approved: The payment has been approved and credited.
	authorized: The payment has been authorized but not captured yet.
	in_process: The payment is in analysis.
	in_mediation: The user started a dispute.
	rejected: The payment was rejected (the user can try to pay again).
	cancelled: Either the payment was canceled by one of the parties or expired.
	refunded: The payment was returned to the user.
	charged_back: A chargeback was placed on the buyer's credit card.
    */
    @PostMapping("webhook/licencia/payment-state/{id}")
    public void actualizarEstadoPago(@PathVariable("id") Long pago, @RequestBody MPNotificationDTO cuerpo) throws Exception {
		PaymentClient client = new PaymentClient();
		Payment pagoMP = client.get(cuerpo.getData().getId());
		if(pagoMP.getStatus().equals("approved")) {
			pagoService.cambiarEstadoPago(1, pago);//pago aprobado (EstadoPago = 1)
		} else if(pagoMP.getStatus().equals("cancelled")) {
			pagoService.cambiarEstadoPago(2, pago);//pago rechazado (EstadoPago = 2)
		}
    }
    
    @PostMapping("webhook/reserva/payment-state/{id}")
    public void actualizarEstadoSenia(@PathVariable("id") Long idReserva, @RequestBody MPNotificationDTO cuerpo) {
    	try {
        	Reserva r = reservaService.obtenerReservaPorId(idReserva);
        	Usuario u = r.getAsignacionTipoTurno().getRecurso().getUsuario();
    		PaymentClient client = new PaymentClient();
    		Payment pagoMP = client.get(cuerpo.getData().getId());
    		if(pagoMP.getStatus().equals("approved")) {
    			reservaService.editarReserva(estadoService.registrarPagoSenia(r), u);
    		} else if(pagoMP.getStatus().equals("cancelled")) {
    			reservaService.eliminarReserva(r, u);
    		}
		}catch(Exception e) {
			//notificar al pagador que hubo un error con el pago
		}
    }
    
    @GetMapping("prueba")
    public void probarIMPL() throws Exception {
    	Licencia lprueba = licRepo.getById(1L);
    	Usuario uprueba = usuarioRepo.getById(2L);
    	Pago pago = pagoService.nuevoPago(lprueba, uprueba);
    }
    
}
