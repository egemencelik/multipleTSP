package edu.anadolu;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws IOException {

        Params params;
        try {
            params = CliFactory.parseArguments(Params.class, args);
        } catch (ArgumentValidationException e) {
            System.out.println(e.getMessage());
            return;
        }

        mTSP best = null;
        int minCost = Integer.MAX_VALUE;

        for (int i = 0; i < 100_000; i++) {

            mTSP mTSP = new mTSP(params.getNumDepots(), params.getNumSalesmen());

            mTSP.randomSolution();
            mTSP.validate();
            // mTSP.print(false);

            final int cost = mTSP.cost();

            //System.out.println("Total cost is " + cost);

            if (cost < minCost) {
                best = mTSP;
                minCost = cost;
            }
        }

        int swapNodesInRoute = 0;
        int swapHubWithNodeInRoute = 0;
        int swapNodesBetweenRoutes = 0;
        int insertNodeInRoute = 0;
        int insertNodeBetweenRoutes = 0;

        for(int iter=0;iter<5000000;iter++){
            mTSP temp = new mTSP(best);
            int random = new Random().nextInt(5);

            if(random==0){
                temp.swapNodesInRoute();
                temp.validate();
                if(temp.cost()<best.cost()){
                    best = temp;
                    swapNodesInRoute++;
                }
            }
            else if(random==1){
                temp.swapHubWithNodeInRoute();
                temp.validate();
                if(temp.cost()<best.cost()){
                    best = temp;
                    swapHubWithNodeInRoute++;
                }
            }
            else if(random==2){
                temp.swapNodesBetweenRoutes();
                temp.validate();
                if(temp.cost()<best.cost()){
                    best = temp;
                    swapNodesBetweenRoutes++;
                }
            }
            else if(random==3){
                temp.insertNodeInRoute();
                temp.validate();
                if(temp.cost()<best.cost()){
                    best = temp;
                    insertNodeInRoute++;
                }
            }
            else{
                temp.insertNodeBetweenRoutes();
                temp.validate();
                if(temp.cost()<best.cost()){
                    best = temp;
                    insertNodeBetweenRoutes++;
                }
            }
        }

        best.print(params.getVerbose());
        System.out.println("**Total cost is " + best.cost());
        System.out.println();
        JSONObject sampleObject = new JSONObject();
        sampleObject.put("swapNodesInRoute",swapNodesInRoute);
        sampleObject.put("swapHubWithNodeInRoute",swapHubWithNodeInRoute);
        sampleObject.put("swapNodesBetweenRoutes",swapNodesBetweenRoutes);
        sampleObject.put("insertNodeInRoute",insertNodeInRoute);
        sampleObject.put("insertNodeBetweenRoutes",insertNodeBetweenRoutes);
        System.out.println(sampleObject.toString());

        JSONObject map = new JSONObject(best.routes);

        best.writeJson("solution.json");

    }


}
