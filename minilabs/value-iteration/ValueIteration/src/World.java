import java.text.DecimalFormat;

public class World {

    private State[][] world;

    public World(float[][] worldValues, float[] directionProbabilities){

        this.world = new State[worldValues.length + 2][worldValues[0].length + 2];

        for(int i = 0; i < this.world.length; i++){
            for(int j = 0; j < this.world[i].length; j++){

                // Build a border
                if(i == 0 || j == 0 || i == this.world.length - 1 || j == this.world[0].length - 1){
                    State n = new State(Float.MIN_VALUE);
                    n.setValue(Float.MIN_VALUE);
                    this.world[i][j] = n;
                    continue;
                }

                // Fill the world
                State n = new State(worldValues[i-1][j-1]);
                if(n.getReward() == Float.MIN_VALUE){
                    n.setValue(Float.MIN_VALUE);
                }
                this.world[i][j] = n;
            }
        }

        // Connect the Nodes
        for(int i = 1; i < this.world.length - 1; i++){
            for(int j = 1; j < this.world[0].length - 1; j++){
                this.world[i][j].setMoveState(CompassDirection.NORTH, this.world[i-1][j]);
                this.world[i][j].setMoveState(CompassDirection.EAST, this.world[i][j+1]);
                this.world[i][j].setMoveState(CompassDirection.SOUTH, this.world[i+1][j]);
                this.world[i][j].setMoveState(CompassDirection.WEST, this.world[i][j-1]);
            }
        }

    }

    public void reverseIterate(int numIterations){

        int initialNumIterations = numIterations;

        while(numIterations > 0){

            // Update move values
            for(int i = this.world.length - 2; i > 0; i--){
                for(int j = this.world[0].length - 1; j > 0; j--){

                    State curState = this.world[i][j];
                    float curStateValue = (numIterations == initialNumIterations) ?
                            curState.getReward() : curState.getValue();
                    float newValue;
                    // Check North
                    newValue = curStateValue + curState.getMoveValue(CompassDirection.NORTH);
                    curState.setMoveValue(CompassDirection.NORTH, newValue);
                    // Check East
                    newValue = curStateValue + curState.getMoveValue(CompassDirection.EAST);
                    curState.setMoveValue(CompassDirection.EAST, newValue);
                    // Check South
                    newValue = curStateValue + curState.getMoveValue(CompassDirection.SOUTH);
                    curState.setMoveValue(CompassDirection.SOUTH, newValue);
                    // Check West
                    newValue = curStateValue + curState.getMoveValue(CompassDirection.WEST);
                    curState.setMoveValue(CompassDirection.WEST, newValue);

                }
            }

            // Update optimal values
            for(int i = this.world.length - 2; i > 0; i--){
                for(int j = this.world[0].length - 1; j > 0; j--){

                    State curState = this.world[i][j];
                    float maxValue = Float.MIN_VALUE;
                    for(CompassDirection direction : CompassDirection.values()){
                        if(curState.getMoveValue(direction) > maxValue){
                            maxValue = curState.getMoveValue(direction);
                        }
                    }
                    curState.setValue(maxValue);

                }
            }

            numIterations--;
        }

    }

    @Override
    public String toString(){
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 1; i < this.world.length - 1; i++){
            for(int j = 1; j < this.world[i].length - 1; j++){
                stringBuilder.append(df.format(this.world[i][j].getValue()));
                stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public static void main(String[] args){

        float[][] worldValues = new float[5][5];
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                worldValues[i][j] = -0.4f;
            }
        }
        worldValues[0][0] = 3;
        worldValues[0][1] = Float.MIN_VALUE;
        worldValues[0][2] = Float.MIN_VALUE;
        worldValues[0][3] = Float.MIN_VALUE;
        worldValues[0][4] = 10;
        worldValues[2][2] = Float.MIN_VALUE;
        worldValues[2][4] = -10;

        int numIterations = 10;

        World world = new World(worldValues);
        world.reverseIterate(numIterations);
        System.out.println(world.toString());
    }
}
