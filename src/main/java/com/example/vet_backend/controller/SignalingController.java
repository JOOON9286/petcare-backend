package com.example.vet_backend.controller; // 패키지 경로 변경

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class SignalingController extends TextWebSocketHandler { // 클래스 이름 변경

    // 방 관리 맵: reservationId(방 ID) -> Map of WebSocketSession
    private final Map<String, Map<String, WebSocketSession>> rooms = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper; // JSON 파싱을 위한 ObjectMapper

    // 세션 ID와 방 ID(Reservation ID)를 매핑하는 맵
    private final Map<String, String> sessionToRoomId = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 연결 후 특별한 동작 없음. 클라이언트가 'request-call' 메시지를 보낼 때 방에 추가됨.
        System.out.println("WebSocket 연결 수립: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Map<String, Object> messageMap = objectMapper.readValue(payload, Map.class);

        String type = (String) messageMap.get("type");
        String reservationId = (String) messageMap.get("reservationId");

        if (reservationId == null) {
            System.err.println("ERROR: reservationId가 메시지에 없습니다.");
            return;
        }

        // 1. 방 참여 요청 처리 (Initial Call Request)
        if ("request-call".equals(type) || "offer".equals(type) || "answer".equals(type) || "ice-candidate".equals(type)) {
            // ICE Candidate 메시지도 방 참여를 보장해야 하므로 조건에 추가
            joinRoom(session, reservationId);
        }

        // 2. 같은 방의 다른 피어에게 메시지 중계 (Signaling)
        relayMessage(session, reservationId, payload);
    }

    private void joinRoom(WebSocketSession session, String reservationId) {
        // 방이 없으면 새로 생성
        rooms.putIfAbsent(reservationId, new ConcurrentHashMap<>());

        // 방에 세션 추가
        Map<String, WebSocketSession> room = rooms.get(reservationId);
        if (!room.containsKey(session.getId())) {
            room.put(session.getId(), session);
            sessionToRoomId.put(session.getId(), reservationId);
            System.out.println("Session " + session.getId() + " joined room: " + reservationId);
        }
    }

    private void relayMessage(WebSocketSession senderSession, String reservationId, String payload) throws IOException {
        Map<String, WebSocketSession> room = rooms.get(reservationId);
        if (room == null) return;

        // 같은 방의 모든 세션(나 자신 제외)에게 메시지 중계
        for (WebSocketSession receiverSession : room.values()) {
            if (!receiverSession.getId().equals(senderSession.getId()) && receiverSession.isOpen()) {
                receiverSession.sendMessage(new TextMessage(payload));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String reservationId = sessionToRoomId.remove(session.getId());
        if (reservationId != null) {
            Map<String, WebSocketSession> room = rooms.get(reservationId);
            if (room != null) {
                room.remove(session.getId());
                System.out.println("Session " + session.getId() + " left room: " + reservationId);

                // 방에 남은 세션이 없으면 방 자체를 제거
                if (room.isEmpty()) {
                    rooms.remove(reservationId);
                    System.out.println("Room " + reservationId + " closed.");
                }
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("WebSocket 전송 오류: " + exception.getMessage());
    }
}
