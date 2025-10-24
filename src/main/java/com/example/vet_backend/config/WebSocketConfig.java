package com.example.vet_backend.config;

import com.example.vet_backend.controller.SignalingController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket // WebSocket 기능을 활성화합니다.
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {


    private final SignalingController signalingHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 프론트엔드가 접속할 WebSocket 엔드포인트를 등록합니다.
        // 클라이언트가 'ws://localhost:8080/ws/signaling' 으로 접속하게 됩니다.
        registry.addHandler(signalingHandler, "/ws/signaling")
                .setAllowedOrigins("*"); // 개발 환경에서는 모든 오리진 허용
    }
}
