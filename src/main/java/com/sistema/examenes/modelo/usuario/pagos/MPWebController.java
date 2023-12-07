package com.sistema.examenes.modelo.usuario.pagos;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.mercadopago.exceptions.MPException;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.repositorios.LicenciaRepository;
import com.sistema.examenes.repositorios.UsuarioRepository;

@Controller
public class MPWebController {

	@Autowired
	private PagoService pagoService;
	
	@Autowired
	private LicenciaRepository licRepo;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
    @GetMapping("/mp/generic")
    public RedirectView success(
            HttpServletRequest request,
            @RequestParam("collection_id") String collectionId,
            @RequestParam("collection_status") String collectionStatus,
            @RequestParam("external_reference") String externalReference,
            @RequestParam("payment_type") String paymentType,
            @RequestParam("merchant_order_id") String merchantOrderId,
            @RequestParam("preference_id") String preferenceId,
            @RequestParam("site_id") String siteId,
            @RequestParam("processing_mode") String processingMode,
            @RequestParam("merchant_account_id") String merchantAccountId,
            RedirectAttributes attributes)
            throws MPException {
        attributes.addFlashAttribute("genericResponse", true);
        attributes.addFlashAttribute("collection_id", collectionId);
        attributes.addFlashAttribute("collection_status", collectionStatus);
        attributes.addFlashAttribute("external_reference", externalReference);
        attributes.addFlashAttribute("payment_type", paymentType);
        attributes.addFlashAttribute("merchant_order_id", merchantOrderId);
        attributes.addFlashAttribute("preference_id",preferenceId);
        attributes.addFlashAttribute("site_id",siteId);
        attributes.addFlashAttribute("processing_mode",processingMode);
        attributes.addFlashAttribute("merchant_account_id",merchantAccountId);

        return new RedirectView("/");
    }
    
    @PostMapping("webhook/mp/payment-state/{id}")
    public void actualizarEstadoPago(@PathVariable("id") Long pago) {
    	
    }
    
    @GetMapping("mp-prueba")
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
