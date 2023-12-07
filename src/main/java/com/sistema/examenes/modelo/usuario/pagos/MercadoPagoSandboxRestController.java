package com.sistema.examenes.modelo.usuario.pagos;

import java.sql.Date;
import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.GsonBuilder;
import com.mercadopago.exceptions.MPException;
//import com.mercadopago.resources.Payment;
//import com.mercadopago.resources.Preference;
//import com.mercadopago.resources.datastructures.preference.BackUrls;
//import com.mercadopago.resources.datastructures.preference.Item;
import com.sistema.examenes.configuraciones.JwtUtils;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.repositorios.LicenciaRepository;
import com.sistema.examenes.repositorios.PagoRepository;
import com.sistema.examenes.repositorios.UsuarioRepository;
import com.sistema.examenes.servicios.UsuarioService;
import com.sistema.examenes.servicios.impl.UserDetailsServiceImpl;

import io.jsonwebtoken.JwtException;
//import lombok.var;

@RestController
@CrossOrigin("*")
public class MercadoPagoSandboxRestController {
	/*
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	@Autowired
	private PagoRepository pagoRepo;
	@Autowired
	private LicenciaRepository licenciaRepo;
	
	
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;
	
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PreferenceService preferenceService;

    public MercadoPagoSandboxRestController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @PostMapping(value = "/mp/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createPreference(
            @RequestBody NewPreferenceDTO preferenceDTO
            ) throws MPException {
        return this.preferenceService.create(preferenceDTO);
    }
    
    
    
    @GetMapping(value="/mp/createAndRedirect/{id}")
    public String createAndRedirect(@PathVariable int id,@RequestHeader("Authorization") String tokenHeader) throws MPException, JsonProcessingException{
    	try {
    		//obtener el usuario
    		String token = tokenHeader.substring(7); // Elimina el prefijo 'Bearer ' del token
            UserDetails userDetails =  this.userDetailsService.loadUserByUsername(jwtUtils.extractUsername(token));
            jwtUtils.validateToken(token,userDetails);
            Usuario u = usuarioService.obtenerUsuario(jwtUtils.extractUsername(token));
            
            System.out.println(u.getEmail());
        	
        	
        	//crear nuevo pago de la licencia ID en estado PENDIENTE
        	Licencia l = licenciaRepo.getById((long)id);
        	Pago p = new Pago();
        	Date fechaActual = Date.valueOf(LocalDate.now());
        	p.setFecha(fechaActual);
        	
        	Date fechaVto = Date.valueOf(LocalDate.now().plusDays(l.getDias()));
        	p.setFechaVto(fechaVto);
        	
        	p.setLicencia(l);
        	p.setMonto(l.getMonto());
        	p.setUsuario(u);
        	p.setEstado(0);
        	
        	//p.set
        	
        	p = pagoRepo.save(p);
        	
        	//ARMAMOS EL LINK DE PAGO
        	Preference preference = new Preference();
        	
        	String link = "http://localhost:4200/payment/"+p.getId();
        	preference.setBackUrls(
        			//VAMOS A PONER UN SOLO LINK EN EL FRONT QUE LO QUE VA A HACER ES
        			//1-mandar solicitud de recarga de estado de Pago con el id de pago y el merchant-id como parametro
        			//2-el resultado se actualizara en el Pago y va a devolver el objeto Pago
        			//3-segun el estado (ya actualizado) del objeto que devuelve en el front mostramos el mensaje de
        			//que el pago id... esta en Estado ...
        		
        			*//*
            	new BackUrls().setFailure("http://localhost:4200/payment-failure")
            	.setPending("http://localhost:4200/payment-pending")
           		.setSuccess("http://localhost:4200/payment-success")
           		*/
	/*
           		new BackUrls().setFailure(link).setPending(link).setSuccess(link)
        	)
        	//.setNotificationUrl("http://localhost:8080/mp/refresh-payment-state/"+p.getId()) - ESTO ES PARA CUANDO ESTE CONFIGURADO EL WEBHOOK Y SE REALIZEN ACTUALIZACIONES AUTOMATICAS
        	;
        	Item item = new Item();
        	item.setTitle(l.getNombre() + " - COD:" + p.getId())
        	.setQuantity(1).setUnitPrice((float)(l.getMonto()/100))
        	.setDescription(l.getDescripcion() + " - Código de Compra: " + p.getId())
        	.setCurrencyId("ARS");
        	preference.appendItem(item);
        	//var result = preference.save();
        	Preference result = preference.save();
        	// result.getPayer();
        	//System.out.println("PAYER");
        	//System.out.println(result.getPayer());
        	//System.out.println("PREFERENCE");
        	//System.out.println(result.getId());
        	//p.setMpPayerId(result.getPayer().);
        	// result.getId()
        	// result.get aplicationID pero creo que aca va NULL
        	// SE DEBEN GUARDAR ESTOS DATOS EN EL PAGO PARA DESPUES PODER ACCEDER A SU ESTADO DESDE UN POST A LA API DE MP
        	
        	// https://api.mercadopago.com/merchant_orders/
        	
        	//System.out.println(result.getSandboxInitPoint());
        	//return "redirect"+result;
        	ObjectMapper mapper = new ObjectMapper();
        	ObjectNode linkRes = JsonNodeFactory.instance.objectNode();
        	linkRes.put("link", result.getSandboxInitPoint());
        	String json = mapper.writeValueAsString(linkRes);
    	    return json;
        	
    	}catch(JwtException e) {
            return null;
        }
    }
    
    *//*
    @PostMapping(value="/mp/refresh-payment-state/{idPago}")
    private ResponseEntity actualizarEstadoPago() {
    	MercadoPago.SDK.getAccessToken();
    }
    *//*
    
    
    
    
    @GetMapping(value="/mp/success")
    public String success(HttpServletRequest request,
            @RequestParam("collection_id") String collectionId,
            @RequestParam("collection_status") String collectionStatus,
            @RequestParam("external_reference") String externalReference,
            @RequestParam("payment_type") String paymentType,
            @RequestParam("merchant_order_id") String merchantOrderId,
            @RequestParam("preference_id") String preferenceId,
            @RequestParam("site_id") String siteId,
            @RequestParam("processing_mode") String processingMode,
            @RequestParam("merchant_account_id") String merchantAccountId,
            Model model
    		)throws MPException {
    	//var payment = Payment.findById(collectionId);
    	Payment payment = Payment.findById(collectionId);
    	System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(payment));
    	model.addAttribute("payment",payment);
    	return "ok";
    }
    
    @GetMapping(value="/mp/failure")
    public String failure(HttpServletRequest request,
            @RequestParam("collection_id") String collectionId,
            @RequestParam("collection_status") String collectionStatus,
            @RequestParam("external_reference") String externalReference,
            @RequestParam("payment_type") String paymentType,
            @RequestParam("merchant_order_id") String merchantOrderId,
            @RequestParam("preference_id") String preferenceId,
            @RequestParam("site_id") String siteId,
            @RequestParam("processing_mode") String processingMode,
            @RequestParam("merchant_account_id") String merchantAccountId,
            Model model
    		)throws MPException {
    	//var payment = Payment.findById(collectionId);
    	Payment payment = Payment.findById(collectionId);
    	System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(payment));
    	model.addAttribute("payment",payment);
    	return "no ok";
    }
    
    */
    
}
