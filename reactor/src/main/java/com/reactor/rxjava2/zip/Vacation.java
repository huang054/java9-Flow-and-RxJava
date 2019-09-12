package com.reactor.rxjava2.zip;

import io.reactivex.Observable;

import java.time.LocalDate;
import java.util.Random;

public class Vacation {
    private final City where;
    private final LocalDate when;

    private Flight flight;

    private Hotel hotel;

    private Weather weather;


    public Vacation(City where, LocalDate when) {
        this.where = where;
        this.when = when;
    }

    public Flight getFlight() {
        return flight;
    }

    public Vacation setFlight(Flight flight) {
        this.flight = flight;
        return this;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public Vacation setHotel(Hotel hotel) {
        this.hotel = hotel;
        return this;
    }

    public Weather getWeather() {
        return weather;
    }

    public Vacation setWeather(Weather weather) {
        this.weather = weather;
        return this;
    }
    public Observable<Flight> cheapFlightFrom(City from){
        return Observable.just(new Flight(new Random().nextInt(5)+1));
    }
    public Observable<Hotel> cheapHotel(){
        return Observable.just(new Hotel());
    }

    public City getWhere() {
        return where;
    }

    public LocalDate getWhen() {
        return when;
    }
}
