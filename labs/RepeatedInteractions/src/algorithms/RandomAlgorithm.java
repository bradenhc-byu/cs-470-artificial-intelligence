package algorithms;

import games.RepeatedGame;

import java.util.Random;

public class RandomAlgorithm implements IRepeatedGameAlgorithm {

    private Player mMe;

    public RandomAlgorithm(Player player){
        mMe = player;
    }

    @Override
    public String makeMove(RepeatedGame gameModel) {
        int index = new Random().nextInt(gameModel.getAvailableMoves().length);
        return gameModel.getAvailableMoves()[index];
    }

    @Override
    public String getName() {
        return "Random";
    }
}
