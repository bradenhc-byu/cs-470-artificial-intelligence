import games.RepeatedGame;
import algorithms.*;

import java.text.DecimalFormat;

public class RepeatedGameArena {

    public void beginTournament(RepeatedGame game, IRepeatedGameAlgorithm[] rowAlgorithms,
                                IRepeatedGameAlgorithm[] colAlgorithms, int rounds){

        float[][] resultsMatrix = new float[rowAlgorithms.length][colAlgorithms.length];

        for(int i = 0; i < rowAlgorithms.length; i++){
            for(int j = 0; j < colAlgorithms.length; j++){
                resultsMatrix[i][j] = game.play(rowAlgorithms[i], colAlgorithms[j], rounds);
            }
        }

        System.out.println(game.getGameName() + " results:");
        printGameResults(resultsMatrix);

    }

    private void printGameResults(float[][] resultsMatrix){
        DecimalFormat df = new DecimalFormat("0.00");
        for(int i = 0; i < resultsMatrix.length; i++){
            float averageRowScore = 0f;
            for(int j = 0; j < resultsMatrix[i].length; j++){
                averageRowScore += resultsMatrix[i][j];
                System.out.print(df.format(resultsMatrix[i][j]) + "\t");
            }
            System.out.print(" | " + df.format(averageRowScore / resultsMatrix[i].length) + "\n\r");
        }
    }


    public static void main(String[] args){

        RepeatedGameArena arena = new RepeatedGameArena();

        // Algorithm array row players
        IRepeatedGameAlgorithm[] rowAlgorithms = {
            new TitForTatAlgorithm(IRepeatedGameAlgorithm.Player.ROW),
            new BullyAlgorithm(IRepeatedGameAlgorithm.Player.ROW),
            new FictitiousPlayAlgorithm(IRepeatedGameAlgorithm.Player.ROW),
            new MaximinAlgorithm(IRepeatedGameAlgorithm.Player.ROW),
            new RandomAlgorithm(IRepeatedGameAlgorithm.Player.ROW),
            new CustomAlgorithm(IRepeatedGameAlgorithm.Player.ROW)
        };

        // Algorithm array col players
        IRepeatedGameAlgorithm[] colAlgorithms = {
            new TitForTatAlgorithm(IRepeatedGameAlgorithm.Player.COL),
            new BullyAlgorithm(IRepeatedGameAlgorithm.Player.COL),
            new FictitiousPlayAlgorithm(IRepeatedGameAlgorithm.Player.COL),
            new MaximinAlgorithm(IRepeatedGameAlgorithm.Player.COL),
            new RandomAlgorithm(IRepeatedGameAlgorithm.Player.COL),
            new CustomAlgorithm(IRepeatedGameAlgorithm.Player.COL)
        };



        int numRounds = 100;

        // Play the Prisoners Dilemma Game ------------------------------------------------------------------
        RepeatedGame prisonersDilemma = new RepeatedGame("PrisonersDilemma");
        String[] pdPossibleMoves = {"c","d"};
        int[][] pdRowPlayerRewards = {{3,0}, {5,1}};
        int[][] pdColPlayerRewards = {{3,5}, {0,1}};
        prisonersDilemma.initializeRewardMatrix(pdPossibleMoves, pdRowPlayerRewards, pdColPlayerRewards);

        arena.beginTournament(prisonersDilemma, rowAlgorithms, colAlgorithms, numRounds);



        // Play the Chicken Game -----------------------------------------------------------------------------
        RepeatedGame chicken = new RepeatedGame("Chicken");
        String[] cPossibleMoves = {"c","d"};
        int[][] cRowPlayerRewards = {{3,1}, {4,0}};
        int[][] cColPlayerRewards = {{3,4}, {1,0}};
        chicken.initializeRewardMatrix(cPossibleMoves, cRowPlayerRewards, cColPlayerRewards);

        arena.beginTournament(chicken, rowAlgorithms, colAlgorithms, numRounds);

        // Play the Chicken Game -----------------------------------------------------------------------------
        RepeatedGame stagHunt = new RepeatedGame("Stag Hunt");
        String[] shPossibleMoves = {"c","d"};
        int[][] shRowPlayerRewards = {{4,-3}, {3,2}};
        int[][] shColPlayerRewards = {{4,3}, {-3,2}};
        stagHunt.initializeRewardMatrix(shPossibleMoves,shRowPlayerRewards, shColPlayerRewards);

        arena.beginTournament(stagHunt, rowAlgorithms, colAlgorithms, numRounds);
    }

}
