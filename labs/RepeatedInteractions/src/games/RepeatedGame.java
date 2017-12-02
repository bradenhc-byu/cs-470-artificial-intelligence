package games;

// Standard Java packages and classes
import java.util.ArrayList;
import java.util.List;

// Custom packages and classes
import algorithms.IRepeatedGameAlgorithm;

public class RepeatedGame {

    // Static Game ID
    private static int GAMEID = 0;

    private String mGameName;
    private boolean mGameInitialized = false;
    private String[] mAvailableMoves;
    private RewardMatrix mRewardMatrix;
    private List<String> mRowPlayerHistory = new ArrayList<>();
    private List<String> mColPlayerHistory = new ArrayList<>();
    private int mRound = 0;
    private double mRowPlayerScore = 0;
    private double mColPlayerScore = 0;

    /**
     * Default construtor. Creates an empty instance of the RepeatedGame class. The game has a default name of
     * 'RepeatedGame' plus a statically incrementing game id.
     */

    public RepeatedGame(){
        mGameName = "RepeatedGame" + GAMEID;
        GAMEID++;
    }

    /**
     * Argument constructor. Assigns a provided name to the repeated game but does not initialize the game matrix. The
     * method initializeRewardMatrix() must be called following this constructor in order to play the game.
     *
     * @param name The string name of the game.
     */
    public RepeatedGame(String name){
        mGameName = name;
    }

    public RepeatedGame(String name, String[] availableMoves, int[][] rowPlayerRewards, int[][] colPlayerRewards){
        mGameName = name;
        initializeRewardMatrix(availableMoves, rowPlayerRewards, colPlayerRewards);
    }


    /**
     * Plays the game, following the rules coded up for a particular implementation of this interface
     *
     * @return True if the game completed successfully, false otherwise
     */
    public float play(IRepeatedGameAlgorithm rowAlgorithm, IRepeatedGameAlgorithm colAlgorithm, int rounds){
        if(!mGameInitialized){
            System.err.println("Unable to play game. Game has not been initialized.");
            return 0f;
        }
        // Get ready to play a new game
        resetGame();

        System.out.println("Playing " + mGameName
                + " (" + rowAlgorithm.getName() + " vs. " + colAlgorithm.getName() + ")");
        // Play the game using the provided algorithms
        while(mRound < rounds){
            String rowPlayerMove = rowAlgorithm.makeMove(this);
            String colPlayerMove = colAlgorithm.makeMove(this);
            mRowPlayerHistory.add(rowPlayerMove);
            mColPlayerHistory.add(colPlayerMove);
            mRowPlayerScore += mRewardMatrix.getMoveResult(rowPlayerMove, colPlayerMove).getRowPlayerScore();
            mColPlayerScore += mRewardMatrix.getMoveResult(rowPlayerMove, colPlayerMove).getColPlayerScore();
            mRound++;
        }
        double rowPlayerAverageScore = mRowPlayerScore / rounds;
        return (float) rowPlayerAverageScore;
    }

    /**
     * Creates the matrix used to evaluates rewards of the players. The matrix involves only two players. The number of
     * available moves and their costs for each player are specified by the arguments.
     *
     * @param possibleMoves The number of moves available to each player
     * @param rowPlayerRewards A 2D array of integers of the same size as availableMoves that represent the rewards for
     *                         the row player for each move sequentially
     * @param colPlayerRewards A 2D array of integers of the same size as availableMoves that represent the rewards for
     *                         the column player for each move sequentially
     *
     * @return True if the matrix was initialized successfully, false otherwise
     */

    public void initializeRewardMatrix(String[] possibleMoves, int[][] rowPlayerRewards, int[][] colPlayerRewards){
        mRewardMatrix = new RewardMatrix(possibleMoves);
        mAvailableMoves = possibleMoves.clone();
        for(int row = 0; row < mRewardMatrix.getSize(); row++){
            for(int col = 0; col < mRewardMatrix.getSize(); col++){
                MoveResult moveResult = new MoveResult(rowPlayerRewards[row][col], colPlayerRewards[row][col]);
                mRewardMatrix.updateMatrix(possibleMoves[row], possibleMoves[col], moveResult);
            }
        }
        mGameInitialized = true;
    }

    public void resetGame(){
        mRound = 0;
        mRowPlayerScore = 0;
        mRowPlayerHistory.clear();
        mColPlayerScore = 0;
        mColPlayerHistory.clear();
    }

    public int getMoveResult(IRepeatedGameAlgorithm.Player player, String rowMove, String colMove){
        if(player == IRepeatedGameAlgorithm.Player.ROW){
            return mRewardMatrix.getMoveResult(rowMove, colMove).getRowPlayerScore();
        }
        else{
            return mRewardMatrix.getMoveResult(rowMove, colMove).getColPlayerScore();
        }
    }


    /* Getters and Setters */

    public String getGameName() {
        return mGameName;
    }

    public void setGameName(String gameName) {
        mGameName = gameName;
    }

    public boolean isGameInitialized() {
        return mGameInitialized;
    }

    public void setGameInitialized(boolean gameInitialized) {
        mGameInitialized = gameInitialized;
    }

    public String[] getAvailableMoves() {
        return mAvailableMoves;
    }

    public RewardMatrix getRewardMatrix() {
        return mRewardMatrix;
    }

    public void setRewardMatrix(RewardMatrix rewardMatrix) {
        mRewardMatrix = rewardMatrix;
    }

    public List<String> getRowPlayerHistory() {
        return mRowPlayerHistory;
    }

    public void setRowPlayerHistory(List<String> rowPlayerHistory) {
        mRowPlayerHistory = rowPlayerHistory;
    }

    public List<String> getColPlayerHistory() {
        return mColPlayerHistory;
    }

    public void setColPlayerHistory(List<String> colPlayerHistory) {
        mColPlayerHistory = colPlayerHistory;
    }

    public int getRound() {
        return mRound;
    }

    public void setRound(int round) {
        mRound = round;
    }

    public double getRowPlayerScore() {
        return mRowPlayerScore;
    }

    public void setRowPlayerScore(double rowPlayerScore) {
        mRowPlayerScore = rowPlayerScore;
    }

    public double getColPlayerScore() {
        return mColPlayerScore;
    }

    public void setColPlayerScore(double colPlayerScore) {
        mColPlayerScore = colPlayerScore;
    }
}
