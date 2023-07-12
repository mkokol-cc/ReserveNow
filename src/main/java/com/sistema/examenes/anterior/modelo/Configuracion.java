package com.sistema.examenes.anterior.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usuario")
public class Configuracion {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
    private String urlInstagram;
    private String urlGoogleMaps;
    private String urlYoutube;
    private String urlTwitter;
    private String urlWp;
    
    private String apiWp;
    
    private String apiMp1;
    private String apiMp2;
    
    
}
