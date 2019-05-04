package edu.anadolu;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.sun.istack.internal.NotNull;

import java.util.Random;
import java.util.stream.IntStream;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {

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

        if (best != null) {
            best.print(params.getVerbose());
            System.out.println("**Total cost is " + best.cost());

        }
        int count = 0;

        for(int iter=0;iter<5000000;iter++){
            mTSP temp = new mTSP(best);

            int random = new Random().nextInt(5);

            if(random==0)
                temp.swapNodesInRoute();
            else if(random==1)
                temp.swapHubWithNodeInRoute();
            else if(random==2)
                temp.swapNodesBetweenRoutes();
            else if(random==3)
                temp.insertNodeInRoute();
            else
                temp.insertNodeBetweenRoutes();

            temp.validate();

            if(temp.cost()<best.cost()){
                best = temp;
                count++;
            }
        }

        /*mTSP temp = new mTSP(best);
        System.out.println("swapnodesinroute");
        temp.swapNodesInRoute();
        System.out.println("swapHubWithNodeInRoute");
        temp.swapHubWithNodeInRoute();
        System.out.println("swapNodesBetweenRoutes");
        temp.swapNodesBetweenRoutes();
        System.out.println("insertNodeInRoute");
        temp.insertNodeInRoute();
        System.out.println("insertNodeBetweenRoutes");
        temp.insertNodeBetweenRoutes();*/
        //temp.validate();

        //temp.print(false);
        System.out.println("**Total cost is " + best.cost());
        System.out.println(count);
        //best.print(false);

    }
}
