package com.sistema.automatico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sistema.servicios.UsuarioService;
import com.sistema.servicios_v2.ReservaServiceV2;

@Service
public class ProcedimientosAutomaticosService {
	
	@Autowired
	private ReservaServiceV2 reservaService;
	@Autowired
	private UsuarioService usuarioService;
	
	//actualizar estado reservas
	//actualizar hora de reservas fijas
	//eliminar reservas mas viejas que cierto tiempo (opcional)
	//controlar vencimiento licencias
	//notificar reservas proximas a los reservantes
	
    @Scheduled(fixedRate = 60000) // Ejecución cada 60 segundos
    public void actualizarEstadoReservas() throws Exception {//actualiza tanto reservas fijas como individuales
    	reservaService.actualizarReservas();
    }
    @Scheduled(fixedRate = 86400000) // Ejecución cada 60*60*24 segundos
    public void verificarVtoLicencias() throws Exception {
    	usuarioService.verificarVtoLicencias();
    }
    @Scheduled(fixedRate = 60000) // Ejecución cada 60 segundos
    public void notificarReservasProximas() throws Exception {
    	reservaService.notificarReservas();
    }
    /*
    @Scheduled(fixedRate = 3000) // Ejecución cada 3 segundos
    public void hola(){
    	System.out.println("Hello");
    }
    */
}
