package algorithms;

import games.RepeatedGame;

public interface IRepeatedGameAlgorithm {

    enum Player{
        ROW,
        COL
    }

    /**
     * Use the implementation of the algorithm to make a move for the player given the parameters
     *
     * @param gameModel An instance of a RepeatedGame object that holds the current state of the game
     *
     * @return An String indicating the move the player makes using the algorithm
     */
    String makeMove(RepeatedGame gameModel);

    /**
     * Provides the algorithms name
     *
     * @return The name of the algorithm
     */
    String getName();

}
