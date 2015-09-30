package org.oma.metaheuristic.ga_2opt;


import java.util.ArrayList;
import org.oma.metaheuristic.main.GlobalData;
/*
* Population.java
* Manages a population of candidate tours
*/



public class Population {

    // Holds population of tours
    private ArrayList<Tour> tours;

    // Construct a population
    public Population(int populationSize, boolean initialise) {
        tours = new ArrayList<Tour>(populationSize);
        boolean shuffle=false;
        // If we need to initialise a population of tours do so
        if (initialise) {
            // Loop and create individuals
            for (int i = 0; i < populationSize; i++) {
                Tour newTour = new Tour();
                newTour.generateIndividual(shuffle);
                tours.add(newTour);

                shuffle=true;
                
            }
        }
    }
    
    // Saves a tour
    public void saveTour(int index, Tour tour) {
        tours.add(tour);
    }
    
    // Gets a tour from population
    public Tour getTour(int index) {
        return tours.get(index);
    }
    

    // Gets the best tour in the population
    public Tour getFittest() {
    	
    	Tour fittest = tours.get(0);
        // Loop through individuals to find fittest
        for (int i = 1; i < tours.size(); i++) {
        	
            if (getTour(i) != null && (fittest.getFitness() <= getTour(i).getFitness())) {
                fittest = getTour(i);
            }
        }
        return fittest;
    }

    // Gets population size
    public int populationSize() {
        return tours.size();
    }
    
}