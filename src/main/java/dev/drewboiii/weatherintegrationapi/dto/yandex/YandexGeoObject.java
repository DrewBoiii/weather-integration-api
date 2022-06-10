package dev.drewboiii.weatherintegrationapi.dto.yandex;

import lombok.Data;

@Data
public class YandexGeoObject {

    private District district;
    private Locality locality;
    private Province province;
    private Country country;

    @Data
    public static class District {

        private String name;

    }

    @Data
    public static class Locality {

        private String name;

    }

    @Data
    public static class Province {

        private String name;

    }

    @Data
    public static class Country {

        private String name;

    }

}