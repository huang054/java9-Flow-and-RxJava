package com.reactor.rxjava2.zip;


import io.reactivex.Observable;

import java.time.LocalDate;

public class Zip {
    static void zip_test(){
        final WeatherStation weatherStation = new WeatherStation.BasecWeatherStation();
        Observable<WeatherStation.Temperature> temperatureObservable=weatherStation.temperature();
        Observable<WeatherStation.Wind> windObservable = weatherStation.wind();
        Observable.zip(temperatureObservable,windObservable,Weather::new);
        //temperatureObservable.zipWith(windObservable,Weather::new);
    }

    public static void main(String[] args) {
        zip_test2();
    }
    static void zip_test2(){
        final WeatherStation weatherStation = new WeatherStation.BasecWeatherStation();
        Observable<WeatherStation.Temperature> temperatureObservable =weatherStation.temperature();
        Observable<WeatherStation.Wind> windObservable =weatherStation.wind();
        Observable<Weather> weatherObservable =Observable.zip(temperatureObservable,windObservable,Weather::new);
        Observable<LocalDate> nextWeekDays=Observable.range(1,7)
                .map(i->LocalDate.now().plusDays(i));
        Observable.fromArray(City.values())
                .flatMap(city->nextWeekDays.map(date->new Vacation(city,date)))
                .flatMap(vacation -> Observable.zip(weatherObservable.filter(Weather::isSunny)
                        ,vacation.cheapFlightFrom(vacation.getWhere()),vacation.cheapHotel()
                        ,(w,f,h)->{
                             w.isSunny();
                             return vacation.setWeather(w).setFlight(f).setHotel(h);
                        })
                ).subscribe(item->System.out.println("we got："+item.getWhere()+"航班折扣:"
        +item.getFlight().getCount()+"折 from the observable"),throwable -> System.out.println("异常->"+throwable.getMessage())
        ,()->System.out.println("completed"));
    }
    static void zip_test3(){
        Integer[] numbers={1,2,13,34,15,17};
        String[] fruits={"苹果","梨子","李子","番茄","芒果"};
        Observable<Integer> source1=Observable.fromArray(numbers);
        Observable<String> source2=Observable.fromArray(fruits);
        Observable<Integer> source3=Observable.range(10,3);
        Observable.zip(source1,source2,source3,(value1,value2,value3)->value1+":"
        +value2+"*"+value3)
                .subscribe(item->System.out.println("we got:"+item+"from the observable")
                ,throwable -> System.out.println("异常->"+throwable.getMessage()),()->
                        System.out.println("completed"));
    }
}
