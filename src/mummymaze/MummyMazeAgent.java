package mummymaze;

import agent.Agent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static gui.Properties.*;

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
        int lineTrap = 0;
        int columnTrap = 0;
        int lineKey = 0;
        int columnKey = 0;
        int lineExit = 0;
        int columnExit = 0;
        for (int i = 0; i < MATRIX_LINE_COLUMN_SIZE; i++) {
            String line = scanner.nextLine();
            for (int j = 0; j < MATRIX_LINE_COLUMN_SIZE; j++) {
                matrix[i][j] = line.charAt(j);
                switch (matrix[i][j]) {
                    case TRAP_CHAR -> {
                        lineTrap = i;
                        columnTrap = j;
                    }
                    case KEY_CHAR -> {
                        lineKey = i;
                        columnKey = j;
                    }
                    case EXIT_CHAR -> {
                        lineExit = i;
                        columnExit = j;
                    }
                }
            }

        }
        initialEnvironment = new MummyMazeState(matrix, lineTrap, columnTrap, lineKey, columnKey, lineExit, columnExit);
        resetEnvironment();
        return environment;
    }
}
