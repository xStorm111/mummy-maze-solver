package utils;

import agent.Solution;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import searchmethods.SearchMethod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static javax.swing.JOptionPane.showMessageDialog;

public class ExcelStatistics {
    protected ArrayList<SearchMethod> searchMethods;
    protected SearchMethod searchMethod;
    protected Solution solution;

    public ExcelStatistics(ArrayList<SearchMethod> searchMethods, SearchMethod searchMethod, Solution solution) {
        this.searchMethods = searchMethods;
        this.searchMethod = searchMethod;
        this.solution = solution;
    }

    public void generateExcelStatistics(String levelFileName, String dirPath, String statsFilename) throws IOException {
        File directory = new File(dirPath);
        if (!directory.exists())
            directory.mkdir();

        File file = new File(dirPath + statsFilename);

        // This data needs to be written (Object[])
        Map<String, Object[]> searchData
                = new TreeMap<>();

        searchData.put(
                "1",
                new Object[] { "Search Name", "Cost", "Expanded Nodes", "Frontier", "Generated States"});

        for (int i = 0; i < searchMethods.size(); i++) {
            int excelIndex = i+2;
            var currSearchMethod = searchMethods.get(i);
            Object[] rowData;

            //if we match the search method, let's fill the row stats
            if (this.searchMethod.equals(currSearchMethod))
            {
                String solutionCost;
                if (solution == null)
                    solutionCost = "No solution found";
                else
                    solutionCost = Double.toString(solution.getCost());

                rowData = new Object[]{
                        currSearchMethod.toString(),
                        solutionCost,
                        Integer.toString(searchMethod.getStatistics().numExpandedNodes),
                        Integer.toString(searchMethod.getStatistics().maxFrontierSize),
                        Integer.toString(searchMethod.getStatistics().numGeneratedSates)
                };
            }
            else {
                rowData = new Object[]{currSearchMethod.toString()};
            }

            searchData.put("" + excelIndex, rowData);
        }

        if (file.exists()) {
            file.setWritable(true);
            Workbook wb = WorkbookFactory.create(file);
            Sheet sheet = wb.getSheet(levelFileName);
            if(sheet == null){
                populateSheet(wb, levelFileName, searchData);
            }
            else
                overwriteSheet(sheet, searchData);


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
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);

        file.setReadOnly(); //prevent user to manipulate the data.
        out.close();
    }

    //https://stackoverflow.com/a/9050159
    private static int findRow(Sheet sheet, String cellContent) {
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellType() == CellType.STRING) {
                    if (cell.getRichStringCellValue().getString().trim().equals(cellContent)) {
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

    private void overwriteSheet(Sheet sheet, Map<String, Object[]> searchData){
        int rowIndex = findRow(sheet, this.searchMethod.toString());
        Row rowX = sheet.getRow(rowIndex);
        Set<String> keyid = searchData.keySet();
        // writing the data into the sheets...
        for (String key : keyid) {
            Object[] objectArr = searchData.get(key);
            if (objectArr[0] == this.searchMethod.toString()) {
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
}
