package org.example;

public class Server {
    //mandatory fields
    private String ipAddress;
    private Location location;
    private User owner;

    //optional fields
    private String hostname;
    private ServerStatus status;
    private Integer cpuCores;
    private Integer ramGb;
    private Integer storageGb;

    Server(Builder b) {
        this.ipAddress = b.ipAddress;
        this.location = b.location;
        this.owner = b.owner;
        this.hostname = b.hostname;
        this.status = b.status;
        this.cpuCores = b.cpuCores;
        this.ramGb = b.ramGb;
        this.storageGb = b.storageGb;
    }

    //Builder Class
    public static class Builder {
        private String ipAddress;
        private Location location;
        private User owner;

        private String hostname;
        private ServerStatus status;
        private Integer cpuCores;
        private Integer ramGb;
        private Integer storageGb;

        public Builder(String ipAddr, Location l, User owner) {
            this.ipAddress = ipAddr;
            this.location = l;
            this.owner = owner;
        }

        public Builder setHostname(String hostname) {
            this.hostname = hostname;
            return this;
        }

        public Builder setStatus(ServerStatus status) {
            this.status = status;
            return this;
        }

        public Builder setCpuCores(Integer cpuCores) {
            this.cpuCores = cpuCores;
            return this;
        }

        public Builder setRamGb(Integer ramGb) {
            this.ramGb = ramGb;
            return this;
        }

        public Builder setStorageGb(Integer storageGb) {
            this.storageGb = storageGb;
            return this;
        }

        public Server build() {
            return new Server(this);
        }
    }
}
