package org.oma.metaheuristic.ga_2opt;
/*
* Tour.java
* Stores a candidate tour
*/



import java.util.ArrayList;
import java.util.Collections;


public class Tour implements Cloneable{

    // Holds our tour of cities
    private ArrayList<City> tour = new ArrayList<City>();
    // Cache
    private double fitness = 0;
    private float distance = 0;

    
    // Constructs a blank tour
    public Tour(){
        for (int i = 0; i < TourManager.numberOfCities(); i++) {
            tour.add(null);
        }
    }
    public Tour(int size){
    	for (int i = 0; i < size; i++) {
            tour.add(null);
        }
    }
    
    public void shuffleMe(){
    	Collections.shuffle(tour);
    }
    
    
    public Tour(ArrayList<City> tour){
        this.tour = tour;
    }

    // Creates a random individual
    public void generateIndividual(boolean shuffle) {
        // Loop through all our destination cities and add them to our tour
        for (int cityIndex = 0; cityIndex < TourManager.numberOfCities(); cityIndex++) {
          setCity(cityIndex, TourManager.getCity(cityIndex));
        }
        // Randomly reorder the tour
        if(shuffle==true)
        Collections.shuffle(tour);//non ok per istanze grandi
    }

    // Gets a city from the tour
    public City getCity(int tourPosition) {
        return (City)tour.get(tourPosition);
    }

    // Sets a city in a certain position within a tour
    public void setCity(int tourPosition, City city) {
        tour.set(tourPosition, city);
        // If the tours been altered we need to reset the fitness and distance
        fitness = 0;
        distance = 0;
    }
    
    // Gets the tours fitness
    public double getFitness() {
        if (fitness == 0) {
            fitness = 1/(double)getDistance();
        }
        return fitness;
    }
    
    // Gets the total distance of the tour
    public float getDistance(){
        if (distance == 0) {
            float tourDistance = 0;
            // Loop through our tour's cities
            for (int cityIndex=0; cityIndex < tour.size(); cityIndex++) {
                // Get city we're travelling from
                City fromCity = getCity(cityIndex);
                // City we're travelling to
                City destinationCity;
                
                // Check we're not on our tour's last city, if we are set our
                // tour's final destination city to our starting city
                
                if(cityIndex+1 < tourSize()){
                    destinationCity = getCity(cityIndex+1);
                }
                else{
                    destinationCity = getCity(0);
                }
                
               // destinationCity = getCity((cityIndex + 1) % tourSize());
                
                // Get the distance between the two cities
                tourDistance += fromCity.distanceTo(destinationCity);
               // System.out.println(cityIndex+": "+fromCity.distanceTo(destinationCity)+" "+tourDistance);
            }
            distance = tourDistance;
        }
        return distance;
    }

    // Get number of cities on our tour
    public int tourSize() {
        return tour.size();
    }
    
    // Check if the tour contains a city
    public boolean containsCity(City city){
        return tour.contains(city);
    }
    
    @Override
    public String toString() {
        String geneString = "";//" 0) ";
        for (int i = 0; i < tourSize(); i++) {
            geneString += getCity(i)+"\n"/*+(i+1)+") "*/;
        }
        return geneString;
    }


	@Override
	public boolean equals(Object obj) {
		Tour tourToCompare = (Tour)obj;
		
		for(int i = 0; i < this.tour.size(); i++) {
			if (!tourToCompare.tour.get(i).equals(this.tour.get(i)))
				return false;
		}
		return true;
	}


	@Override
	protected Tour clone()  {
		Tour to = new Tour();
		for (int i = 0; i < tour.size(); i ++)
			to.setCity(i, tour.get(i));
		return to;
	}
    
	public static Tour shifTour (Tour tour, int pos) {
		Tour newTour = new Tour();
		int i = 0;
		for (int j = 0; j < tour.tourSize() && i < pos; j++) {
			if (tour.getCity(j) != null)
				newTour.setCity(i++, tour.getCity(j));
		}
		return newTour;
	}
	
	public static void translate(Tour tour, int x, int size, int y) {
		Tour tra = tour.clone();
		int j=x;
		int t=x;
		for(int i = 0; i< tour.tourSize(); i++) {
			if (i >= y && i < size) {
				tour.setCity(i, tra.getCity(j++));
			}
			else if (i > size) {
				tour.setCity(t++,tra.getCity(t++));
			}

		}
		
	}

	public void removeCity(City city) {
		tour.remove(city);
	}


}