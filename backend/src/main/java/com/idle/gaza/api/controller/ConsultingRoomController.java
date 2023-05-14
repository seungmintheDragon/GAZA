package com.idle.gaza.api.controller;

import java.util.List;
import java.util.Map;

import com.idle.gaza.api.model.ChatRoomRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.idle.gaza.db.entity.ConsultingRoom;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/map")
@Log4j2
public class ConsultingRoomController {

//	private final MapRoomRepository mapRoomRepository;
	private final ChatRoomRepository chatRoomRepository;
	@Autowired
	private HttpSession httpSession;

	// 모든 채팅방 목록 반환
	@GetMapping("/rooms")
	@ResponseBody
	public List<ConsultingRoom> room() {
	    return chatRoomRepository.findAllRoom();
	}
	 
	// 채팅방 생성
	@PostMapping("/room")
	@ResponseBody
	public ConsultingRoom createRoom(@RequestBody String name) {
		ConsultingRoom room = chatRoomRepository.createRoom(name);
	    return room;
	}

	 // 특정 채팅방 조회
	@GetMapping("/room/{roomId}")
	@ResponseBody
	public ResponseEntity<?> roomInfo(@PathVariable String roomId) {
		System.out.println("들어온 룸 아이디 : "+roomId);
	    ConsultingRoom room = chatRoomRepository.findByRoomId(roomId);
		if (room == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    return new ResponseEntity<>(room,HttpStatus.OK);
	}
	 
	public ConsultingRoomController(ChatRoomRepository chatRoomRepository) {
		super();
		this.chatRoomRepository = chatRoomRepository;
	}


	@PostMapping("/session")
	public ResponseEntity<?> setSession(@RequestBody Map<String, String> maps){
		String sessionId = maps.get("sessionId");
		String key = maps.get("reservationId");

		httpSession.setAttribute(key, sessionId);
		log.info("key= " +httpSession.getAttribute(key) );
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/session")
	public ResponseEntity<?> getSession(@RequestParam String reservationId){
		String key = reservationId;
		//log.info("key" +httpSession.getAttribute(key) );
		Object value = httpSession.getAttribute(key);
		return new ResponseEntity<>(value, HttpStatus.OK);
	}


}