package com.sistema.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.sistema.anterior.modelo.Notificacion;

@Service
public class WebSocketService {

	private final SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	public WebSocketService(final SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}
	
	public void sendMessage(final String topicSuffix) {
		messagingTemplate.convertAndSend("/topic/"+topicSuffix,"Mensaje Por Defecto.");
	}
	
	public void sendNotificacion(final String topicSuffix, Notificacion notificacion) {
		
		messagingTemplate.convertAndSend("/topic/"+topicSuffix,notificacion);
	}
}
