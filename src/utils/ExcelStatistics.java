package utils;

import agent.Heuristic;
import agent.Solution;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import searchmethods.SearchMethod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static javax.swing.JOptionPane.showMessageDialog;

public class ExcelStatistics implements IExcelStatistics{
    protected ArrayList<SearchMethod> searchMethods;
    protected SearchMethod searchMethod;
    protected Solution solution;

    public ExcelStatistics(ArrayList<SearchMethod> searchMethods, SearchMethod searchMethod, Solution solution) {
        this.searchMethods = searchMethods;
        this.searchMethod = searchMethod;
        this.solution = solution;
    }

    //Sorry for the pacman code, had to do this in a rush.
    public void generateExcelStatistics(String levelFileName, String dirPath, String statsFilename, ArrayList<Heuristic> heuristics, Heuristic heuristic) throws IOException {
        File directory = new File(dirPath);
        if (!directory.exists())
            directory.mkdir();

        File file = new File(dirPath + statsFilename);

        // This data needs to be written (Object[])
        Map<String, Object[]> searchData
                = new TreeMap<>();

        searchData.put(
                "1",
                new Object[] { "Search Name", "Cost", "Expanded Nodes", "Frontier", "Generated States", "Heuristic"});

        int excelIndex = 2;
        for (SearchMethod currSearchMethod : searchMethods) {
            Object[] rowData;

            String currSearchMethodString = currSearchMethod.toString();
            boolean isHeuristicRelatedSearch = IsHeuristicRelated(currSearchMethodString);

            //if we match the search method, let's fill the row stats
            if (this.searchMethod.equals(currSearchMethod)) {
                String solutionCost;
                if (solution == null)
                    solutionCost = "No solution found";
                else
                    solutionCost = Double.toString(solution.getCost());

                if (isHeuristicRelatedSearch)
                {
                    for (Heuristic value : heuristics) {
                        if(value.equals(heuristic))
                        {
                            rowData = generateRowData(solutionCost, heuristic, isHeuristicRelatedSearch);
                        }else {
                            rowData = new Object[]{currSearchMethodString, "", "", "", "", value.toString()};
                        }

                        searchData.put("" + excelIndex, rowData);
                        excelIndex++;
                    }
                }
                else
                {
                    rowData = generateRowData(solutionCost, null, false);
                    searchData.put("" + excelIndex, rowData);
                    excelIndex++;
                }
            } else {
                if (isHeuristicRelatedSearch) {
                    for (Heuristic value : heuristics) {
                        rowData = new Object[]{currSearchMethodString, "", "", "", "", value.toString()};
                        searchData.put("" + excelIndex, rowData);
                        excelIndex++;
                    }
                } else {
                    rowData = new Object[]{currSearchMethodString};
                    searchData.put("" + excelIndex, rowData);
                    excelIndex++;
                }
            }
        }

        if (file.exists()) {
            file.setWritable(true);
            Workbook wb = WorkbookFactory.create(file);
            Sheet sheet = wb.getSheet(levelFileName);
            if(sheet == null){
                populateSheet(wb, levelFileName, searchData);
            }
            else
                overwriteSheet(sheet, searchData, heuristic);


            //rewrite file
            try {
                boolean delete = file.delete();
                if(delete)
                    writeToFile(file, wb);
            }
            //catch doesn't throw if the user contains the file opened, because the file is read-only.
            catch (SecurityException exception)
            {
                showMessageDialog(null, exception.getMessage());
            }
        }
        else{
            // workbook object
            XSSFWorkbook workbook = new XSSFWorkbook();
            populateSheet(workbook, levelFileName, searchData);

            writeToFile(file, workbook);
        }
    }

    private void writeToFile(File file, Workbook wb) throws IOException {
        try(FileOutputStream out = new FileOutputStream(file)){
            wb.write(out);

            file.setReadOnly(); //prevent user to manipulate the data.
        }
        catch (Exception e){
        }

    }

    //https://stackoverflow.com/a/9050159
    private static int findRow(Sheet sheet, String cellContent, Heuristic heuristic, boolean isHeuristicRelated) {
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING) {
                    if (cell.getRichStringCellValue().getString().trim().equals(cellContent)) {
                        //if its heuristic related we need to analyze if we are looking for the row with the right heuristic
                        if(isHeuristicRelated) {
                            Cell heuristicCell = row.getCell(cell.getColumnIndex() + 5);
                            String heuristicCellValue = heuristicCell.getRichStringCellValue().getString().trim().toLowerCase(Locale.ROOT);
                            String heuristicName = heuristic.toString().toLowerCase(Locale.ROOT);
                            if (heuristicCellValue.equals(heuristicName)) {
                                return row.getRowNum();
                            }
                            else{
                                continue;
                            }
                        }

                        return row.getRowNum();
                    }
                }
            }
        }
        return 0;
    }

    private void populateSheet(Workbook workbook, String levelFileName, Map<String, Object[]> searchData){
        // spreadsheet object
        Sheet sheet
                = workbook.createSheet(levelFileName);

        // creating a row object
        Row row;

        Set<String> keyid = searchData.keySet();
        int rowid = 0;

        // writing the data into the sheets...

        for (String key : keyid) {

            row = sheet.createRow(rowid++);
            Object[] objectArr = searchData.get(key);

            int cellid = 0;

            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String)obj);
            }
        }

        autosizeSheetColumns(sheet, searchData);
    }

    private void overwriteSheet(Sheet sheet, Map<String, Object[]> searchData, Heuristic heuristic){
        boolean isHeuristicRelated = IsHeuristicRelated(searchMethod.toString());
        int rowIndex = findRow(sheet, this.searchMethod.toString(), heuristic, isHeuristicRelated);
        Row rowX = sheet.getRow(rowIndex);
        Set<String> keyid = searchData.keySet();
        // writing the data into the sheets...
        for (String key : keyid) {
            Object[] objectArr = searchData.get(key);
            if (objectArr[0] == this.searchMethod.toString()) {
                //found searchMethod but not with the right heuristic
                if(isHeuristicRelated)
                    if (!IsCorrectHeuristicInObjectArray(objectArr, heuristic))
                        continue;


                int cellid = 0;

                for (Object obj : objectArr) {
                    Cell cell = rowX.getCell(cellid);
                    if(cell == null || cell.getCellType() == CellType._NONE)
                        cell = rowX.createCell(cellid);

                    cell.setCellValue((String)obj);

                    cellid++;
                }
            }
        }

        autosizeSheetColumns(sheet, searchData);
    }

    private void autosizeSheetColumns(Sheet sheet, Map<String, Object[]> searchData){
        for (int i = 0; i < searchData.get("1").length; i++) {
            Objects.requireNonNull(sheet).autoSizeColumn(i);
        }
    }

    private Object[] generateRowData(String solutionCost, Heuristic heuristic, boolean isHeuristicRelatedSearch){
        return new Object[]{
                searchMethod.toString(),
                solutionCost,
                Integer.toString(searchMethod.getStatistics().numExpandedNodes),
                Integer.toString(searchMethod.getStatistics().maxFrontierSize),
                Integer.toString(searchMethod.getStatistics().numGeneratedSates),
                isHeuristicRelatedSearch && heuristic != null ? heuristic.toString() : ""
        };
    }

    private boolean IsCorrectHeuristicInObjectArray(Object[] objectArr, Heuristic heuristic){
        //found searchMethod but not with the right heuristic
        String heuristicNameTrimLowered = heuristic.toString().trim().toLowerCase(Locale.ROOT);
        String objHeuristicTrimLowered = objectArr[5].toString().trim().toLowerCase(Locale.ROOT);

        return heuristicNameTrimLowered.equals(objHeuristicTrimLowered);
    }

    private boolean IsHeuristicRelated(String searchMethodString){
        return Objects.equals(searchMethodString, "A* search")
                || Objects.equals(searchMethodString, "Beam search")
                || Objects.equals(searchMethodString, "IDA* search");
    }
}
