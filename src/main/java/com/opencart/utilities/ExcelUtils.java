/* 
 * /src/main/java/com/opencart/utilities/ExcelUtils.java
 */
package com.opencart.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

    private Workbook workbook;
    private Sheet sheet;

    public ExcelUtils(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("[FAILED] Sheet '" + sheetName + "' not found in file: " + filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] Unable to open Excel file: " + filePath + " - " + e.getMessage(), e);
        }
    }

    public int getRowCount() {
        return sheet.getPhysicalNumberOfRows();
    }

    public int getCellCount(int rowIndex) {
        Row row = sheet.getRow(rowIndex);
        return (row != null) ? row.getPhysicalNumberOfCells() : 0;
    }

    public String getCellData(int rowIndex, int colIndex) {
        try {
            Row row = sheet.getRow(rowIndex);
            if (row == null) return "";
            Cell cell = row.getCell(colIndex);
            if (cell == null) return "";

            DataFormatter formatter = new DataFormatter();
            return formatter.formatCellValue(cell);
        } catch (Exception e) {
            return "";
        }
    }

    public Object[][] getSheetDataAs2DArray() {
        int rowCount = getRowCount();
        int colCount = getCellCount(0);
        Object[][] data = new Object[rowCount - 1][colCount];

        for (int i = 1; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                data[i - 1][j] = getCellData(i, j);
            }
        }
        return data;
    }

    public void close() {
        try {
            workbook.close();
        } catch (IOException e) {
            // ignore
        }
    }
}
