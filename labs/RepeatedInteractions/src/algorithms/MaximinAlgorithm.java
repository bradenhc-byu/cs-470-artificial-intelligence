package algorithms;

import games.RepeatedGame;

import java.util.ArrayList;
import java.util.List;

public class MaximinAlgorithm implements IRepeatedGameAlgorithm {

    private Player mMe;
    private boolean mPrune;
    private String mSolution;

    public MaximinAlgorithm(Player player){
        mMe = player;
        mPrune = false;
        mSolution = "";
    }

    public MaximinAlgorithm(Player player, boolean prune){
        mMe = player;
        mPrune = prune;
        mSolution = "";
    }

    @Override
    public String makeMove(RepeatedGame gameModel) {
        // Compute the maximin solution to the game
        if(mSolution.equals("")){


        }
        return "d";
    }

    @Override
    public String getName() {
        return "Maximin";
    }

}
