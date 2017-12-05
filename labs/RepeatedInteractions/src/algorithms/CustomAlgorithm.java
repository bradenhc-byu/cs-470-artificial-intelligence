package algorithms;

import games.MoveResult;
import games.RepeatedGame;
import games.RewardMatrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomAlgorithm implements IRepeatedGameAlgorithm {

    private Player mMe;
    private String mParetoOptimalMove = "";
    private Random mRandomGenerator = new Random();

    public CustomAlgorithm(Player player){
        mMe = player;
    }

    @Override
    public String makeMove(RepeatedGame gameModel) {

        // First, determine the Pareto Optimal move
        if(mParetoOptimalMove.equals("")){
            mParetoOptimalMove = paretoOptimalMove(gameModel);
        }

        // If we are winning, keep playing the pareto optimal solution
        double rowScore = gameModel.getRowPlayerScore();
        double colScore = gameModel.getColPlayerScore();
        if(mMe == Player.ROW && rowScore > colScore){
            return mParetoOptimalMove;
        }
        else if(colScore > rowScore){
            return mParetoOptimalMove;
        }

        // Create a probability distribution measuring how often the other player has played the
        // Pareto optimal solution
        float paretoProbability = 1;
        List<String> history = (mMe == Player.ROW) ? gameModel.getRowPlayerHistory() : gameModel.getColPlayerHistory();
        for(String move : history){
            if(move.equals(mParetoOptimalMove)){
                paretoProbability += 1f;
            }
        }
        int historySize = (history.size() != 0) ? history.size() : 2;
        paretoProbability = paretoProbability / historySize;

        // Next randomly choose the Pareto optimal solution or another solution based on the computed
        // probability distribution
        float r = mRandomGenerator.nextFloat();
        if(r <= paretoProbability){
            return mParetoOptimalMove;
        }
        else{
            return gameModel.getAvailableMoves()[mRandomGenerator.nextInt(gameModel.getAvailableMoves().length)];
        }
    }

    private String paretoOptimalMove(RepeatedGame gameModel){
        RewardMatrix matrix = gameModel.getRewardMatrix();
        String[] possibleMoves = gameModel.getAvailableMoves();
        // Get a list of all possible solutions
        ArrayList<MoveResult> points = new ArrayList<>();
        for(int i = 0; i < matrix.getSize(); i++){
            for(int j = 0; j < matrix.getSize(); j++){
                MoveResult scores = matrix.getMoveResult(possibleMoves[i], possibleMoves[j]);
                points.add(scores);
            }
        }
        // Find all pareto optimal solutions
        List<MoveResult> paretoSolutions = new ArrayList<>();
        for(int i = 0; i < points.size(); i++){
            int rowPlayerReward = points.get(i).getRowPlayerScore();
            int colPlayerReward = points.get(i).getColPlayerScore();
            boolean foundBetter = false;
            for(int j = 0; j < points.size(); j++) {
                int compareRowPlayerReward = points.get(j).getRowPlayerScore();
                int compareColPlayerReward = points.get(j).getColPlayerScore();
                if(compareRowPlayerReward > rowPlayerReward && compareColPlayerReward > colPlayerReward){
                    foundBetter = true;
                }
            }
            if(!foundBetter){
                paretoSolutions.add(points.get(i));
            }
        }
        // If more than one pareto optimal solution exists, return the one that maximizes the player's score
        String solution;
        if(paretoSolutions.size() > 1){
            solution = getMove(paretoSolutions.get(0), gameModel, possibleMoves);
        }
        else{
            solution = getMove(paretoSolutions.get(0), gameModel, possibleMoves);
        }
        return solution;
    }

    private String getMove(MoveResult rewards, RepeatedGame game, String[] possibleMoves){
        for(String rowMove : possibleMoves){
            for(String colMove : possibleMoves){
                if(game.getRewardMatrix().getMoveResult(rowMove, colMove) == rewards){
                    if(mMe == Player.ROW){
                        return rowMove;
                    }
                    else{
                        return colMove;
                    }
                }
            }
        }
        return possibleMoves[0];
    }

    @Override
    public String getName() {
        return "Custom";
    }
}
