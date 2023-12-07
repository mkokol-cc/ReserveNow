package com.sistema.examenes.modelo.usuario.pagos;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.repositorios.LicenciaRepository;
import com.sistema.examenes.repositorios.UsuarioRepository;

@RestController
@RequestMapping("/mp")
public class MPController {
	
	@Autowired
	private PagoService pagoService;
	
	@Autowired
	private LicenciaRepository licRepo;
	
	@Autowired
	private UsuarioRepository usuarioRepo;


    @PostMapping("webhook/payment-state/{id}")
    public void actualizarEstadoPago(@PathVariable("id") Long pago, @RequestBody MPNotificationDTO cuerpo) throws MPException, MPApiException {
    	
    	/* PARA VER EL OBJETO CUERPO DE LA REQUEST
    	ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convertir el objeto a formato JSON
            String json = objectMapper.writeValueAsString(cuerpo);
            System.out.println("Objeto recibido en formato JSON:");
            System.out.println(json);
        } catch (JsonProcessingException e) {
            // Manejar excepciones de serialización a JSON
            e.printStackTrace();
        }
        */
    	/*
    	ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(cuerpo);// Convertir cuerpo a JSON
            // Obtener el valor de 'data.id' (id de Payment)
            JsonNode dataNode = jsonNode.get("data");
            Long idDelPago = dataNode.get("id").asLong();
    		PaymentClient client = new PaymentClient();
    		Payment pagoMP = client.get(idDelPago);
    		System.out.println("El estado del pago es "+pagoMP.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    	System.out.println("Id Pago: "+cuerpo.getData().getId());
		PaymentClient client = new PaymentClient();
		Payment pagoMP = client.get(cuerpo.getData().getId());
		System.out.println("El estado del pago es "+pagoMP.getStatus());
    }
    
    @GetMapping("prueba")
    public void probarIMPL() throws Exception {
    	Licencia lprueba = licRepo.getById(1L);
    	//lprueba.setId(1L);
    	//lprueba.setNombre("LICENCIA DE PRUEBA");
    	//lprueba.setDescripcion("es para probar la implementacion");
    	//lprueba.setMonto(9000);
    	
    	//Licencia guardada = licRepo.save(lprueba);
    	
    	Usuario uprueba = usuarioRepo.getById(2L);
    	//uprueba.setNombre("Cosme");
    	//uprueba.setApellido("Fulanito");
    	//uprueba.setEmail("emaildeprueba@gmail.com");
    	
    	Pago pago = pagoService.nuevoPago(lprueba, uprueba);
    	
    	//TESTMPService ser = new TESTMPService();
    	//ser.sosFeliz();
    	//ser.enviarApiMP(ser.sosFeliz()/*ser.crearPagoMPLicencia(uprueba, lprueba, new BigDecimal("0"))*/);
    }
    
}
