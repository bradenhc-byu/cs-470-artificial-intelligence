import java.util.HashMap;
import java.util.Map;

public class State {

    private float value;
    private Map<CompassDirection, State> moves = new HashMap<>();
    private Map<CompassDirection, Float> moveValues = new HashMap<>();
    private float reward;

    public State(float reward){
        this.setReward(reward);
        this.moveValues.put(CompassDirection.NORTH, 0f);
        this.moveValues.put(CompassDirection.EAST, 0f);
        this.moveValues.put(CompassDirection.SOUTH, 0f);
        this.moveValues.put(CompassDirection.WEST, 0f);
    }

    public float getValue() {
        return this.value;
    }

    public void setValue(float mValue) {
        this.value = mValue;
    }

    public State getMoveState(CompassDirection direction){
        return this.moves.get(direction);
    }

    public void setMoveState(CompassDirection direction, State state){
        this.moves.put(direction, state);
    }

    public float getMoveValue(CompassDirection direction) {
        return this.moveValues.get(direction);
    }

    public void setMoveValue(CompassDirection direction, float value) {
        this.moveValues.put(direction, value);
    }

    public float getReward(){
        return this.reward;
    }

    public void setReward(float reward){
        this.reward = reward;
    }

}
