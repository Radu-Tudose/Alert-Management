package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        if (args == null || args.length < 2)
            return;

        Database db = Database.getInstance();

        String type = args[0];

        try {
            if (args.length == 2) {
                String base = args[1];
                if (type.equals(PathTypes.SERVERS.getValue())) {
                    runServers(db, base);
                } else if (type.equals(PathTypes.GROUPS.getValue())) {
                    runGroups(db, base);
                }
            } else if (args.length == 4) {
                String serversBase = args[1];
                String groupsBase = args[2];
                String listenersBase = args[3];

                runServers(db, serversBase);
                runGroups(db, groupsBase);
                runListeners(db, listenersBase);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runServers(Database db, String basePath) throws Exception {
        String inPath = basePath + ".in";
        String outPath = basePath + ".out";

        try (Scanner sc = new Scanner(new File(inPath));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outPath))) {

            if (sc.hasNextLine())
                sc.nextLine();
            int lineNo = 1;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] p = line.split("\\s*\\|\\s*", -1);

                String cmd = p[0];

                if (cmd.equals("ADD SERVER")) {
                    String hostname = p[1];
                    String ip = p[2];
                    String status = p[3];
                    String country = p[4];
                    String city = p[5];
                    String address = p[6];
                    String lat = p[7];
                    String lon = p[8];
                    String uname = p[9];
                    String urole =p[10];
                    String uemail = p[11];
                    String udept = p[12];
                    String uclear = p[13];
                    String cpu = p[14];
                    String ram = p[15];
                    String storage = p[16];

                    try {
                        db.constructServer(hostname, ip, status, country, city, address, lat, lon,
                                uname, urole, uemail, udept, uclear, cpu, ram, storage);
                        bw.write("ADD SERVER: " + ip + ": " + status);
                    } catch (MissingIpAddressException | UserException | LocationException e) {
                        bw.write("ADD SERVER: " + e.getMessage() + " ## line no: " + lineNo);
                    }
                    bw.newLine();
                }

                lineNo++;
            }
        }
    }

    private static void runGroups(Database db, String basePath) throws Exception {
        String inPath = basePath + ".in";
        String outPath = basePath + ".out";

        try (Scanner sc = new Scanner(new File(inPath));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outPath))) {

            if (sc.hasNextLine()) sc.nextLine();
            int lineNo = 1;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] p = line.split("\\s*\\|\\s*", -1);

                String cmd = p[0];

                if (cmd.equals("ADD GROUP")) {
                    String ip = p[1];
                    try {
                        db.constructGroup(ip);
                        bw.write("ADD GROUP: " + ip);
                    } catch (MissingIpAddressException e) {
                        bw.write("ADD GROUP: " + e.getMessage() + " ## line no: " + lineNo);
                    }
                    bw.newLine();

                } else if (cmd.equals("FIND GROUP")) {
                    String ip = p[1];
                    try {
                        ResourceGroup g = db.findGroup(ip);
                        if (g != null) {
                            bw.write("FIND GROUP: " + ip);
                        } else {
                            bw.write("FIND GROUP: Group not found: ipAddress = " + ip);
                        }
                    } catch (MissingIpAddressException e) {
                        bw.write("FIND GROUP: " + e.getMessage() + " ## line no: " + lineNo);
                    }
                    bw.newLine();

                } else if (cmd.equals("REMOVE GROUP")) {
                    String ip = p[1];
                    try {
                        ResourceGroup g = db.removeGroup(ip);
                        if (g != null){
                            bw.write("REMOVE GROUP: " + ip);
                        } else {
                            bw.write("REMOVE GROUP: Group not found: ipAddress = " + ip);
                        }
                    } catch (MissingIpAddressException e) {
                        bw.write("REMOVE GROUP: " + e.getMessage() + " ## line no: " + lineNo);
                    }
                    bw.newLine();

                } else if (cmd.equals("ADD MEMBER")) {
                    String ip = p[1];
                    String name = p[2];
                    String role = p[3];
                    String email = p[4];
                    String dept = p[5];
                    String clearance = p[6];

                    try {
                        ResourceGroup g = db.addMember(ip, name, role, email, dept, clearance);
                        if (g != null) {
                            bw.write("ADD MEMBER: " + ip + ": name = " + name + " && role = " + role);
                        } else {
                            bw.write("ADD MEMBER: Group not found: ipAddress = " + ip);
                        }
                    } catch (MissingIpAddressException e) {
                        bw.write("ADD MEMBER: " + e.getMessage() + " ## line no: " + lineNo);
                    }
                    bw.newLine();

                } else if (cmd.equals("FIND MEMBER")) {
                    String ip = p[1];
                    String name = p[2];
                    String role = p[3];

                    try {
                        if (name == null || name.isEmpty() || role == null || role.isEmpty()) {
                            throw new UserException();
                        }

                        ResourceGroup g = db.findGroup(ip);
                        if (g == null) {
                            bw.write("FIND MEMBER: Group not found: ipAddress = " + ip);
                        } else {
                            User u = g.findMember(name, role);
                            if (u != null) {
                                bw.write("FIND MEMBER: " + ip + ": name = " + name + " && role = " + role);
                            } else {
                                bw.write("FIND MEMBER: Member not found: ipAddress = " + ip +
                                        ": name = " + name + " && role = " + role);
                            }
                        }
                    } catch (MissingIpAddressException | UserException e) {
                        bw.write("FIND MEMBER: " + e.getMessage() + " ## line no: " + lineNo);
                    }
                    bw.newLine();

                } else if (cmd.equals("REMOVE MEMBER")) {
                    String ip = p[1];
                    String name = p[2];
                    String role = p[3];

                    try {
                        if (name == null || name.isEmpty() || role == null || role.isEmpty()) {
                            throw new UserException();
                        }

                        ResourceGroup g = db.findGroup(ip);
                        if (g == null) {
                            bw.write("REMOVE MEMBER: Group not found: ipAddress = " + ip);
                        } else {
                            boolean removed = g.removeMember(name, role);
                            if (removed) {
                                bw.write("REMOVE MEMBER: " + ip + ": name = " + name + " && role = " + role);
                            } else {
                                bw.write("REMOVE MEMBER: Member not found: ipAddress = " + ip +
                                        ": name = " + name + " && role = " + role);
                            }
                        }
                    } catch (MissingIpAddressException | UserException e) {
                        bw.write("REMOVE MEMBER: " + e.getMessage() + " ## line no: " + lineNo);
                    }
                    bw.newLine();
                }

                lineNo++;
            }
        }
    }

    private static void runListeners(Database db, String basePath) throws Exception {
        String inPath = basePath + ".in";
        String outPath = basePath + ".out";

        try (Scanner sc = new Scanner(new File(inPath));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outPath))) {

            if (sc.hasNextLine()) sc.nextLine();
            int lineNo = 1;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] p = line.split("\\s*\\|\\s*", -1);

                String cmd = p[0];

                if (cmd.equals("ADD EVENT")) {
                    String type = p[1];
                    String severity = p[2];
                    String ip = p[3];
                    String message = p[4];

                    try {
                        db.constructEvent(type, severity, ip, message);
                        bw.write("ADD EVENT: " + ip + ": type = " + type +
                                " && severity = " + severity + " && message = " + message);
                    } catch (MissingIpAddressException e) {
                        bw.write("ADD EVENT: " + e.getMessage() + " ## line no: " + lineNo);
                    }
                    bw.newLine();
                }

                lineNo++;
            }
        }
    }
}