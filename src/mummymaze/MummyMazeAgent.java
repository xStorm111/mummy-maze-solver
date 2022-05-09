package mummymaze;

import agent.Agent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static gui.Properties.MATRIX_LINE_COLUMN_SIZE;

public class MummyMazeAgent extends Agent<MummyMazeState>{
    
    protected MummyMazeState initialEnvironment;
    
    public MummyMazeAgent(MummyMazeState environemt) {
        super(environemt);
        initialEnvironment = (MummyMazeState) environemt.clone();
        heuristics.add(new HeuristicTileDistance());
        heuristics.add(new HeuristicTilesOutOfPlace());
        heuristic = heuristics.get(0);
    }
            
    public MummyMazeState resetEnvironment(){
        environment = (MummyMazeState) initialEnvironment.clone();
        return environment;
    }
                 
    public MummyMazeState readInitialStateFromFile(File file) throws IOException {
        java.util.Scanner scanner = new java.util.Scanner(file);
        char[][] matrix = new char [MATRIX_LINE_COLUMN_SIZE][MATRIX_LINE_COLUMN_SIZE];
        
        for (int i = 0; i < MATRIX_LINE_COLUMN_SIZE; i++) {
            String line = scanner.nextLine();
            for (int j = 0; j < MATRIX_LINE_COLUMN_SIZE; j++) {
                matrix[i][j] = line.charAt(j);
            }

        }
        initialEnvironment = new MummyMazeState(matrix);
        resetEnvironment();
        return environment;
    }
}
