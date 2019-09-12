package com.reactor.rxjava2.zip;

import io.reactivex.Observable;

public interface WeatherStation {
    Observable<Temperature> temperature();
    Observable<Wind> wind();

    class BasecWeatherStation implements  WeatherStation{

        @Override
        public Observable<Temperature> temperature() {
            return Observable.just(new Temperature());
        }

        @Override
        public Observable<Wind> wind() {
            return Observable.just(new Wind());
        }
    }
    class Temperature{}

    class Wind{}
}
