package edu.anadolu;

import java.util.*;

public class mTSP {

    private int numDepots;
    private int numSalesmen;
    private int random1,random2;
    private Map<Integer,List<List<Integer>>> routes;
    private Random random;
    private int cost;
    private List<Integer> seenCities;
    private List<Integer> depots;
    private final int MIN_ROUTE;

    public mTSP(int numDepots, int numSalesmen) {
        this.numDepots = numDepots;
        this.numSalesmen = numSalesmen;
        random = new Random();
        routes = new HashMap<>();
        seenCities = new ArrayList<>();
        getCities();
        cost = 0;
        depots = new ArrayList<>();
        MIN_ROUTE = 1;
    }

    public mTSP(mTSP mTsp){
        this.numDepots=mTsp.numDepots;
        this.numSalesmen=mTsp.numSalesmen;
        random=new Random();
        routes=mTsp.routes;
        seenCities=mTsp.seenCities;
        getCities();
        cost=0;
        depots= mTsp.depots;
        MIN_ROUTE=1;
    }

    public void getCities(){
        for(int i=1;i<82;i++){
            seenCities.add(i);
        }
    }

    public void placeDepots(){
        for(int i=0;i<numDepots;i++){
            int plate = random.nextInt(81)+1;
            if(!routes.containsKey(plate)){
                routes.put(plate,new LinkedList<>());
                depots.add(plate);
                for(int j=0;j<numSalesmen;j++){
                    routes.get(plate).add(new LinkedList<>());
                    seenCities.remove(new Integer(plate));
                    for(int c=0;c<MIN_ROUTE;c++){
                        int city=random.nextInt(seenCities.size());
                        routes.get(plate).get(j).add(seenCities.get(city));
                        seenCities.remove(city);
                    }
                }
            }
            else
                i--;
        }
    }

    public void randomSolution() {
        placeDepots();
        while(!seenCities.isEmpty()){
            int depot=random.nextInt(numDepots);
            int route=random.nextInt(numSalesmen);
            int city=random.nextInt(seenCities.size());
            routes.get(depots.get(depot)).get(route).add(seenCities.get(city));
            seenCities.remove(city);
        }
    }

    public void validate() {
        for(int depot:routes.keySet())
            calcCost(depot);
    }

    public int cost() {
        return cost;
    }

    public void calcCost(int depot){
        for(List<Integer> l:routes.get(depot)){
            int current=depot;
            for(int i:l){
                cost+=TurkishNetwork.distance[current-1][i-1];
                current=i;
            }
            cost+=TurkishNetwork.distance[current-1][depot-1];
        }
    }

    public void print(boolean verbose) {
        if(!verbose){
            int i=1;
            for(int depot:routes.keySet()){
                System.out.println("Depot"+i+": "+depot);
                int route=1;
                for(List<Integer> a:routes.get(depot)){
                    System.out.print("  Route"+route+": ");
                    route++;
                    for(int j:a){
                        if(j==a.get(a.size()-1)){
                            System.out.print(j);
                        }else
                            System.out.print(j+",");
                    }
                    System.out.println();
                }
                i++;
            }
        }else{
            int i=1;
            for(int depot:routes.keySet()){
                System.out.println("Depot"+i+": "+TurkishNetwork.cities[depot-1]);
                int route=1;
                for(List<Integer> a:routes.get(depot)){
                    System.out.print("  Route"+route+": ");
                    route++;
                    for(int j:a){
                        if(j==a.get(a.size()-1)){
                            System.out.print(TurkishNetwork.cities[j-1]);
                        }else
                            System.out.print(TurkishNetwork.cities[j-1]+",");
                    }
                    System.out.println();
                }
                i++;
            }
        }
    }

    public void randomInInterval(int interval){

        random1 = random.nextInt(interval);
        random2 = random.nextInt(interval);

        if(random1==random2)
            randomInInterval(interval);
    }

}