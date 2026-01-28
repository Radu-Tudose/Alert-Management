package org.example;

public class Location {
    //mandatory fields
    private String country;

    //optional fields
    private String city;
    private String address;
    private Double latitude;
    private Double longitude;

    public Location(Builder b) {
        this.country = b.country;
        this.city = b.city;
        this.address = b.address;
        this.latitude = b.latitude;
        this.longitude = b.longitude;
    }

    //Builder Class
    public static class Builder {
        private String country;

        private String city;
        private String address;
        private Double latitude;
        private Double longitude;

        public Builder(String country) {
            this.country = country;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setAddress(String addr) {
            this.address = addr;
            return this;
        }

        public Builder setLatitude(Double lat) {
            this.latitude = lat;
            return this;
        }

        public Builder setLongitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Location build() {
            return new Location(this);
        }
    }
}
