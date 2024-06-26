package com.sistema.notificaciones;

import java.sql.Date;
import java.time.LocalDate;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sistema.configuraciones.JwtUtils;
import com.sistema.modelo.pagosMP.Licencia;
import com.sistema.modelo.pagosMP.Pago;
import com.sistema.modelo.usuario.JwtResponse;
import com.sistema.modelo.usuario.Usuario;
import com.sistema.repositorios.LicenciaRepository;
import com.sistema.repositorios.PagoRepository;
import com.sistema.repositorios.UsuarioRepository;
import com.sistema.servicios.UsuarioService;
import com.sistema.servicios.impl.UserDetailsServiceImpl;

import io.jsonwebtoken.JwtException;

@RestController
@RequestMapping("/notifications")
@CrossOrigin("*")
public class NotificationsController{

    @Autowired
    private JavaMailSender emailSender;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private UsuarioRepository usuarioRepo;
    
    @Autowired
    private PagoRepository pagoRepo;
    
    @Autowired
    private LicenciaRepository licenciaRepo;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private NotificationsService notificationsService;
    
    /*
     * VAMOS A AVISAR:
     * pagos de licencias
     * vencimiento de licencias
     * (si lo tiene activado) notificaciones de reservas al Administrador
     * notificaciones de reservas al reservante
     * notificacion de cambio de estado de reserva a reservante
     * 
     * VAMOS A ENVIAR MENSAJE POR:
     * validacion de email
     * validacion de numero telefonico?
     * 
     * */
    
    private String domain = "http://localhost:4200";
    
    @PostMapping("/wellcome/{email}")
    public ResponseEntity<?> enviarEmailBienvenida(@PathVariable("email") String to){        
    	try {
	    	notificationsService.enviarEmailBienvenida(usuarioService.obtenerUsuarioActual());
	        return ResponseEntity.ok("Se envio correctamente el mail");
        }catch(Exception e) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    /*
    @PostMapping("/bienvenida/{email}")
    public ResponseEntity<JwtResponse> mailBienvenida(@PathVariable("email") String to) throws Exception {        
    	String token = generarTokenBienvenida(to);
        String link = domain+"/verify-email/";
        //----------
        String htmlMsg = "<h3>Hola Usuario</h3><p>Bienvenido a Calendario, haga click "
        		+ "<a href=\""+link+token+"\">AQUI</a>"
        		+ " para habilitar su cuenta.</p>";
        sendEmail(to,"Bienvenido",htmlMsg);
        
        return ResponseEntity.ok(new JwtResponse(token));
    }*/
    
    @PostMapping("/reestablecer-clave/{email}")
    public ResponseEntity<?> mailReestablecerClave(@PathVariable("email") String to){
    	Usuario u = usuarioService.obtenerUsuario(to);
    	//System.out.println("HE ENTRADO");
    	if(u!=null && u.isEnabled()) {
    		try {
    			notificationsService.enviarEmailReestablecerClave(u);
    			return ResponseEntity.ok("Se envio correctamente el mail");
    		}catch(Exception e) {
    			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    		}
    		/*
    		//PARAMETROS
            String link = domain+"/change-password/";
            String token = generarTokenBienvenida(to);
            //----------
            String htmlMsg = "<h3>Hola Usuario</h3><p>Para poder recuperar tu contraseña haga click</p>"
            		+ "<p><a href=\""+link+token+"\">Aquí!</a></p>";
            sendEmail(to,"Reestablecer Contraseña",htmlMsg);
            */
    	}else {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario no encontrado");
    	}
    }
    /*
    @GetMapping("/validar-email/{token}")
    private ResponseEntity<?> validarEmail(@PathVariable("token") String token) {
        try {
        	System.out.println(1);
            //String token = tokenHeader.substring(7); // Elimina el prefijo 'Bearer ' del token
            UserDetails userDetails =  this.userDetailsService.loadUserByUsername(jwtUtils.extractUsername(token));
            jwtUtils.validateToken(token,userDetails);
            Usuario u = usuarioService.obtenerUsuario(jwtUtils.extractUsername(token));
            System.out.println(u.isEnabled());
            if(!u.isEnabled()) {
            	u.setEnabled(true);
            	u=usuarioRepo.save(u);
            }
            if(u.getPagos().size()==0) {
            	Pago p = new Pago();
            	Licencia l = licenciaRepo.getById((long)1);//obtenemos la licencia mensual
            	p.setFecha(LocalDate.now());
            	p.setLicencia(l);
            	p.setMonto(0);//monto gratuito
            	p.setUsuario(u);
            	//p.setEstado(PAGADO);
            	pagoRepo.save(p);
            	//System.out.println("TE DIMOS EL MES GRATIS");
            }
            return ResponseEntity.ok(u);
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }*/
     
    /*
    @PostMapping("/contacto/{email}")
    private ResponseEntity<?> enviarEmailContacto(@PathVariable("email") String to,@RequestBody String mensaje){
    	try {
    		String emailContacto="matias-oggero@hotmail.com";
            String htmlMsg = "<h3>Hola Estimado</h3><p>Nos ha llegado un email con este destinatario</p>"
            		+ "<p>Pronto nos contactaremos contigo!</p>";
    		sendEmail(to,"Te has comunicado con nosotros!",htmlMsg);
    		htmlMsg="<p>Email: "+to+"</p>"
            		+ "<p>Mensaje: "+mensaje+"</p>";
    		sendEmail(emailContacto,"Nuevo Mensaje de Contacto",htmlMsg);
    		return ResponseEntity.ok().build();
    	}catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    	}
    }*/
    
    /*
    private void sendEmail(String to, String subject, String htmlMsg) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        //-------------------------------------------------------
        helper.setText(htmlMsg, true); // Use this or above line.
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom("noreply@reservenow.com");
        emailSender.send(mimeMessage);
    }
    
    private String generarTokenBienvenida(String email) throws Exception {
    	Usuario u = usuarioService.obtenerUsuario(email);
    	System.out.println("el estado del ENABLED es "+u.isEnabled());
    	if(u!=null) {
    		try {
    			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(u.getUsername(),u.getPassword()));
    	        UserDetails userDetails =  this.userDetailsService.loadUserByUsername(email);
    	        String token = this.jwtUtils.generateToken(userDetails);
    	        return token;
            }catch (DisabledException exception){
                throw  new Exception("USUARIO DESHABILITADO " + exception.getMessage());
            }catch (BadCredentialsException e){
                throw  new Exception("Credenciales invalidas " + e.getMessage());
            }
    	}else {
    		throw new Exception("Usuario no encontrado");
    	}
    }
    */
    /*
    //ANDA PERO TENES QUE CAMBIAR EL TOKEN Y A CADA NUMERO QUE QUERES ENVIARLE MENSAJES TENES QUE PONERLO
    //EN LA LISTA DE CONTACTOS MANUALMENTE
     
    @PostMapping("/wpp")
    public void sendWpp(){
        try {
        	String myToken="EABW2JZCCwhAYBANXz4zZCHNkWfjJklCKwAxbFreLdh8ApSLKtchVHVEqJbRD9b0nllxkdA88yB2ctrEiA7VGGod0akXs8MCWHZBwZCuiYgYnZBp2HgB2oSZCTPh0eYAv5bY1O2DNuRrOR78lFvZBD6g0DMnnbzpLf68bWRjT1qSK9VU5BDcZChYARU7M7HG9lrzM74YHZC7pRfFGrIWtd7BuYPoVgCjCn4KsZD";
        	String idNumero="101939039559793";
        	String numeroDestino="543533451122";
        	
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://graph.facebook.com/v13.0/"+idNumero+"/messages"))
                .header("Authorization", "Bearer "+myToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{ \"messaging_product\": \"whatsapp\", \"recipient_type\": \"individual\", \"to\": \""+numeroDestino+"\", \"type\": \"template\", \"template\": { \"name\": \"hello_world\", \"language\": { \"code\": \"en_US\" } } }"))
                .build();
            HttpClient http = HttpClient.newHttpClient();
            HttpResponse<String> response = http.send(request,BodyHandlers.ofString());
            System.out.println(response.body());
           
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    */
}