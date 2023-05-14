package com.idle.gaza.db.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class ConsultingRoom implements Serializable {

	private static final long serialVersionUID = 3014076092847700094L;
    private String roomId;
    private String name;
    
    public static ConsultingRoom create(String name) {
        ConsultingRoom consultingRoom = new ConsultingRoom();
        consultingRoom.roomId = UUID.randomUUID().toString();
        consultingRoom.name = name;
        return consultingRoom;
    }

	public ConsultingRoom() {
		super();
	}
    
    
}