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
        random=mTsp.random;
        this.routes=mTsp.routes;
        seenCities=mTsp.seenCities;
        cost=0;
        this.depots= mTsp.depots;
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
                routes.put(plate,new ArrayList<>());
                depots.add(plate);

                for(int j=0;j<numSalesmen;j++){
                    routes.get(plate).add(new ArrayList<>());
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

    public void swapNodesInRoute(){
        int randomDepot = depots.get(random.nextInt(depots.size()));
        List<List<Integer>> allRoutes = routes.get(randomDepot);

        int randomRouteIndex = random.nextInt(allRoutes.size());
        List<Integer> randomRoute = allRoutes.get(randomRouteIndex);


        if(randomRoute.size()==1)
            return;

        randomInInterval(randomRoute.size());
        //System.out.println(routes.get(randomDepot).get(randomRouteIndex));
        Collections.swap(routes.get(randomDepot).get(randomRouteIndex),random1,random2);
        //System.out.println(routes.get(randomDepot).get(randomRouteIndex));

    }

    public void swapHubWithNodeInRoute(){
        int randomDepot = depots.get(random.nextInt(depots.size()));
        List<List<Integer>> allRoutes = routes.get(randomDepot);

        int randomRouteIndex = random.nextInt(allRoutes.size());
        List<Integer> randomRoute = allRoutes.get(randomRouteIndex);

        random1 = random.nextInt(randomRoute.size());

        int newHub = randomRoute.get(random1);
        //System.out.println(routes);
        allRoutes.get(randomRouteIndex).add(random1,randomDepot);
        allRoutes.get(randomRouteIndex).remove(new Integer(newHub));

        routes.put(newHub,allRoutes);
        routes.remove(randomDepot);
        depots.add(newHub);
        depots.remove(new Integer(randomDepot));

        //System.out.println(routes);

    }

    public void swapNodesBetweenRoutes(){
        int randomDepot1 = depots.get(random.nextInt(depots.size()));
        List<List<Integer>> allRoutes1 = routes.get(randomDepot1);

        int randomDepot2 = depots.get(random.nextInt(depots.size()));
        List<List<Integer>> allRoutes2 = routes.get(randomDepot2);

        int randomRouteIndex1 = random.nextInt(allRoutes1.size());
        List<Integer> randomRoute1 = allRoutes1.get(randomRouteIndex1);

        int randomRouteIndex2 = random.nextInt(allRoutes2.size());
        List<Integer> randomRoute2 = allRoutes2.get(randomRouteIndex2);

        if(randomRoute1==randomRoute2){
            return;
        }

        int randomNode1 = randomRoute1.get(random.nextInt(randomRoute1.size()));
        int randomNode2 = randomRoute2.get(random.nextInt(randomRoute2.size()));
        //System.out.println(routes.get(randomDepot1).get(randomRouteIndex1));
        //System.out.println(routes.get(randomDepot2).get(randomRouteIndex2));
        routes.get(randomDepot1).get(randomRouteIndex1).add(randomRoute1.indexOf(randomNode1),randomNode2);
        routes.get(randomDepot1).get(randomRouteIndex1).remove(new Integer(randomNode1));

        routes.get(randomDepot2).get(randomRouteIndex2).add(randomRoute2.indexOf(randomNode2),randomNode1);
        routes.get(randomDepot2).get(randomRouteIndex2).remove(new Integer(randomNode2));
        //System.out.println(routes.get(randomDepot1).get(randomRouteIndex1));
        //System.out.println(routes.get(randomDepot2).get(randomRouteIndex2));

    }

    public void insertNodeInRoute(){
        int randomDepot = depots.get(random.nextInt(depots.size()));
        List<List<Integer>> allRoutes = routes.get(randomDepot);

        int randomRouteIndex = random.nextInt(allRoutes.size());
        List<Integer> randomRoute = allRoutes.get(randomRouteIndex);

        int randomNode = randomRoute.get(random.nextInt(randomRoute.size()));
        //System.out.println(routes.get(randomDepot).get(randomRouteIndex));
        routes.get(randomDepot).get(randomRouteIndex).remove(new Integer(randomNode));
        routes.get(randomDepot).get(randomRouteIndex).add(randomNode);
        //System.out.println(routes.get(randomDepot).get(randomRouteIndex));

    }

    public void insertNodeBetweenRoutes(){
        int randomDepot1 = depots.get(random.nextInt(numDepots));
        List<List<Integer>> allRoutes1 = routes.get(randomDepot1);

        int randomDepot2 = depots.get(random.nextInt(numDepots));
        List<List<Integer>> allRoutes2 = routes.get(randomDepot2);

        int randomRouteIndex1 = random.nextInt(numSalesmen);
        List<Integer> randomRoute1 = allRoutes1.get(randomRouteIndex1);

        int randomRouteIndex2 = random.nextInt(numSalesmen);
        List<Integer> randomRoute2 = allRoutes2.get(randomRouteIndex2);

        if(randomRoute1==randomRoute2||randomRoute1.size()==1){
            return;
        }
        int randomNode1 = randomRoute1.get(random.nextInt(randomRoute1.size()));
        int randomNode2 = randomRoute2.get(random.nextInt(randomRoute2.size()));
        //System.out.println(routes);
        routes.get(randomDepot1).get(randomRouteIndex1).remove(new Integer(randomNode1));

        if (randomRoute2.indexOf(randomNode2) == randomRoute2.size()-1)
            routes.get(randomDepot2).get(randomRouteIndex2).add(randomNode1);
        else
            routes.get(randomDepot2).get(randomRouteIndex2).add(randomRoute2.indexOf(randomNode2)+1,randomNode1);
        //System.out.println(routes);

    }

    public void randomInInterval(int interval){

        random1 = random.nextInt(interval);
        random2 = random.nextInt(interval);

        if(random1==random2)
            randomInInterval(interval);
    }

}