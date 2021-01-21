package com.pontoon.scanbt;

public class Snapobj {
    String rssi;
    String distance;
    String deviceId;
    String timestamp;

    public Snapobj(String rssi, String distance, String deviceId, String timestamp) {
        this.rssi = rssi;
        this.distance = distance;
        this.deviceId = deviceId;
        this.timestamp = timestamp;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
