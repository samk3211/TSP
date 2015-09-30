package org.oma.metaheuristic.ga_2opt;

import org.oma.metaheuristic.main.GlobalData;
/*
* TourManager.java
* Holds the cities of a tour
*/



import java.util.ArrayList;

public class TourManager {

    // Holds our cities
    private static ArrayList<City> destinationCities = null;


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
       // return destinationCities.size();
    	return GlobalData.numCustomers;
    }
    
    public static void clear() {
    	destinationCities = new ArrayList<City>();
    }
}