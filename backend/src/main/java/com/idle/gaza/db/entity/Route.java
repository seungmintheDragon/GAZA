package com.idle.gaza.db.entity;

public class Route {
    public enum RouteType {
        UP, DOWN, DELETE, SAVE
    }
    private RouteType type;
    private String roomId;
    private String name;
    private String address;
    private String latitude;
    private String longitude;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public RouteType getType() {
        return type;
    }

    public void setType(RouteType type) {
        this.type = type;
    }
}
