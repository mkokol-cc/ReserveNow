package com.sistema.examenes.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Habilita un broker simple para enviar mensajes a los clientes
        config.setApplicationDestinationPrefixes("/ws"); // Prefijo para rutas de destino de mensajes desde el cliente al servidor
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	//.setAllowedOrigins("http://localhost:4200") para que solo ese puerto (puerto del frontend) lo pueda usar
        registry.addEndpoint("/ws-endpoint").setAllowedOrigins("http://localhost:4200").withSockJS(); // Endpoint para que los clientes se conecten
    }
}