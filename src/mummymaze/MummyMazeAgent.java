package mummymaze;

import agent.Agent;
import mummymaze.heuristics.HeuristicTileDistance;
import mummymaze.heuristics.HeuristicTilesOutOfPlace;
import mummymaze.models.items.Key;
import mummymaze.models.items.Trap;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import static gui.Properties.*;

public class MummyMazeAgent extends Agent<MummyMazeState>{
    
    protected MummyMazeState initialEnvironment;
    
    public MummyMazeAgent(MummyMazeState environment) {
        super(environment);
        initialEnvironment = environment.clone();
        heuristics.add(new HeuristicTileDistance());
        heuristics.add(new HeuristicTilesOutOfPlace());
        heuristic = heuristics.get(0);
    }
            
    public MummyMazeState resetEnvironment(){
        environment = initialEnvironment.clone();
        return environment;
    }
                 
    public MummyMazeState readInitialStateFromFile(File file) throws IOException {
        java.util.Scanner scanner = new java.util.Scanner(file);
        char[][] matrix = new char [MATRIX_LINE_COLUMN_SIZE][MATRIX_LINE_COLUMN_SIZE];

        LinkedList<Trap> traps = new LinkedList<>();
        Key key = new Key(0,0);
        int lineExit = 0;
        int columnExit = 0;
        for (int i = 0; i < MATRIX_LINE_COLUMN_SIZE; i++) {
            String line = scanner.nextLine();
            for (int j = 0; j < MATRIX_LINE_COLUMN_SIZE; j++) {
                matrix[i][j] = line.charAt(j);
                switch (matrix[i][j]) {
                    case TRAP_CHAR -> {
                        traps.add(new Trap(i,j));
                    }
                    case KEY_CHAR -> {
                        key.line = i;
                        key.column = j;
                    }
                    case EXIT_CHAR -> {
                        lineExit = i;
                        columnExit = j;
                    }
                }
            }

        }
        initialEnvironment = new MummyMazeState(matrix, traps, key, lineExit, columnExit);
        resetEnvironment();
        return environment;
    }
}
