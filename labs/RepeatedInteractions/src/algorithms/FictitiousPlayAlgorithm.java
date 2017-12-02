package algorithms;

import games.RepeatedGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FictitiousPlayAlgorithm implements IRepeatedGameAlgorithm {

    private Player mMe;
    private String[] mPossibleMoves;
    private int[] mMoveCounts;
    private boolean mInitialized;

    public FictitiousPlayAlgorithm(Player player){
        mInitialized = false;
        mMe = player;
    }

    @Override
    public String makeMove(RepeatedGame gameModel) {
        if(!mInitialized){
            mMoveCounts = new int[gameModel.getAvailableMoves().length];
            mPossibleMoves = gameModel.getAvailableMoves().clone();
            mInitialized = true;
        }
        if(gameModel.getRound() == 0){
            return mPossibleMoves[0];
        }
        // Update the strategy weights
        List<String> moveHistory = (mMe == Player.ROW) ? gameModel.getColPlayerHistory() :
                gameModel.getRowPlayerHistory();
        updateWeights(moveHistory);

        // Calculate the best response
        String bestResponse = mPossibleMoves[0];
        double bestPayoff = Double.MIN_VALUE;
        for(int i = 0; i < mPossibleMoves.length; i++){
            double payoff = 0.0;
            if(mMe == Player.ROW){
                for(int j = 0; j < mPossibleMoves.length; j++){
                    double assessment = mMoveCounts[i] / moveHistory.size();
                    payoff += assessment * gameModel.getMoveResult(mMe, mPossibleMoves[i], mPossibleMoves[j]);
                }
                if(payoff > bestPayoff){
                    bestPayoff = payoff;
                    bestResponse = mPossibleMoves[i];
                }
            }
            else{
                for(int j = 0; j < mPossibleMoves.length; j++){
                    double assessment = mMoveCounts[j] / moveHistory.size();
                    payoff += assessment * gameModel.getMoveResult(mMe, mPossibleMoves[j], mPossibleMoves[i]);
                }
                if(payoff > bestPayoff){
                    bestPayoff = payoff;
                    bestResponse = mPossibleMoves[i];
                }
            }
        }
        return bestResponse;
    }

    private void updateWeights(List<String> moveHistory){
        String lastMove = moveHistory.get(moveHistory.size()-1);
        for(int i = 0; i < mPossibleMoves.length; i++){
            if (lastMove.equals(mPossibleMoves[i])) {
                mMoveCounts[i] += 1;
                break;
            }
        }
    }

    @Override
    public String getName() {
        return "Fictitious Play";
    }
}
