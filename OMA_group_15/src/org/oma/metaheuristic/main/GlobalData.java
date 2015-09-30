package org.oma.metaheuristic.main;


public class GlobalData  {

	/***
	 * @path of the folder that contains .tsp files
	 * In this folder will be saved: result.csv , <instance>.opt.tour (overwritten if it already exists), output.txt 
	 */
	public static String path = new String("/Volumes/Data HD/Downloads/TSP.1/");
	
	public static String city = new String("eil51");
	public static final int bestKnown = 426;
	public static int totIter = 10;
    public static int popSize = 50;
    public static boolean overridePopSize = false;
    public static boolean overrideMaxEvolutions = false;
    public static int maxEvolutions = 30;
    public static double mutationRate = 0.008;
    public static int maxAge = 7;
    public static int mutatePerEvo = 5;
    
    
	public static int numCustomers=0;

}
