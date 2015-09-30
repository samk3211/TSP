package org.oma.metaheuristic.ga_2opt;


import java.util.Random;


import org.oma.metaheuristic.main.GlobalData;


/*
* GA.java
* Manages algorithms for evolving population
*/



public class GA {
	
    /* GA parameters */
    private static  int tournamentSize ;
    private static final boolean elitism = true;
    private static Random r = new Random();
    
    
    // Evolves a population over one generation
    public static Population evolvePopulation(Population pop, int mutationPerEvo) {
    	int cloneCounter = 0;
        Population newPopulation = new Population(pop.populationSize(), false);
        tournamentSize = (int) (pop.populationSize()*0.2);
        // Keep our best individual if elitism is enabled
        int elitismOffset = 0;
        if (elitism) {
            newPopulation.saveTour(0, pop.getFittest());
            newPopulation.saveTour(1, tournamentSelection(pop));
            elitismOffset = 2;
        }

        // Crossover population
        // Loop over the new population's size and create individuals from
        // Current population
        int k = 0;
        for (int i = elitismOffset; i < GlobalData.popSize +k; i++) {
        	
            // Select parents
            Tour parent1 = tournamentSelection(pop);
            Tour parent2 = tournamentSelection(pop);
            
//             Crossover parents
			Tour child = crossover(parent1, parent2);
            
            if (mutationPerEvo == 0)
            	mutate(child);

            reorder2Opt(child);
            
            // Add child to new population
            
            boolean flag = false;
            for(int j=0; j < newPopulation.populationSize() && !flag; j++) {
            	if (newPopulation.getTour(j) != null && newPopulation.getTour(j).equals(child))
            		flag = true;
            }
            if (!flag) {
            	newPopulation.saveTour(i, child);
            	cloneCounter = 0;
            	
            } else {
            	cloneCounter++;
            	if (cloneCounter < 15)
            		k++;
            }
            
            
        }

        return newPopulation;
    }

	// Applies crossover to a set of parents and creates offspring
    public static Tour crossover(Tour parent1, Tour parent2) {
        // Create new child tour
        Tour child = new Tour();

        // Get start and end sub tour positions for parent1's tour
      int startPos = (int) (r.nextInt(parent1.tourSize()));
      int endPos = (int) (r.nextInt(parent1.tourSize()));
//      while ((Math.abs(startPos-endPos)) < parent1.tourSize()*0.05)
//    	  endPos = (int) (r.nextInt(parent1.tourSize()));
        // Loop and add the sub tour from parent1 to our child
        for (int i = 0; i < child.tourSize(); i++) {
            // If our start position is less than the end position
            if (startPos < endPos && i > startPos && i < endPos) {
                child.setCity(i, parent1.getCity(i));
            } // If our start position is larger
            else if (startPos > endPos) {
                if (!(i < startPos && i > endPos)) {
                    child.setCity(i, parent1.getCity(i));
                }
            }
        }
        // Loop through parent2's city tour
        for (int i = 0; i < parent2.tourSize(); i++) {
            // If child doesn't have the city add it
            if (!child.containsCity(parent2.getCity(i))) {
                // Loop to find a spare position in the child's tour
                for (int ii = 0; ii < child.tourSize(); ii++) {
                    // Spare position found, add city
                    if (child.getCity(ii) == null) {
                        child.setCity(ii, parent2.getCity(i));
                        break;
                    }
                }
            }
        }
        return child;
    }
    
    public static Tour crossoverDouble(Tour parent1, Tour parent2) {

        Tour child = new Tour();
        int[] mrk = new int[4];
      mrk[0] = (int) (r.nextInt(parent1.tourSize()));
      mrk[1] = (int) (r.nextInt(parent1.tourSize()));
      mrk[2] = (int) (r.nextInt(parent1.tourSize()));
      mrk[3] = (int) (r.nextInt(parent1.tourSize()));
      sort(mrk);
      
        for (int i = 0; i < child.tourSize(); i++) {

            if ((i > mrk[0] && i < mrk[1]) || (i > mrk[2] && i < mrk[3])) {
                child.setCity(i, parent1.getCity(i));
            } 
        }
        for (int i = 0; i < parent2.tourSize(); i++) {
            if (!child.containsCity(parent2.getCity(i))) {
                for (int ii = 0; ii < child.tourSize(); ii++) {
                    if (child.getCity(ii) == null) {
                        child.setCity(ii, parent2.getCity(i));
                        break;
                    }
                }
            }
        }
        return child;
    }

        
    
    // Mutate a tour using swap mutation
    private static void mutate(Tour tour) {
        // Loop through tour cities
        for(int tourPos1=0; tourPos1 < tour.tourSize(); tourPos1++){
            // Apply mutation rate
            if( r.nextDouble() < GlobalData.mutationRate){
                // Get a second random position in the tour

                int tourPos2 = (int) ((double)tour.tourSize() * r.nextDouble());
                // Get the cities at target position in tour
                City city1 = tour.getCity(tourPos1);
                City city2 = tour.getCity(tourPos2);

                // Swap them around
                tour.setCity(tourPos2, city1);
                tour.setCity(tourPos1, city2);
                if (tourPos1 < tourPos2) exchange(tour, tourPos1+1, tourPos2-1);
                else exchange(tour, tourPos2+1, tourPos1-1);

            }
        }
    }

    // Selects candidate tour for crossover
    private static Tour tournamentSelection(Population pop) {
        // Create a tournament population
        Population tournament = new Population(tournamentSize, false);
        // For each place in the tournament get a random candidate tour and
        // add it
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (r.nextDouble() * pop.populationSize());
            while (pop.getTour(randomId) == null) {
            	randomId = (int) (r.nextDouble() * pop.populationSize());
            }
            	tournament.saveTour(i, pop.getTour(randomId));
        }
        // Get the fittest tour
        Tour fittest = tournament.getFittest();
//        Tour fittest = pop.getFittest(); //just select the best??
        return fittest;
    }
        
    /**
     * child optimization
  	*/
    public static void reorder2Opt(Tour tour) {
        boolean done = false;
        int numCities = tour.tourSize();
        for(int k = 0; k < numCities && !done; k++)
        {
            done = true;
            for(int i = 0; i < numCities; i++)
            {
                for(int j = i + 2; j < numCities; j++)
                	if(	
                            tour.getCity(i).distanceTo(tour.getCity(((i + 1) % numCities))) +
                            tour.getCity(j).distanceTo(tour.getCity(((j + 1) % numCities))) 
                      		   > 
                            tour.getCity(i).distanceTo(tour.getCity(j)) +
                            tour.getCity(((i + 1) % numCities)).distanceTo(tour.getCity(((j + 1) % numCities)))
                          
                         )
                         {
                             City tmp = tour.getCity(((i + 1) % numCities));
                             tour.setCity((i + 1) % numCities, tour.getCity(j));
                             tour.setCity(j, tmp);
//                             tsp_ga.onNewTourReady(tour);

                             exchange(tour, i + 2, j - 1);
                             done = false;
                         }
            }
        }
    }
    
    public  static void reorder3Opt(Tour tour) {
        boolean done = false;
        int numCities = (tour.tourSize()*(4))/4;
//        System.out.println((tour.tourSize()*(299)/300));
//        System.out.println(tour.tourSize()-1);
        for(int t = 0; t < 1 && !done; t++) {
            done = true;
            for(int i = 0; i < numCities; i++)
                for(int j = i + 2; j < numCities; j++)
                	for (int k = j + 2; k < numCities; k++) {
                		done=bestPermutation(tour, i, ((i + 1) % numCities), j, ((j + 1) % numCities), k, ((k + 1) % numCities));

                	}
        }
    }
    
    
    private static boolean bestPermutation (Tour tour, int a, int b, int c, int d, int e, int f) {
    	City A = tour.getCity(a);
    	City B = tour.getCity(b);
    	City C = tour.getCity(c);
    	City D = tour.getCity(d);
    	City E = tour.getCity(e);
    	City F = tour.getCity(f);
    	double[] dist = new double[8];
    	dist[0] = A.distanceTo(B) + C.distanceTo(E) + D.distanceTo(F);
    	dist[1] = A.distanceTo(C) + B.distanceTo(D) + E.distanceTo(F);
    	dist[2] = A.distanceTo(C) + B.distanceTo(E) + D.distanceTo(F);
    	dist[3] = A.distanceTo(D) + E.distanceTo(B) + C.distanceTo(F);
    	dist[4] = A.distanceTo(D) + E.distanceTo(C) + B.distanceTo(F);
    	dist[5] = A.distanceTo(E) + D.distanceTo(B) + C.distanceTo(F);
    	dist[6] = A.distanceTo(E) + D.distanceTo(C) + B.distanceTo(F);
    	dist[7] = A.distanceTo(B) + C.distanceTo(D) + E.distanceTo(F);
    	int index = 0;
    	double min = Double.MAX_VALUE;
    	for (int i = 0; i < dist.length; i++)
    		if (dist[i] < min) {
    			index = i;
    			min = dist[i];
    		}
    	if (index == 0) {
    		tour.setCity(d, E);
    		tour.setCity(e, D);
    		exchange(tour, c+2, e-1);
    		return false;
    	} else if (index == 1) {
    		tour.setCity(b, C);
    		tour.setCity(c, B);
    		exchange(tour, a+2, c-1);
    		return false;
    	} else if (index == 2) {
    		tour.setCity(b, C);
    		tour.setCity(c, B);
    		tour.setCity(d, E);
    		tour.setCity(e, D);
    		exchange(tour, a+2,c-1);
            exchange(tour, c+2,e-1);
            return false;
    	} else if (index == 3) {
    		tour.setCity(b, D);
    		tour.setCity(c, E);
    		tour.setCity(d, B);
    		tour.setCity(e, C);
    		exchange(tour, a+2,e-1);
    		exchange(tour, a+2,c-1);
            exchange(tour, c+2,e-1);
    		return false;
    	} else if (index == 4) {
    		tour.setCity(b, D);
    		tour.setCity(c, E);
    		tour.setCity(d, C);
    		tour.setCity(e, B);
    		exchange(tour, c+2,e-1);
            exchange(tour, a+2,e-1);
    		return false;
    	} else if (index == 5) {
    		tour.setCity(b, E);
    		tour.setCity(c, D);
    		tour.setCity(d, B);
    		tour.setCity(e, C);
    		exchange(tour, a+2,c-1);
            exchange(tour, a+2,e-1);
    		return false;
    	} else if (index == 6) {
    		tour.setCity(b, E);
    		tour.setCity(e, B);
    		exchange(tour, a+2,e-1);
    		return false;
    	} else {
    		return true;
    	}
    }
    
    
    private static boolean bestPermutationMutated (Tour tour, int a, int b, int c, int d, int e, int f) {

    	City A = tour.getCity(a);
    	City B = tour.getCity(b);
    	City C = tour.getCity(c);
    	City D = tour.getCity(d);
    	City E = tour.getCity(e);
    	City F = tour.getCity(f);
    	double[] dist = new double[8];
    	dist[0] = A.distanceTo(B) + C.distanceTo(E) + D.distanceTo(F) + r.nextInt(10);
    	dist[1] = A.distanceTo(C) + B.distanceTo(D) + E.distanceTo(F) + r.nextInt(10);
    	dist[2] = A.distanceTo(C) + B.distanceTo(E) + D.distanceTo(F) + r.nextInt(10);
    	dist[3] = A.distanceTo(D) + E.distanceTo(B) + C.distanceTo(F) + r.nextInt(10);
    	dist[4] = A.distanceTo(D) + E.distanceTo(C) + B.distanceTo(F) + r.nextInt(10);
    	dist[5] = A.distanceTo(E) + D.distanceTo(B) + C.distanceTo(F) + r.nextInt(10);
    	dist[6] = A.distanceTo(E) + D.distanceTo(C) + B.distanceTo(F) + r.nextInt(10);
    	dist[7] = A.distanceTo(B) + C.distanceTo(D) + E.distanceTo(F) + r.nextInt(10);
    	int index = 0;
    	double min = Double.MAX_VALUE;
    	for (int i = 0; i < dist.length; i++)
    		if (dist[i] < min) {
    			index = i;
    			min = dist[i];
    		}
    	if (index == 0) {
    		tour.setCity(d, E);
    		tour.setCity(e, D);
    		exchange(tour, c+2, e-1);
    		return false;
    	} else if (index == 1) {
    		tour.setCity(b, C);
    		tour.setCity(c, B);
    		exchange(tour, a+2, c-1);
    		return false;
    	} else if (index == 2) {
    		tour.setCity(b, C);
    		tour.setCity(c, B);
    		tour.setCity(d, E);
    		tour.setCity(e, D);
    		exchange(tour, a+2,c-1);
            exchange(tour, c+2,e-1);
            return false;
    	} else if (index == 3) {
    		tour.setCity(b, D);
    		tour.setCity(c, E);
    		tour.setCity(d, B);
    		tour.setCity(e, C);
    		exchange(tour, a+2,e-1);
    		exchange(tour, a+2,c-1);
            exchange(tour, c+2,e-1);
    		return false;
    	} else if (index == 4) {
    		tour.setCity(b, D);
    		tour.setCity(c, E);
    		tour.setCity(d, C);
    		tour.setCity(e, B);
    		exchange(tour, c+2,e-1);
            exchange(tour, a+2,e-1);
    		return false;
    	} else if (index == 5) {
    		tour.setCity(b, E);
    		tour.setCity(c, D);
    		tour.setCity(d, B);
    		tour.setCity(e, C);
    		exchange(tour, a+2,c-1);
            exchange(tour, a+2,e-1);
    		return false;
    	} else if (index == 6) {
    		tour.setCity(b, E);
    		tour.setCity(e, B);
    		exchange(tour, a+2,e-1);
    		return false;
    	} else {
    		return true;
    	}
    }
    private static void exchange(Tour cities, int from, int To)
    {
        if(from >= To || from >= cities.tourSize()|| To < 0)
            return;
        for(; from < To; To--)
        {
            City tmp = cities.getCity(from);
            cities.setCity(from, cities.getCity(To));
            cities.setCity(To, tmp);
            from++;
        }

    }
    
/********************************/

	private static void reverseTour(Tour tour, int start, int end) {
		if (start > end) {
			int buf = end;
			end = start;
			start = buf;
		}
	for (int i = 1; i <= ((end - start) / 2); i++) {
		City temp = tour.getCity(end - i);
		tour.setCity(end - i, tour.getCity(i + start));
		tour.setCity(i + start,temp);

		}	
	}
	
	public static void sort(int[] mrk) {
		for(int i = 0; i < mrk.length; i++) {
            boolean flag = false;
            for(int j = 0; j < mrk.length-1; j++)
                if(mrk[j]>mrk[j+1]) {
                    int k = mrk[j];
                    mrk[j] = mrk[j+1];
                    mrk[j+1] = k;
                    flag=true;
                }
            if(!flag) break;
        }
	}
    
	
	private static Tour gsx(Tour p1, Tour p2) {
		Tour c = new Tour();
		Tour notPicked = p1.clone();
		boolean p1Valid = true;
		boolean p2Valid = true;
		int totSize = p1.tourSize();
		int x = r.nextInt(p1.tourSize());
		int childIndexFromP2 = x;

		City cityToPick = p1.getCity(x);
		c.setCity(x, cityToPick);
		notPicked.removeCity(cityToPick);

		int y = 0;
		int addedCities = 0;
		for (int i = 0; i < p2.tourSize(); i++)
			if (p2.getCity(i).equals(cityToPick)) {
				y = i;
				break;
			}
		do {
			x = (x-1)%totSize < 0 ? (x-1)%totSize+totSize:(x-1)%totSize;
			y = (y+1)%totSize;
			if (p1Valid) {
				if (!c.containsCity(p1.getCity(x))) {
					c.setCity(x, p1.getCity(x));
					notPicked.removeCity(p1.getCity(x));
					addedCities++;
				}
				else
					p1Valid = false;
			}
			if (p2Valid) {
				if (!c.containsCity(p2.getCity(y))) {
					childIndexFromP2 = (childIndexFromP2 +1) % totSize;
					c.setCity(childIndexFromP2, p2.getCity(y));
					notPicked.removeCity(p2.getCity(y));
					addedCities++;
				}
				else
					p2Valid = false;
			}
		} while (p1Valid || p2Valid);
		c = Tour.shifTour(c, ++addedCities);

		notPicked.shuffleMe();
		if (addedCities < totSize) {
			for (int i = 0; i < notPicked.tourSize(); i++) {
				c.setCity(addedCities+i, notPicked.getCity(i));
			}
		}
		return c;
	}
	
	private static Tour cutChild(Tour child, int from, int to) {

		Tour chromosomePiece = new Tour(to-from);

		for(int i = 0; i < (to-from); i++) {
			chromosomePiece.setCity(i, child.getCity(from+i));
		}
//		reorderThreeOpt(chromosomePiece,to-from);
		return chromosomePiece;
	}

	private static Tour combineChild(Tour[] subTours, int cutSize) {

		Tour reChild = new Tour();
		int j = 0;		
		for(int p=0; p<subTours.length; p++){
			for(int i=0; i<subTours[p].tourSize();i++){
				reChild.setCity(j++,subTours[p].getCity(i));
			}
		}
		return reChild;
	}
	  
	

}