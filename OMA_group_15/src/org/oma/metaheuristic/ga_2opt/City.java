package org.oma.metaheuristic.ga_2opt;


/*
* City.java
* creates the city object
*/



public class City {
    double x;
    double y;
    int index;

    
    // Constructs a city at chosen x, y location
    public City(double x, double y, int index){
        this.x = x;
        this.y = y;
        this.index = index;
    }
    
    // Gets city's x coordinate
    public double getX(){
        return this.x;
    }
    
    // Gets city's y coordinate
    public double getY(){
        return this.y;
    }
    
    public int getIndex() {
    	return this.index;
    }
    
    // Gets the distance to given city
    public double distanceTo(City c){
        double xDistance = getX() - c.getX();
        double yDistance = getY() - c.getY();
        double distance = Math.round(Math.sqrt(xDistance*xDistance + yDistance*yDistance ));
        
        return distance;
    }
    
    public double distanceRMSTo(City c){
        double xDistance = Math.abs(getX() - c.getX());
        double yDistance = Math.abs(getY() - c.getY());
        double distance = Math.round(xDistance*xDistance + yDistance*yDistance );
        
        return distance;
    }
    
    @Override
    public String toString(){
    	//return String.format("%3d  %3d",getX(), getY());
        return getX()+", "+getY();
    }

	@Override
	public boolean equals(Object obj) {
		City cityToCompare = (City)obj;
		if (cityToCompare!= null && this.x == cityToCompare.x && this.y == cityToCompare.y)
			return true;
		return false;
	}
    
    
}