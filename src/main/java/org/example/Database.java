package org.example;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Database {
    private static Database instance;
    private Set<Server> servers;
    private Set<ResourceGroup> resourceGroups;
    private Set<Alert> alerts;
    private final UserFactory userFactory = new UserFactory();

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

    public void constructServer(String hostname, String ip_address, String status, String country,
                                 String city, String address, String lat, String longi, String username,
                                 String role, String email, String dept, String clearance, String cpu_cores,
                                 String ram, String storage)
            throws MissingIpAddressException, UserException, LocationException {

        if (ip_address == null || ip_address.isEmpty()) {
            throw new MissingIpAddressException();
        }
        if (username == null || username.isEmpty() || role == null || role.isEmpty()) {
            throw new UserException();
        }
        if (country == null || country.isEmpty()) {
            throw new LocationException();
        }

        Location.Builder locBuilder = new Location.Builder(country);

        if (city != null && !city.isEmpty()) {
            locBuilder.setCity(city);
        }
        if (address != null && !address.isEmpty()) {
            locBuilder.setAddress(address);
        }
        if (lat != null && !lat.isEmpty()) {
            locBuilder.setLatitude(Double.parseDouble(lat));
        }
        if (longi != null && !longi.isEmpty()) {
            locBuilder.setLongitude(Double.parseDouble(longi));
        }

        Location location = locBuilder.build();

        Integer clearanceLevel = null;
        if (clearance != null && !clearance.isEmpty()) {
            clearanceLevel = Integer.parseInt(clearance);
        }

        User owner = this.userFactory.build(username, role, email, dept, clearanceLevel);

        Server.Builder serverBuilder = new Server.Builder(ip_address, location, owner);

        if (hostname != null && !hostname.isEmpty()) {
            serverBuilder.setHostname(hostname);
        }
        if (status != null && !status.isEmpty()) {
            serverBuilder.setStatus(ServerStatus.valueOf(status.toUpperCase()));
        }
        if (cpu_cores != null && !cpu_cores.isEmpty()) {
            serverBuilder.setCpuCores(Integer.parseInt(cpu_cores));
        }
        if (ram != null && !ram.isEmpty()) {
            serverBuilder.setRamGb(Integer.parseInt(ram));
        }
        if (storage != null && !storage.isEmpty()) {
            serverBuilder.setStorageGb(Integer.parseInt(storage));
        }

        Server s = serverBuilder.build();

        addServer(s);
    }

    public void constructGroup(String ip_address) throws MissingIpAddressException {

        if (ip_address == null || ip_address.isEmpty()) {
            throw new MissingIpAddressException();
        }

        ResourceGroup group = new ResourceGroup(ip_address);
        addResourceGroup(group);
    }

    public ResourceGroup findGroup(String ip_address) throws MissingIpAddressException {

        if (ip_address == null || ip_address.isEmpty()) {
            throw new MissingIpAddressException();
        }

        return findGroupByIp(ip_address);
    }

    public ResourceGroup removeGroup(String ip_address) throws MissingIpAddressException {

        if (ip_address == null || ip_address.isEmpty()) {
            throw new MissingIpAddressException();
        }

        ResourceGroup group = findGroupByIp(ip_address);
        if (group != null) {
            resourceGroups.remove(group);
        }

        return group; // null = not found
    }

    public ResourceGroup addMember(String ip_address, String name, String role, String email,
                                    String dept, String clearance)
            throws MissingIpAddressException {

        if (ip_address == null || ip_address.isEmpty()) {
            throw new MissingIpAddressException();
        }

        ResourceGroup group = findGroupByIp(ip_address);
        if (group == null) {
            return null; // null = not found
        }

        Integer clearanceLevel = null;
        if (clearance != null && !clearance.isEmpty()) {
            clearanceLevel = Integer.parseInt(clearance);
        }

        User member = this.userFactory.build(name, role, email, dept, clearanceLevel);

        group.addMember(member);
        return group;
    }

    public AlertTypes parseAlertType(String raw) {
        for (AlertTypes t : AlertTypes.values()) {
            if (t.getValue().equalsIgnoreCase(raw)) {
                return t;
            }
        }
        return null;
    }

    public Severity parseSeverity(String raw) {
        for (Severity s : Severity.values()) {
            if (s.getValue().equalsIgnoreCase(raw)) {
                return s;
            }
        }
        return null;
    }

    public void constructEvent(String alertType, String alertSeverity, String ipAddress, String message)
            throws MissingIpAddressException {

        if (ipAddress == null || ipAddress.isEmpty()) {
            throw new MissingIpAddressException();
        }

        AlertTypes typeEnum = parseAlertType(alertType);
        Severity severityEnum = parseSeverity(alertSeverity);

        Alert a = new Alert(typeEnum, severityEnum, message, ipAddress);
        addAlert(a);
    }

}
