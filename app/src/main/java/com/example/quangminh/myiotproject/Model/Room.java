package com.example.quangminh.myiotproject.Model;

public class Room {
    String roomName;
    String temperature;
    String humidity;

    public Room() {
    }

    public Room(String roomName) {
        this.roomName = roomName;
        this.temperature = "";
        this.humidity="";
    }

    public Room(String roomName, String temperature, String humidity) {

        this.roomName = roomName;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
