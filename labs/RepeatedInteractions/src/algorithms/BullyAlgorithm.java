package algorithms;

import games.RepeatedGame;
import games.RewardMatrix;

public class BullyAlgorithm implements IRepeatedGameAlgorithm {

    private Player mMe;
    private String mHighestPotentialMove = "";

    public BullyAlgorithm(Player player){
        mMe = player;
    }

    @Override
    public String makeMove(RepeatedGame gameModel) {
        if(mHighestPotentialMove.equals("")){
            RewardMatrix gameMatrix = gameModel.getRewardMatrix();
            String[] possibleMoves = gameModel.getAvailableMoves();
            double greatestPotentialScore = Integer.MIN_VALUE;
            for(int i = 0; i < gameMatrix.getSize(); i++){
                for(int j = 0; j < gameMatrix.getSize(); j++){
                    double score = (mMe == Player.ROW) ?
                            gameMatrix.getMoveResult(possibleMoves[i], possibleMoves[j]).getRowPlayerScore() :
                            gameMatrix.getMoveResult(possibleMoves[i], possibleMoves[j]).getColPlayerScore();
                    if(score > greatestPotentialScore){
                        greatestPotentialScore = score;
                        mHighestPotentialMove = (mMe == Player.ROW) ? possibleMoves[i] : possibleMoves[j];
                    }
                }
            }
        }
        return mHighestPotentialMove;
    }

    @Override
    public String getName() {
        return "Bully";
    }
}
