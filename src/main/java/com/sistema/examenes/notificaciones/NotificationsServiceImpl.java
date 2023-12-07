package com.sistema.examenes.notificaciones;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import com.sistema.examenes.anterior.modelo.Reserva;
import com.sistema.examenes.configuraciones.JwtUtils;
import com.sistema.examenes.modelo.usuario.Usuario;
import com.sistema.examenes.servicios.impl.UserDetailsServiceImpl;

public class NotificationsServiceImpl implements NotificationsService {

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtUtils jwtUtils;
    
    private String dominio = "http://localhost:4200";
    
    private String mensajeHTMLBienvenida = "";
    private String mensajeHTMLVtoLicencia = "";
    private String mensajeHTMLPagoCorrecto = "";
    private String mensajeHTMLNuevaReserva = "";
    private String mensajeHTMLReestablecerClave = "";
    
    
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
	

	@Override
	public void notificarWppNuevaReservaReservante() {
		// TODO Auto-generated method stub
		
	}
	public void enviarWppValidacionUsuario() {
		
	}
	public void notificarWppCambioEstadoReserva() {
		
	}
    
	@Override
	public void enviarEmailValidacionUsuario(Usuario usuario) throws Exception {
		String header = generarHeader("Validá tu Correo Electrónico");
		String cuerpo = "Estas a un paso de registrarte."
		+generarBotonLink(dominio+"/validate-email/"+generarToken(usuario),"Click Aquí");
		String footer = generarFooter();
		enviarEmail(usuario.getEmail(),header+cuerpo+footer,"Validacion Correo Electrónico");
	}
	
	@Override
	public void notificarVencimientoLicencia(Usuario usuario) throws Exception {
		String header = generarHeader("Renova tu Licencia");
		String cuerpo = "Tu licencia se ha vencido, renuevala!."+generarBotonLink(dominio+"/admin","Renovar Licencia");
		String footer = generarFooter();
		enviarEmail(usuario.getEmail(),header+cuerpo+footer,"Renovar Licencia");
	}
	
	@Override
	public void notificarPagoCorrectoLicencia(Usuario usuario) throws Exception {
		String header = generarHeader("Gracias por Elegirnos!");
		String cuerpo = "Tu pago se ha procesado correctamente!";
		String footer = generarFooter();
		enviarEmail(usuario.getEmail(),header+cuerpo+footer,"Pago Correcto");
	}
	
	/* EL RESERVANTE NO TIENE MAIL POR EL MOMENTO
	public void notificarWppNuevaReservaReservante(Reserva r) throws Exception {
		String header = generarHeader("Datos de tu Reserva");
		String cuerpo = r.getAsignacionTipoTurno().getTipoTurno().getNombre();
		String footer = generarFooter();
		enviarEmail(r.getReservante()..getEmail(),header+cuerpo+footer,"¡Gracias por Reservar!");
	}
	public void notificarEmailCambioEstadoReserva() {
		
	}
	*/
	
	@Override
	public void notificarEmailNuevaReservaAdministrador(Usuario usuario, Reserva r) throws Exception {
		String header = generarHeader("Se registró una nueva reserva");
		String cuerpo = r.getAsignacionTipoTurno().getTipoTurno().getNombre();
		String footer = generarFooter();
		enviarEmail(usuario.getEmail(),header+cuerpo+footer,"¡Nueva Reserva!");
	}
	
	@Override
	public void enviarEmailReestablecerClave(Usuario usuario) throws Exception {
		String header = generarHeader("Reestablecer Contraseña");
		String cuerpo = generarBotonLink(dominio+"/reestablecer-clave/"+generarToken(usuario),"Nueva Contraseña");
		String footer = generarFooter();
		enviarEmail(usuario.getEmail(),header+cuerpo+footer,"Reestablecer Contraseña");
	}
	
	
	
	private void enviarWhatsApp(String destinatiario, String mensaje) throws Exception {
		try {
        	String myToken="EABW2JZCCwhAYBANXz4zZCHNkWfjJklCKwAxbFreLdh8ApSLKtchVHVEqJbRD9b0nllxkdA88yB2ctrEiA7VGGod0akXs8MCWHZBwZCuiYgYnZBp2HgB2oSZCTPh0eYAv5bY1O2DNuRrOR78lFvZBD6g0DMnnbzpLf68bWRjT1qSK9VU5BDcZChYARU7M7HG9lrzM74YHZC7pRfFGrIWtd7BuYPoVgCjCn4KsZD";
        	String idNumero="101939039559793";
        	//String numeroDestino="543533451122";
        	
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://graph.facebook.com/v17.0/"+idNumero+"/messages"))
                .header("Authorization", "Bearer "+myToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{ \"messaging_product\": \"whatsapp\", \"recipient_type\": \"individual\", \"to\": \""+destinatiario+"\", \"type\": \"template\", \"template\": { \"name\": \"hello_world\", \"language\": { \"code\": \"en_US\" } } }"))
                .build();
            HttpClient http = HttpClient.newHttpClient();
            HttpResponse<String> response = http.send(request,BodyHandlers.ofString());
            System.out.println(response.body());
           
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new Exception(e.getMessage());
        }
	}
	
	private void enviarEmail(String destinatario, String mensajeHTML, String asunto) throws Exception {
        try {
    		MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            //-------------------------------------------------------
			helper.setText(mensajeHTML, true);
			helper.setTo(destinatario);
	        helper.setSubject(asunto);
	        helper.setFrom("noreply@reservenow.com");
	        emailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new Exception(e.getMessage());
		}
	}
	
	private String generarHeader(String titulo) {
	    return "<!DOCTYPE html>\r\n"
	    		+ "<html lang=\"en\">\r\n"
	    		+ "<head>\r\n"
	    		+ "  <meta charset=\"UTF-8\">\r\n"
	    		+ "  <title>Correo electrónico</title>\r\n"
	    		+ "</head>\r\n"
	    		+ "<body style=\"margin: 0; padding: 0; font-family: Arial, sans-serif;\">\r\n"
	    		+ "\r\n"
	    		+ "  <table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\r\n"
	    		+ "    <tr>\r\n"
	    		+ "      <td style=\"padding: 20px 0; text-align: center; background-color: #f8f8f8;\">\r\n"
	    		+ "        <h1 style=\"margin: 0; font-size: 24px;\">"+titulo+"</h1>\r\n"
	    		+ "      </td>\r\n"
	    		+ "    </tr>\r\n"
	    		+ "  </table>\r\n"
	    		+ "\r\n";
	}
	
	private String generarFooter() {
	    return ""
	    		+ "  <table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\r\n"
	    		+ "    <tr>\r\n"
	    		+ "      <td style=\"text-align: center; padding: 20px 0; background-color: #f8f8f8;\">\r\n"
	    		+ "        <p style=\"margin: 0; font-size: 14px;\">Este es el pie de página del correo electrónico.</p>\r\n"
	    		+ "        <p style=\"margin: 5px 0 0; font-size: 12px;\">© 2023 Mi Empresa. Todos los derechos reservados.</p>\r\n"
	    		+ "      </td>\r\n"
	    		+ "    </tr>\r\n"
	    		+ "  </table>"
	    		+ "</body>\r\n"
	    		+ "</html>";
	}
	
	private String generarBotonLink(String link, String textoBtn) {
		return "  <table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\r\n"
				+ "    <tr>\r\n"
				+ "      <td style=\"padding: 20px 0; text-align: center;\">\r\n"
				+ "        <!-- Botón como enlace -->\r\n"
				+ "        <a href=\""+link+"\" target=\"_blank\" style=\"display: inline-block; padding: 10px 20px; background-color: #007bff; color: #ffffff; text-decoration: none; border-radius: 5px; font-weight: bold;\">"+textoBtn+"</a>\r\n"
				+ "      </td>\r\n"
				+ "    </tr>\r\n"
				+ "  </table>";
	}
	
	private String generarToken(Usuario u) throws Exception {
    	if(u!=null/* && !u.isEnabled()*/) {
    		try {
    			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(u.getUsername(),u.getPassword()));
    	        UserDetails userDetails =  this.userDetailsService.loadUserByUsername(u.getEmail());
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

	
}
