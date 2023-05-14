package com.idle.gaza.db.entity;

public class Point {
	public enum PointType {
		FOCUS, CLICK
	}
	private PointType type; // 메시지 타입
    private String lat;
    private String lng;
    private String roomId;
    
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public PointType getType() {
		return type;
	}

	public void setType(PointType type) {
		this.type = type;
	}
}