package com.example.kac.prijavinapako.Trgovski_potnik;

import com.example.kac.prijavinapako.Trgovski_potnik.City;

import java.util.ArrayList;

/**
 * Created by Klemen on 2. 01. 2018.
 */

public class TourManager {
    // Holds our cities
    private static ArrayList destinationCities = new ArrayList<City>();

    public void clearCity(){
        destinationCities.clear();
    }

    // Adds a destination city
    public static void addCity(City city) {
        destinationCities.add(city);
    }

    // Get a city
    public static City getCity(int index){
        return (City)destinationCities.get(index);
    }

    // Get the number of destination cities
    public static int numberOfCities(){
        return destinationCities.size();
    }
}
