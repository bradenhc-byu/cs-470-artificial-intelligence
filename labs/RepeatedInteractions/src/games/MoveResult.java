package games;

public class MoveResult {

    private int mRowPlayerScore;
    private int mColPlayerScore;

    public MoveResult(int rowPlayerScore, int colPlayerScore){
        mRowPlayerScore = rowPlayerScore;
        mColPlayerScore = colPlayerScore;
    }

    public int getRowPlayerScore(){
        return mRowPlayerScore;
    }
    public int getColPlayerScore(){
        return mColPlayerScore;
    }


}
