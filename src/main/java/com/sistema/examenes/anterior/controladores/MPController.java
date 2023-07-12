package com.sistema.examenes.anterior.controladores;

//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/*
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
*/
@RestController
@RequestMapping("/api/v1/public")
@CrossOrigin("*")
public class MPController {
	/*
	public void pagar() {
		MercadoPagoConfig.setAccessToken("PROD_ACCESS_TOKEN");
		
	}
	
	
	@GetMapping(value="/payment")
	public Preference cobrar() throws MPException, MPApiException {
		MercadoPagoConfig.setAccessToken("TEST-7405079288753970-041215-b9acfd241ad71407ba522bda572489f1-554532024");
		
		PreferenceItemRequest itemRequest =
			       PreferenceItemRequest.builder()
			           .id("1234")
			           .title("Games")
			           .description("PS5")
			           .pictureUrl("http://picture.com/PS5")
			           .categoryId("games")
			           .quantity(2)
			           .currencyId("BRL")
			           .unitPrice(new BigDecimal("4000"))
			           .build();
			List<PreferenceItemRequest> items = new ArrayList<>();
			items.add(itemRequest);
			PreferenceRequest preferenceRequest = PreferenceRequest.builder().items(items).build();
			PreferenceClient client = new PreferenceClient();
			Preference preference = client.create(preferenceRequest);
			return preference;
	}*/

}