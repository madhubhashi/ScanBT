package com.pontoon.scanbt;

public class DeviceInfo {
    private String distance;
    private String deviceId;
    private int rssi;
    private String timestamp;
    private String deviceName;

    public DeviceInfo() {

    }

    public DeviceInfo(String deviceId, String timestamp,  int rssi, String distance, String deviceName) {
        this.deviceId = deviceId;
        this.timestamp = timestamp;
        this.rssi = rssi;
        this.distance = distance;
        this.deviceName = deviceName;
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

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeviceName() {
        return deviceName;
    }

}
