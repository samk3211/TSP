package org.oma.metaheuristic.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.oma.metaheuristic.ga_2opt.City;
import org.oma.metaheuristic.ga_2opt.Tour;
import org.oma.metaheuristic.ga_2opt.TourManager;


public final class IOManager {

	
	private enum ParamFile {
	    NONE, PARAMS, INSTANCES
	}
	
	public static ArrayList<String> instances = new ArrayList<String>();
	
	public static void readParams(String args[])
	{
		ParamFile fileStatus = ParamFile.NONE;
		String myFileParam = GlobalData.path;
		if (args.length > 0)
		{
			myFileParam = args[0];
		}
		try
		{
			File input = new File(myFileParam);
			BufferedReader br = new BufferedReader(	new InputStreamReader(new FileInputStream(input)));
			String line;
			String token;
			StringTokenizer st;
			while ((line = br.readLine()) != null)
			{   					
				switch (fileStatus)
				{
				case NONE: 
    					st = new StringTokenizer(line);	
						token = st.nextToken();    							
    					switch (token.toUpperCase()) 
    					{
    						case "PARAMS":
    							fileStatus = ParamFile.PARAMS;
    							System.out.println("Reading the parameters");
    							break;
    						case "INSTANCES":
    							fileStatus = ParamFile.INSTANCES;
    							System.out.println("Reading the instances");
    							break;
    						case "EOF":
    							System.out.println("Close the file");
    							br.close();
    							return;
    						default:
    							break;
    					}
					break;
				case PARAMS:
					if (line.toUpperCase().contains("ENDPARAMS"))
					{
						fileStatus = ParamFile.NONE;
						System.out.println("End of parameters");
					}
					else
					{
						st = new StringTokenizer(line," ");
						token = st.nextToken();
						token = st.nextToken();
						while (st.hasMoreTokens()) {
							token =  token + " " + st.nextToken();
						}
						GlobalData.path = token;
						st = new StringTokenizer(br.readLine());
						token = st.nextToken();
						token = st.nextToken();
						GlobalData.popSize = Integer.parseInt(token);
						if (GlobalData.popSize == -1)
							GlobalData.overridePopSize = true;
						st = new StringTokenizer(br.readLine());
						token = st.nextToken();
						token = st.nextToken();
						GlobalData.maxEvolutions = Integer.parseInt(token);
						if (GlobalData.maxEvolutions == -1)
							GlobalData.overrideMaxEvolutions = true;
						st = new StringTokenizer(br.readLine());
						token = st.nextToken();
						token = st.nextToken();
						GlobalData.mutationRate = Double.parseDouble(token);
						st = new StringTokenizer(br.readLine());
						token = st.nextToken();
						token = st.nextToken();
						GlobalData.totIter = Integer.parseInt(token);
							
					}
					break;
				case INSTANCES:
					if (line.toUpperCase().contains("ENDINSTANCES"))
					{
						System.out.println("End of instances");								
						fileStatus = ParamFile.NONE;
					}
					else
					{
						st = new StringTokenizer(line);
						token = st.nextToken(".");
						instances.add(token);
						fileStatus = ParamFile.INSTANCES;
					}
					break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error "+e.getMessage());
//	        System.exit(-1);
		}    			
	}
	
	
	public static void readData() {
		
		int numCustomers=0;
		TourManager.clear();
        String myFile = new String(GlobalData.path + GlobalData.city + ".tsp");
        double x,y;
        int index;
        City c;
		
        try
		{
			File input = new File(myFile);
			BufferedReader br = new BufferedReader(	new InputStreamReader(new FileInputStream(input)));
			String line;
			String token;
			line = br.readLine();	
			line = br.readLine();	
			line = br.readLine();	
			line = br.readLine();
			StringTokenizer st = new StringTokenizer(line);	
			token = st.nextToken();
			token = st.nextToken();		
			numCustomers = Integer.parseInt(token);
			GlobalData.numCustomers= Integer.parseInt(token);
	       			line = br.readLine();
			line = br.readLine();
	        for( int i = 0; i < numCustomers; i++ )
	        {
				line = br.readLine();        	
				st = new StringTokenizer(line);	
				token = st.nextToken();
				index = Integer.parseInt(token);
				token = st.nextToken();
				x = Double.parseDouble(token); 
				token = st.nextToken();
				y = Double.parseDouble(token); 
			    c=new City(x, y, index);
				TourManager.addCity(c);
	        }
			line = br.readLine();						
			if (!line.toUpperCase().contains("EOF"))
			{
				br.close();
				throw new IllegalArgumentException("Error while reading the input file: EOF Section");
			}
			br.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error "+e.getMessage());
	        System.exit(-1);
		}
	}
	
	public static void writeOutput(String city, long time, int fittest, int popSize) {
        String myFileOutput = new String(GlobalData.path + "output.txt");
        File f = new File(myFileOutput);
         
        if(!f.exists())
        {
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not create new log file");
                e.printStackTrace();
            }
 
        }
         
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(myFileOutput, true)));
            pw.println(city + "\t" + time + "\t " + fittest + "\t" + GlobalData.mutationRate + "\t" + popSize + "\t" + GlobalData.maxEvolutions + "\t" + GlobalData.mutatePerEvo);
            pw.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }     
    
    }
	
	public static void writeCSVOutput (int bestTour, float meanTour, int worstTour, long meanTime) {
		
		String myFileOutput = new String(GlobalData.path + "results.csv");
        File f = new File(myFileOutput);
        PrintWriter pw = null;
        if(!f.exists())
        {
            try {
                f.createNewFile();
                pw = new PrintWriter(new BufferedWriter(new FileWriter(myFileOutput, true)));
                pw.println("Instance,Mean sol,Min sol,Max sol,Time mean");
                
            } catch (IOException e) {
                System.out.println("Could not create new log file");
                e.printStackTrace();
            }
 
        }
         
        try {
        	if (pw == null)
        		pw = new PrintWriter(new BufferedWriter(new FileWriter(myFileOutput, true)));
        	
            pw.println(GlobalData.city + "," + meanTour + "," + bestTour + "," + worstTour + "," + meanTime + " ms");
            pw.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
	}
	
public static void writeCSVOutputStats (int evo, int value, long time, int iteration) {
		
		String myFileOutput = new String(GlobalData.path + GlobalData.city +"_" + Integer.toString(iteration) + ".csv");
        File f = new File(myFileOutput);
        PrintWriter pw = null;
        if(!f.exists())
        {
            try {
                f.createNewFile();
                pw = new PrintWriter(new BufferedWriter(new FileWriter(myFileOutput, true)));
                pw.println("Evo,Value,Time");
                
            } catch (IOException e) {
                System.out.println("Could not create new log file");
                e.printStackTrace();
            }
 
        }
         
        try {
        	if (pw == null)
        		pw = new PrintWriter(new BufferedWriter(new FileWriter(myFileOutput, true)));
        	
            pw.println(evo + "," + value + "," + time);
            pw.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
	}

	public static void writeOptTspOutput (Tour tour) {
		
		String myFileOutput = new String(GlobalData.path + GlobalData.city + ".opt.tour");
        File f = new File(myFileOutput);
        PrintWriter pw = null;
        if(!f.exists())
        {
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not create new log file");
                e.printStackTrace();
            }
 
        }
         
        try {
        	if (pw == null)
        		pw = new PrintWriter(new BufferedWriter(new FileWriter(myFileOutput, false)));
        	
            pw.println("NAME: " + GlobalData.city + ".opt.tour");
            pw.println("TYPE: TOUR");
            pw.println("DIMENSION: " + tour.tourSize());
            pw.println("TOUR_SECTION");
            
            for (int i = 0; i < tour.tourSize(); i++)
            	pw.println(tour.getCity(i).getIndex());
            pw.println("-1");
            pw.println("EOF");
            pw.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
	}

}
