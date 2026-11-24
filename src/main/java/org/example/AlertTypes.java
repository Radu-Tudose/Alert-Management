package org.example;

public enum AlertTypes {
    ANOMALY("anomaly"),
    ADVISORY("advisory");


    private final String value;

    AlertTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
