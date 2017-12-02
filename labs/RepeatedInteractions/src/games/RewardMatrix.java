package games;

import java.util.Map;
import java.util.HashMap;

public class RewardMatrix {

    private Map<String, Map<String, MoveResult>> mMatrix = new HashMap<>();
    private int mSize = 0;

    public int getSize(){
        return mSize;
    }


    public RewardMatrix(String[] availableMoves){
        mSize = availableMoves.length;
        for(int i = 0; i < mSize; i++){
            mMatrix.put(availableMoves[i], new HashMap<>());
            for(int j = 0; j < mSize; j++){
                mMatrix.get(availableMoves[i]).put(availableMoves[j], new MoveResult(0,0));
            }
        }
    }

    public void updateMatrix(String rowMove, String colMove, MoveResult result){
        mMatrix.get(rowMove).put(colMove, result);
    }

    public MoveResult getMoveResult(String rowMove, String colMove){
        return mMatrix.get(rowMove).get(colMove);
    }
}
