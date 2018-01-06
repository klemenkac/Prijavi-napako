package com.example.kac.prijavinapako.Trgovski_potnik;


/**
 * Created by Klemen on 2. 01. 2018.
 */

public class City {
    String id;
    double x;
    double y;
    String stDoma;

    // Constructs a randomly placed city
    public City(){
        this.x = (int)(Math.random()*200);
        this.y = (int)(Math.random()*200);
    }

    // Constructs a city at chosen x, y location
    public City(double x, double y, String stDom,String ajdi){
        this.x = x;
        this.y = y;
        this.stDoma=stDom;
        this.id=ajdi;
    }

    // Gets city's x coordinate
    public double getX(){
        return this.x;
    }

    // Gets city's y coordinate
    public double getY(){
        return this.y;
    }

    public String getStDoma(){
        return this.stDoma;
    }

    // Gets the distance to given city
    public double distanceTo(City city){
        double xDistance = Math.abs(getX() - city.getX());
        double yDistance = Math.abs(getY() - city.getY());
        double distance = Math.sqrt( (xDistance*xDistance) + (yDistance*yDistance) );

        return distance;
    }

    @Override
    public String toString(){
        return getX()+", "+getY();
    }
}
