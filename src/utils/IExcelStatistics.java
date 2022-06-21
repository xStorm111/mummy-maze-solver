package utils;

import agent.Heuristic;

import java.io.IOException;
import java.util.ArrayList;

public interface IExcelStatistics {
    void generateExcelStatistics(String levelFileName, String dirPath, String statsFilename, ArrayList<Heuristic> heuristics, Heuristic heuristic) throws IOException;
}
