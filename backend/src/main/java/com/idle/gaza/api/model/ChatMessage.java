package com.idle.gaza.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatMessage {

    private String chatRoomId;//채팅 방 번호
    private String writerName;//메시지 작성자
    private String message;//메시지
    private MessageType messageType;

    public enum MessageType {
        //입장, 채팅
        ENTER, CHAT
    }

    public ChatMessage() {
    }

    public ChatMessage(String chatRoomId, String writerName, String message, MessageType messageType) {
        this.chatRoomId = chatRoomId;
        this.writerName = writerName;
        this.message = message;
        this.messageType = messageType;
    }

}
