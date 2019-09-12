package com.reactor.rxjava2.zip;

public class Weather {
    private final WeatherStation.Temperature temperature;

    public WeatherStation.Temperature getTemperature() {
        return temperature;
    }

    public WeatherStation.Wind getWind() {
        return wind;
    }

    private final WeatherStation.Wind wind;

    public Weather(WeatherStation.Temperature temperature, WeatherStation.Wind wind) {
        this.temperature = temperature;
        this.wind = wind;
    }
    public boolean isSunny(){
        return true;
    }

}
