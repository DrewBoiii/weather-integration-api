package dev.drewboiii.weatherintegrationapi.model;

public enum WeatherProviderLanguage {

    // TODO: 6/8/2022 make method in interface, cuz this is bullshit -> getAvailableLanguages
    ENGLISH("en_US", "en", "en"),
    RUSSIAN("ru_RU", "ru", "ru");

    private final String yandexLang;
    private final String weatherComLang;
    private final String openWeatherLang;

    WeatherProviderLanguage(String yandexLang, String weatherComLang, String openWeatherLang) {
        this.yandexLang = yandexLang;
        this.weatherComLang = weatherComLang;
        this.openWeatherLang = openWeatherLang;
    }

    public String getYandexLang() {
        return yandexLang;
    }

    public String getWeatherComLang() {
        return weatherComLang;
    }

    public String getOpenWeatherLang() {
        return openWeatherLang;
    }
}
