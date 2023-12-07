package com.sistema.examenes.modelo.usuario.pagos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.PhoneRequest;
import com.mercadopago.client.payment.PaymentAdditionalInfoPayerRequest;
import com.mercadopago.client.payment.PaymentAdditionalInfoRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentItemRequest;
import com.mercadopago.client.payment.PaymentOrderRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.client.payment.PaymentReceiverAddressRequest;
import com.mercadopago.client.payment.PaymentShipmentsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceTrackRequest;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.MPHttpClient;
import com.mercadopago.net.MPRequest;
import com.mercadopago.net.MPResponse;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.sistema.examenes.modelo.usuario.Usuario;

public class TESTMPService implements MPHttpClient  {

	private String domino = "http://localhost:8080";
	
	private PaymentItemRequest crearItem(Licencia licencia) {
		double precio = Integer.valueOf(licencia.getMonto()).doubleValue();
		
		PaymentItemRequest item =
		   PaymentItemRequest.builder()
		       .id("LIC-"+licencia.getId())	       
		       .title("Licencia ReserveNow "+licencia.getNombre())
		       .description(licencia.getDescripcion())
		       /*-------ACA VA UNA FOTO SI TENEMOS---------
		       .pictureUrl(
		           "https://http2.mlstatic.com/resources/frontend/statics/growth-sellers-landings/device-mlb-point-i_medium@2x.png")
		       */
		       .categoryId("License")
		       .quantity(1)
		       .unitPrice(BigDecimal.valueOf(precio))
		       .build();
		
		return item;
	}
	
	private PaymentAdditionalInfoPayerRequest crearInformacionCliente(Usuario u) {
		PaymentAdditionalInfoPayerRequest cliente = PaymentAdditionalInfoPayerRequest.builder()
				.firstName(u.getNombre())
				.lastName(u.getApellido())
				.build();
		return cliente;
	}
	
	public void sosFeliz() throws Exception {
		try {
			MercadoPagoConfig.setAccessToken("TEST-7405079288753970-041215-b9acfd241ad71407ba522bda572489f1-554532024");
	
			PreferenceClient client = new PreferenceClient();
	
			List<PreferenceItemRequest> items = new ArrayList<>();
			PreferenceItemRequest item =
			   PreferenceItemRequest.builder()
			       .title("Dummy Title")
			       .description("Dummy description")
			       .pictureUrl("http://www.myapp.com/myimage.jpg")
			       .quantity(1)
			       .currencyId("ARS")
			       .unitPrice(new BigDecimal("1000"))
			       .build();
			items.add(item);
			/*
			List<PreferenceTrackRequest> tracks = new ArrayList<>();
			PreferenceTrackRequest googleTrack = PreferenceTrackRequest.builder().type("google_ad").build();
	
			tracks.add(googleTrack);*/
	
			PreferenceRequest request = PreferenceRequest.builder().items(items)./*tracks(tracks).*/build();
			Preference p = client.create(request);
			System.out.println(p.getInitPoint());
			System.out.println(p.getSandboxInitPoint());
			//return p;
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
	
	public Payment crearPagoMPLicencia(Usuario u, Licencia licencia, BigDecimal comision) throws Exception {
		try {
			
			Map<String, String> customHeaders = new HashMap<>();
			//customHeaders.put("Content-Type", "application/json");
		    customHeaders.put("x-idempotency-key", "16SD4eahAftbnbh8GKRavB93Z2aEv649pa81NB2");
		    customHeaders.put("Authorization", "Bearer TEST-7405079288753970-041215-b9acfd241ad71407ba522bda572489f1-554532024");
		 
			MPRequestOptions requestOptions = MPRequestOptions.builder()
			    .customHeaders(customHeaders)
			    .build();
		
			PaymentClient client = new PaymentClient();
			/*
			List<PaymentItemRequest> items = new ArrayList<>();
			PaymentItemRequest item = crearItem(licencia);
			items.add(item);
			
			PaymentCreateRequest createRequest =
					   PaymentCreateRequest.builder()
					       .additionalInfo(
					           PaymentAdditionalInfoRequest.builder()
					               .items(items)
					               .payer(crearInformacionCliente(u))
					               .build())
					       .applicationFee(comision)
					       .binaryMode(true)//para que el pago solo este en estado aceptado o rechazado (sin estado en proceso)
					       .description(item.getDescription())
					       .installments(1)
					       .notificationUrl(domino+"/webhook/mp/payment-state")//url webhook
					       .payer(PaymentPayerRequest.builder().entityType("individual").email(u.getEmail()).type("customer").build())//aca va guest para las señas
					       .paymentMethodId("Cvu")
					       .transactionAmount(item.getUnitPrice())
					       //.token("ff8080814c11e237014c1ff593b57b4d")
					       .build();
			*/
			List<PaymentItemRequest> items = new ArrayList<>();

			PaymentItemRequest item =
			   PaymentItemRequest.builder()
			       .title("Point Mini")
			       .description("Producto Point para cobros con tarjetas mediante bluetooth")
			       .pictureUrl(
			           "https://http2.mlstatic.com/resources/frontend/statics/growth-sellers-landings/device-mlb-point-i_medium@2x.png")
			       .categoryId("electronics")
			       .quantity(1)
			       .unitPrice(new BigDecimal("58.8"))
			       .build();

			items.add(item);

			PaymentCreateRequest createRequest =
			   PaymentCreateRequest.builder()
			       .additionalInfo(
			           PaymentAdditionalInfoRequest.builder()
			               .items(items)
			               .payer(
			                   PaymentAdditionalInfoPayerRequest.builder()
			                       .firstName("Test")
			                       .lastName("Test")
			                       .phone(
			                           PhoneRequest.builder().areaCode("11").number("987654321").build())
			                       .build())
			               .shipments(
			                   PaymentShipmentsRequest.builder()
			                       .receiverAddress(
			                           PaymentReceiverAddressRequest.builder()
			                               .zipCode("12312-123")
			                               .stateName("Rio de Janeiro")
			                               .cityName("Buzios")
			                               .streetName("Av das Nacoes Unidas")
			                               .streetNumber("3003")
			                               .build())
			                       .build())
			               .build())
			       .description("Payment for product")
			       .externalReference("MP0001")
			       .installments(1)
			       .order(PaymentOrderRequest.builder().type("mercadolibre").build())
			       .payer(PaymentPayerRequest.builder().entityType("individual").type("customer").build())
			       .paymentMethodId("visa")
			       .transactionAmount(new BigDecimal("58.8"))
			       .build();
			
			Payment payment = client.create(createRequest, requestOptions);
			return payment;
		} catch (MPApiException ex) {
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
	public MPResponse send(MPRequest request) throws MPException, MPApiException {
		return null;
	}
	
	public void enviarApiMP(Payment pagoMP) {
		try {
            String url = "https://api.mercadopago.com/v1/payments";
            String token = "TEST-7405079288753970-041215-b9acfd241ad71407ba522bda572489f1-554532024"; // Reemplaza con tu token de acceso

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);

            // Encabezados
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("X-Idempotency-Key", "16SD4eahAftbnbh8GKRavB93Z2aEv649pa81NB2");
            httpPost.addHeader("Authorization", "Bearer " + token);

            // Cuerpo de la solicitud
            String requestBody = "";

            httpPost.setEntity(new StringEntity(requestBody));

            HttpResponse response = httpClient.execute(httpPost);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            System.out.println("Response status code: " + response.getStatusLine().getStatusCode());
            System.out.println("Response body: " + result.toString());

            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
