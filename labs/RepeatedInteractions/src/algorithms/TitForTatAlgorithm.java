package algorithms;

import games.RepeatedGame;

public class TitForTatAlgorithm implements IRepeatedGameAlgorithm {

    private Player mMe;

    public TitForTatAlgorithm(Player player){
        mMe = player;
    }

    @Override
    public String makeMove(RepeatedGame gameModel){

        if(gameModel.getRound() == 0){
            return gameModel.getAvailableMoves()[0];
        }

        if(mMe == Player.ROW){
            return gameModel.getColPlayerHistory().get(gameModel.getColPlayerHistory().size()-1);
        }
        else if(mMe == Player.COL){
            return gameModel.getRowPlayerHistory().get(gameModel.getRowPlayerHistory().size()-1);
        }

        return "";
    }

    @Override
    public String getName() {
        return "Tit for Tat";
    }
}
