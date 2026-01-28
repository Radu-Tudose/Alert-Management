package org.example;

import java.util.ArrayList;
import java.util.List;

public class ResourceGroup {
    private final List<User> members;
    private String ipAddress;

    private Server server;
    private final List<Alert> receivedAlerts = new ArrayList<>();

    public void update() {
        if (server == null) return;

        Alert a = server.getLastAlert();
        receivedAlerts.add(a);
    }

    public void attachServer(Server s) {
        this.server = s;
        if (s != null) {
            server.attachObserver(this);
        }
    }

    ResourceGroup(String ipAddress) {
        this.ipAddress = ipAddress;
        this.members = new ArrayList<>();
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public List<User> getMembers() {
        return this.members;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void addMember(User u) {
        if (u == null) return;
        members.add(u);
    }

    public void removeMember(User u) {
        if (u == null) return;
        members.remove(u);
    }
}
