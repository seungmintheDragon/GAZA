package com.idle.gaza.api.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idle.gaza.api.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

/*
* Redis 구독 서비스
* Redis에 메시지 발행이 될 때까지 대기했다가 메시지가 발행되면 해당 메시지를 읽어 처리함
* */
@Log4j2
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messageTemplate;

    /*
    * redis에서 메시지가 발행(publish)되면 대기하고 있던 메서드가 해당 메시지를 받아 처리함
    * */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try{
            //redis에서 발행된 데이터를 받아서 deserialize
            String publishMessage = (String)redisTemplate.getStringSerializer().deserialize(message.getBody());
            //ChatMessage 객체로 맵핑
            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            //websocket 구독자에서 채팅 메시지를 보낸다
            messageTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getChatRoomId(), chatMessage);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
