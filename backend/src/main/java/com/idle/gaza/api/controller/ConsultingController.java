package com.idle.gaza.api.controller;
import com.idle.gaza.api.model.ChatMessage;
import com.idle.gaza.api.model.ChatRoomRepository;
import com.idle.gaza.api.pubsub.RedisPublisher;
import com.idle.gaza.db.entity.Point;
import com.idle.gaza.db.entity.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequiredArgsConstructor
public class ConsultingController {

	private final RedisPublisher redisPublisher;
	private final ChatRoomRepository chatRoomRepository;
	private final SimpMessageSendingOperations messagingTemplate;

	// 웹소켓으로 들어오는 메시지 발행 처리.
	// /pub/chat/message로 발행 요청 -> 컨트롤러가 메세지를 받아서 처리
	// 발행되면 /sub/chat/room/{roomId}로 메세지를 send. 저게 Topic 역할.
	@MessageMapping("/map/point")
	public void point(@RequestBody Point point) {
		messagingTemplate.convertAndSend("/sub/map/room2/" + point.getRoomId(), point);
	}

	@MessageMapping("/map/route")
	public void route(@RequestBody Route route) {
		messagingTemplate.convertAndSend("/sub/map/room3/" + route.getRoomId(), route);
	}

	/*
	 * '/pub/chat/message'로 들어오는 메시징 처리
	 * */
	@MessageMapping("/chat/message")
	public void message(ChatMessage message) {

		if (ChatMessage.MessageType.ENTER.equals(message.getMessageType())) {
			chatRoomRepository.enterChatRoom(message.getChatRoomId());
			message.setMessage(message.getWriterName() + "님 입장하셨습니다.");
		}

		//websocket에 발행된 메시지를 redis로 발행한다(publish)
		redisPublisher.publish(chatRoomRepository.getTopic(message.getChatRoomId()), message);
	}
}
