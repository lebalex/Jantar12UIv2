/**
 *
 * @author ivc_LebedevAV
 * 2012-08-08
 */

package helpers;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

public class XLSHelper {
    public static void createCell(Workbook wb, Sheet sheet, int column, int rown, int halign, int valign, double val)
    {
        createCell(wb, sheet, column, rown, halign, valign, val, true);
    }
    public static void createCell(Workbook wb, Sheet sheet, int column, int rown, int halign, int valign, String val)
    {
        createCell(wb, sheet, column, rown, halign, valign, val, true);
    }
    public static void createCell(Workbook wb, Sheet sheet, int column, int rown, int halign, int valign, double val, boolean border) {
        Row row = sheet.getRow(rown);
        if(row==null)
            row = sheet.createRow(rown);
        Cell cell = row.createCell(column);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);

        cell.setCellValue(val);
        cell.setCellStyle(setStyle(wb, halign, valign, border));
    }
    /*public static void createCellFormula(Workbook wb, Sheet sheet, int column, int rown, int halign, int valign, double val, boolean border) {
        Row row = sheet.getRow(rown);
        if(row==null)
            row = sheet.createRow(rown);
        Cell cell = row.createCell(column);
        cell.setCellType(Cell.CELL_TYPE_FORMULA);
        cell.setCellValue(val);
        cell.setCellStyle(setStyle(wb, halign, valign, border));
    }*/
    public static void createCell(Workbook wb, Sheet sheet, int column, int rown, int halign, int valign, String val, boolean border) {
        Row row = sheet.getRow(rown);
        if(row==null)
            row = sheet.createRow(rown);
        Cell cell = row.createCell(column);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue(val);
        cell.setCellStyle(setStyle(wb, halign, valign, border));
    }
    public static void setStyleRange(Workbook wb, Sheet sheet,int firstRow, int lastRow, int firstCol, int lastCol)
    {
        //org.apache.poi.ss.util.CellRangeAddress cra = new org.apache.poi.ss.util.CellRangeAddress(9, 9, 1, 6);
        for(int i=firstRow;i<=lastRow;i++){
            for(int j=firstCol;j<=lastCol;j++){
                Row row = sheet.getRow(i);
                if(row!=null){
                    Cell cell = row.getCell(j);
                    if(cell==null)
                        cell = row.createCell(j);
                    cell.setCellStyle(setStyle(wb, 0, 0, true));
                }
            }
        }
    }
    public static void insertCell(Workbook wb, Sheet sheet, int column, int rown, int halign, int valign, String val, boolean border)
    {
        sheet.shiftRows(rown, sheet.getLastRowNum(), 1, true, false);
        sheet.createRow(rown);
        createCell(wb, sheet, column, rown, halign, valign, val, border);
    }
    public static void insertCell(Workbook wb, Sheet sheet, int column, int rown, int halign, int valign, double val, boolean border)
    {
        sheet.shiftRows(rown, sheet.getLastRowNum(), 1, true, false);
        sheet.createRow(rown);
        createCell(wb, sheet, column, rown, halign, valign, val, border);
    }
    public static void insertCell(Workbook wb, Sheet sheet, int column, int rown, int halign, int valign, String val)
    {
        sheet.shiftRows(rown, sheet.getLastRowNum(), 1, true, false);
        sheet.createRow(rown);
        createCell(wb, sheet, column, rown, halign, valign, val, true);
    }
    public static void insertCell(Workbook wb, Sheet sheet, int column, int rown, int halign, int valign, double val)
    {
        if(rown<sheet.getLastRowNum())
            sheet.shiftRows(rown, sheet.getLastRowNum(), 1, true, false);
        sheet.createRow(rown);
        createCell(wb, sheet, column, rown, halign, valign, val, true);
    }
    public static int copyPasteRange(Workbook wb, Sheet sheet, int columnStart, int rownStart, int columnStop, int rownStop, int columnDest, int rownDest)
    {
        return copyPasteRange(wb, sheet, columnStart, rownStart, columnStop, rownStop, columnDest, rownDest, null);
    }
    public static int copyPasteRange(Workbook wb, Sheet sheet, int columnStart, int rownStart, int columnStop, int rownStop, int columnDest, int rownDest, String replace)
    {
        int resutlEndRow=-1;
        int rownDestT=rownDest;
        for (int r = rownStart; r <= rownStop; r++) {
            Row row = sheet.getRow(r);
            Row newRow = sheet.createRow(rownDestT);
            int columnDestT = columnDest;
            for (int c = columnStart; c <= columnStop; c++) {
                Cell cell = row.getCell(c);
                if (cell != null) {
                    Cell newCell = newRow.createCell(columnDestT);
                    newCell.setCellStyle(cell.getCellStyle());
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_BLANK:
                            newCell.setCellValue(cell.getStringCellValue());
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            newCell.setCellValue(cell.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_ERROR:
                            newCell.setCellErrorValue(cell.getErrorCellValue());
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            newCell.setCellFormula(cell.getCellFormula());
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            newCell.setCellValue(cell.getNumericCellValue());
                            break;
                        case Cell.CELL_TYPE_STRING:{
                            newCell.setCellValue(StringHelper.replaceAll(cell.getRichStringCellValue().toString(), "#", replace));
                            break;
                        }
                    }
                }


                columnDestT++;
            }
            rownDestT++;

        }
        resutlEndRow=rownDestT;
        CellRangeAddress craL = null;
        rownDestT=rownDest;
        int columnDestT = columnDest;
        int countMerg = sheet.getNumMergedRegions();
        for (int r = 0; r < countMerg; r++) {
            CellRangeAddress cra = sheet.getMergedRegion(r);
            if (cra != null) {
                if (cra.getFirstRow() >= rownStart && cra.getFirstColumn() >= columnStart && cra.getLastRow() <= rownStop && cra.getLastColumn() <= columnStop) {
                    if (craL != cra) {
                        CellRangeAddress craDest = new CellRangeAddress(rownDestT + cra.getFirstRow(), rownDestT + cra.getFirstRow() + (cra.getLastRow() - cra.getFirstRow()), columnDestT + cra.getFirstColumn(), columnDestT + cra.getFirstColumn() + (cra.getLastColumn() - cra.getFirstColumn()));
                        sheet.addMergedRegion(craDest);
                        craL = cra;
                    }
                }
            }
        }
        return resutlEndRow;
    }
    public static String getCellValue(Workbook wb, Sheet sheet, int column, int rown)
    {
        Row row = sheet.getRow(rown);
        Cell cell = null;
        if(row!=null)
            cell = row.getCell(column);
        if(cell!=null)
           return cell.getStringCellValue();
        else
            return null;
    }
    private static CellStyle setStyle(Workbook wb, int halign, int valign, boolean border)
    {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment((short)halign);
        cellStyle.setVerticalAlignment((short)valign);
        if(border){
            cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            cellStyle.setBorderRight(CellStyle.BORDER_THIN);
            cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        }
        return cellStyle;
    }
    public static void setMerge(Sheet sheet, int c1, int r1, int c2, int r2)
    {
        sheet.addMergedRegion(new CellRangeAddress(
            r1, //first row (0-based)
            r2, //last row  (0-based)
            c1, //first column (0-based)
            c2  //last column  (0-based)
    ));
    }
    /*public static void setMerge(Workbook wb, Sheet sheet, int c1, int r1, int c2, int r2, boolean border)
    {
        sheet.addMergedRegion(new CellRangeAddress(
            r1, //first row (0-based)
            r2, //last row  (0-based)
            c1, //first column (0-based)
            c2  //last column  (0-based)
    ));
        if(border){
            Row row = sheet.getRow(r1);
            if(row==null)
                row = sheet.createRow(r1);
            Cell cell = row.createCell(c1);
            cell.setCellStyle(setStyle(wb, 0, 0, border));
        }
    }*/
}
