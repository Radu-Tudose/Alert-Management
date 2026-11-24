package org.example;

public class Alert {
    private AlertTypes type;
    private Severity severity;
    private String message;
    private String ipAddress;

    Alert (AlertTypes type, Severity severity, String message, String ipAddress) {
        this.type = type;
        this.severity = severity;
        this.message = message;
        this.ipAddress = ipAddress;
    }

    public AlertTypes getType() {
        return type;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getMessage() {
        return message;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
