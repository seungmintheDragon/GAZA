package com.idle.gaza.api.model;

import com.idle.gaza.api.pubsub.RedisSubscriber;
import com.idle.gaza.db.entity.ConsultingRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {

    //채팅방에 발행되는 메시지를 처리할 리스너
    private final RedisMessageListenerContainer redisMessageListenerContainer;

    //redis
    @Qualifier("RedisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;

    private HashOperations<String, String, ConsultingRoom> hashChatRoom;
    private static final String CHAT_ROOMS = "CHAT_ROOM";

    //구독 처리 서비스
    private final RedisSubscriber redisSubscriber;

    //채팅방의 대화 메시지를 발행하기 위한 redis topic 정보, 채팅방에 매칭되는 topic 정보를 Map에 넣어 roomId로 찾음
    private Map<String, ChannelTopic> topicMap;

    @PostConstruct
    private void init() {
        hashChatRoom = redisTemplate.opsForHash();
        topicMap = new HashMap<>();
    }


    /* 채팅방 전체 조회 */
    public List<ConsultingRoom> findAllRoom() {
        return hashChatRoom.values(CHAT_ROOMS);
    }

    /* 채팅방 번호로 채팅 방 조회 */
    public ConsultingRoom findByRoomId(String roomId) {
        return hashChatRoom.get(CHAT_ROOMS, roomId);
    }

    /* 채팅방 생성 : 서버 간 채팅방 공유를 위해 redis hash에 저장한다. */
    public ConsultingRoom createRoom(String name) {
        ConsultingRoom newChatRoom = ConsultingRoom.create(name);
        System.out.println(newChatRoom.getRoomId());
        hashChatRoom.put(CHAT_ROOMS, newChatRoom.getRoomId(), newChatRoom);

        return newChatRoom;
    }


    /*
    * 채팅방 입장 : redis에 topic을 만들고, pub/sub 통신을 하기 위해 리스너를 설정함
    * */
    public void enterChatRoom(String roomId){
        ChannelTopic topic = topicMap.get(roomId);
        if(topic == null){
            topic = new ChannelTopic(roomId);
            redisMessageListenerContainer.addMessageListener(redisSubscriber, topic);
            topicMap.put(roomId, topic);
        }
    }

    public ChannelTopic getTopic(String roomId){
        return topicMap.get(roomId);
    }

}
