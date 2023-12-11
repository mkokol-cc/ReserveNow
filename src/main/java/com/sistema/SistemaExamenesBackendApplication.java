package com.sistema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sistema.anterior.modelo.Estado;
import com.sistema.anterior.repositorios.EstadoRepository;

//import com.mercadopago.MercadoPago;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sistema"})
public class SistemaExamenesBackendApplication implements CommandLineRunner {

	@Autowired
	private EstadoRepository estadoRepo;

	public static void main(String[] args) {
		SpringApplication.run(SistemaExamenesBackendApplication.class, args);
	}
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:4200").allowedMethods("*");
			}
		};
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		if(estadoRepo.findAll().size()==0) {
			Estado e1 = new Estado("Pendiente de Pago","",1L,false);//Pte Pago
			Estado e2 = new Estado("Activa","",2L,false);//Activo
			Estado e3 = new Estado("Completada","",3L,true);//Completada
			Estado e4 = new Estado("Cancelada","",4L,true);//Cancelada
			estadoRepo.save(e1);
			estadoRepo.save(e2);
			estadoRepo.save(e3);
			estadoRepo.save(e4);	
		}
		//EstadoRepository
		//MercadoPago.SDK.setAccessToken("TEST-7405079288753970-041215-b9acfd241ad71407ba522bda572489f1-554532024");
	}



}
