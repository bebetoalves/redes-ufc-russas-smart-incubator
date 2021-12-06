package com.net.client;

public enum ClientType {
    SENSOR("SENR"), ACTUATOR("ACTR"), MONITOR("MOTR");

    private final String value;

    ClientType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
