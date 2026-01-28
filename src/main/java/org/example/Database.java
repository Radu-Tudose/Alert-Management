package org.example;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Database {
    private static Database instance;
    private Set<Server> servers;
    private Set<ResourceGroup> resourceGroups;
    private Set<Alert> alerts;

    private Database() {
        this.servers = new HashSet<>();
        this.resourceGroups = new HashSet<>();
        this.alerts = new HashSet<>();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void addServer(Server s) {
        if (s == null) return;
        this.servers.add(s);
    }

    public void addServers(Set<Server> list) {
        if (list == null) return;
        servers.addAll(list);
    }

    public void addResourceGroup(ResourceGroup g) {
        if (g == null) return;
        resourceGroups.add(g);
    }

    public void addResourceGroups(Set<ResourceGroup> list) {
        if (list == null) return;
        resourceGroups.addAll(list);
    }

    public void addAlert(Alert a) {
        if (a == null) return;
        alerts.add(a);

        Server server = findServerByIp(a.getIpAddress());
        ResourceGroup group = findGroupByIp(a.getIpAddress());

        if (server != null && group != null) {
            group.attachServer(server);
            server.setAlert(a);
        }
    }

    private Server findServerByIp(String ip) {
        for (Server s : servers) {
            if (ip.equals(s.getIpAddress())) {
                return s;
            }
        }
        return null;
    }

    private ResourceGroup findGroupByIp(String ip) {
        for (ResourceGroup g : resourceGroups) {
            if (ip.equals(g.getIpAddress())) {
                return g;
            }
        }
        return null;
    }

}
