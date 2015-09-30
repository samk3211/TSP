package org.oma.metaheuristic.main;


import org.oma.metaheuristic.ga_2opt.*;



/*
* TSP_GA.java
* Create a tour and evolve a solution
*/



public class Main {

 
	 public static void main(String args[]) {

		IOManager.readParams(args);
		int off = 0;
		if (IOManager.instances.size() == 0)
			off = 1;
		
		
        
        for (int inst = 0; inst < IOManager.instances.size()+off; inst++) {
        	
        	if (off == 0)
        		GlobalData.city = IOManager.instances.get(inst);
        	System.out.println("Instance: " + GlobalData.city);
        	IOManager.readData();
        	long timeStart = 0;
            long timeStop = 0;
            long timeGap = 0;
            long timeTotal = 0;
            int fittest = 0;
            int fittestTotal = 0;
            int fittestBest = Integer.MAX_VALUE;
            int fittestWorst = Integer.MIN_VALUE;
            if (GlobalData.overridePopSize)
            	checkPopSize();
            if (GlobalData.overrideMaxEvolutions)
            	checkMaxEvolutions();

            Tour bestTour = null;
            // Initialize population
            for (int i = 0; i < GlobalData.totIter; i++) {
            	
            	boolean bestDone = false;
            	int bestAge = 0;
            	
            	timeStart = System.currentTimeMillis();
            	
            	Population pop = new Population(GlobalData.popSize, true);
            	Population nextPop = null;
            	
                for (int j = 0; j < GlobalData.maxEvolutions; j++) {
                	
                    nextPop = GA.evolvePopulation(pop, j%GlobalData.mutatePerEvo);
                    
//                    IOManager.writeCSVOutputStats(j, (int)nextPop.getFittest().getDistance(), System.currentTimeMillis() - timeStart, i);
//    				if (!bestDone && pop.getTour(0).getDistance() == GlobalData.bestKnown) {
//                    	System.out.println("-------> Best known found in " + (System.currentTimeMillis() - timeStart) + " ms at Evolution: " + j);
//                    	bestDone = true;
//                    }
                    
                    if (nextPop.getFittest().getDistance() == pop.getFittest().getDistance()) {
                    	
                    	bestAge++;
                    	
                    	if (bestAge >= GlobalData.maxAge) {
                    		GlobalData.mutatePerEvo = 2;
                    		bestAge = 0;
                    	}
             
                    	else
                    		GlobalData.mutatePerEvo = 5;
                    }
                    
                    else {
                		GlobalData.mutatePerEvo = 5;
                		bestAge = 0;
                    }
                    
                    pop = nextPop;
                    
                    if ((System.currentTimeMillis() - timeStart) == 5*60*1000)
                    	break;
                }
                // Print final results
                timeStop = System.currentTimeMillis();
                timeGap = timeStop-timeStart;
                timeTotal += timeGap;
                
                fittest = (int) pop.getFittest().getDistance();
                fittestTotal += fittest;
                if (fittest < fittestBest) {
                	fittestBest = fittest;
                	bestTour = pop.getFittest();
                }
                else if (fittest > fittestWorst)
                	fittestWorst = fittest;
                
//                System.out.println("Iteration: " + (i+1) + " Total time: " + timeGap + " Final distance: " + fittest);
                
            }

            float fittestMean = fittestTotal / (float)GlobalData.totIter;
            long timeMean = timeTotal / GlobalData.totIter;
            IOManager.writeCSVOutput(fittestBest, fittestMean, fittestWorst, timeMean);
            IOManager.writeOptTspOutput(bestTour);
//            IOManager.writeOutput(GlobalData.city, timeGap, fittest, GlobalData.popSize);
            System.out.println("Best: " + fittestBest + "  Mean: " + fittestMean + " Worst: " + fittestWorst + " TimeMean: " + timeMean);
        }
        

    }
	 
	 private static void checkPopSize() {

		 if (GlobalData.numCustomers < 500 )
			 GlobalData.popSize = 50;
		 else
			 GlobalData.popSize = 30;
	 }
	 
	 private static void checkMaxEvolutions() {

		 if (GlobalData.numCustomers < 160 )
			 GlobalData.maxEvolutions = 30;
		 else if (GlobalData.numCustomers >= 160 && GlobalData.numCustomers < 500)
			 GlobalData.maxEvolutions = 150;
		 else
			 GlobalData.maxEvolutions = 75;
	 }
}