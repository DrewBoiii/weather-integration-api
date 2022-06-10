package dev.drewboiii.weatherintegrationapi.model;

public enum WeatherLocation {

    // TODO: 6/8/2022 go to https://openweathermap.org/api/geocoding-api to make this flexible
    MOSCOW("Russian Federation", "Moscow", "Moscow", 55.833333, 37.620393),
    SAMARA("Russian Federation", "Samara", "Samara", 53.195538, 50.101783);

    private final String country;
    private final String region;
    private final String locality;
    private final Double latitude;
    private final Double longitude;

    WeatherLocation(String country, String region, String locality, Double latitude, Double longitude) {
        this.country = country;
        this.region = region;
        this.locality = locality;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getLocality() {
        return locality;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
