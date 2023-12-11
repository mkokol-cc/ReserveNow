package com.sistema.modelo.pagosMP;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import com.sistema.anterior.modelo.Reserva;
import com.sistema.modelo.usuario.Usuario;

@Service
public class MPServiceImpl implements MPService{
	
	private String MPToken = "TEST-7405079288753970-041215-b9acfd241ad71407ba522bda572489f1-554532024";
	private String urlBackend = "https://879b-170-239-49-21.ngrok-free.app";
	private String urlWebHookReservas = urlBackend + "/mp/webhook/reserva/payment-state";
	private String urlWebHookLicencias = urlBackend + "/mp/webhook/licencia/payment-state";

	@Override
	public Preference crearPagoLicencia(Usuario u, Licencia l, Pago pago) throws Exception {
		try {
			//token mio
			MercadoPagoConfig.setAccessToken(MPToken);
			PreferenceClient client = new PreferenceClient();
			List<PreferenceItemRequest> items = crearItemsLicencia(l);
			PreferenceRequest request = PreferenceRequest.builder()
					.items(items)
					.notificationUrl(urlWebHookLicencias+"/"+pago.getId())
					.build();
			Preference p = client.create(request);
			return p;
		}catch (MPApiException ex) {
			System.out.printf(
		          "MercadoPago Error. Status: %s, Content: %s%n",
		          ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
		    throw new Exception(ex.getMessage());
		} catch (MPException ex) {
		    ex.printStackTrace();
		    throw new Exception(ex.getMessage());
		}
	}

	@Override
	public Preference crearPagoSeña(Reserva r) throws Exception {
		long cantMinParaPagar = 15;
		OffsetDateTime currentOffsetDateTime = OffsetDateTime.now(ZoneOffset.systemDefault());
		try {
			MercadoPagoConfig.setAccessToken(r.getAsignacionTipoTurno().getRecurso().getUsuario().getTokenMP());//en realidad va el token de MP del usaurio
			PreferenceClient client = new PreferenceClient();
			List<PreferenceItemRequest> items = crearItemsReserva(r);
			PreferenceRequest request = PreferenceRequest.builder()
					.expirationDateFrom(currentOffsetDateTime)
					.expirationDateTo(currentOffsetDateTime.plusMinutes(cantMinParaPagar))
					.items(items)
					.notificationUrl(urlWebHookReservas+"/"+r.getId())
					.build();
			Preference p = client.create(request);
			return p;
		}catch (MPApiException ex) {
			System.out.printf(
		          "MercadoPago Error. Status: %s, Content: %s%n",
		          ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
		    throw new Exception(ex.getMessage());
		} catch (MPException ex) {
		    ex.printStackTrace();
		    throw new Exception(ex.getMessage());
		}
	}
	
	private List<PreferenceItemRequest> crearItemsLicencia(Licencia l) {
		MercadoPagoConfig.setAccessToken(MPToken);
		List<PreferenceItemRequest> items = new ArrayList<>();
		PreferenceItemRequest item =
		   PreferenceItemRequest.builder()
		       .title(l.getNombre())
		       .description(l.getDescripcion())
		       //.pictureUrl("http://www.myapp.com/myimage.jpg") si hay imagen ponerla aca
		       .quantity(1)
		       .currencyId("ARS")
		       .unitPrice(new BigDecimal(l.getMonto()))
		       .build();
		items.add(item);
		return items;
	}
	
	private List<PreferenceItemRequest> crearItemsReserva(Reserva r) {
		String nombreRec = r.getAsignacionTipoTurno().getRecurso().getNombre();
		String nombreTipoTurno = r.getAsignacionTipoTurno().getTipoTurno().getNombre();
		Usuario u = r.getAsignacionTipoTurno().getRecurso().getUsuario();
		MercadoPagoConfig.setAccessToken(u.getTokenMP());
		String titulo = u.getUsername()+" - Seña de Reserva";
		String descripcion = nombreTipoTurno+" - "+nombreRec+". fecha:"+r.getFechaHoraInicio().toLocalDate()+" hora:"+r.getFechaHoraInicio().toLocalTime()+".";
		List<PreferenceItemRequest> items = new ArrayList<>();
		PreferenceItemRequest item =
		   PreferenceItemRequest.builder()
		       .title(titulo)
		       .description(descripcion)
		       //.pictureUrl("http://www.myapp.com/myimage.jpg") si hay imagen ponerla aca
		       .quantity(1)
		       .currencyId("ARS")
		       .unitPrice(new BigDecimal(r.getAsignacionTipoTurno().getSeniaCtvos()))
		       .build();
		items.add(item);
		return items;
	}

}
