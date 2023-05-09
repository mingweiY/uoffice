package com.ymw.uoffice.domain;

import com.ymw.uoffice.constant.Constant;
import com.ymw.uoffice.utils.ObjectUtils;

/**
 * @author : yangmingwei
 * @description : sheetData.xml对应的实体类
 * @since : 2023/4/10 14:16
 */
public class SheetData {
    private String dimension;

    private String sheetDataS;

    private String mergeCells;

    private String cellWidth;


    public String getWorkSheet() {
        return Constant.WORKSHEET;
    }

    public String getSheetViews() {
        return "<sheetViews><sheetView tabSelected=\"1\" workbookViewId=\"0\"><selection activeCell=\"A1\" sqref=\"A1\"/></sheetView></sheetViews>";
    }

    public String getSheetFormatPr() {
        return "<sheetFormatPr defaultColWidth=\"9\" defaultRowHeight=\"13.5\"/>";
    }

    public String getSheetPr() {
        return "<sheetPr/>";
    }

    public String getDimension() {
        return this.dimension;
    }

    public void setDimension(String value) {
        String dimension = "<dimension ref=\"A1";
        if (ObjectUtils.isNotEmpty(value)) {
            dimension += ":";
            dimension += value;
        }
        dimension += "\"/>";
        this.dimension = dimension;
    }

    public static String getRowCode(int num) {
        String code = "";
        if (num >= 26) {
            code += Constant.A_TO_Z_SET[(num / 26)-1];
        }
        code += Constant.A_TO_Z_SET[num % 26];
        return code;
    }


    public String getSheetDataS() {
        return sheetDataS;
    }

    public void setSheetDataS(String sheetDataS) {
        this.sheetDataS = sheetDataS;
    }

    public String getMergeCells() {
        return mergeCells;
    }

    public void setMergeCells(String mergeCells) {
        this.mergeCells = mergeCells;
    }

    public String getPageMargins() {
        return "<pageMargins left=\"0.7\" right=\"0.7\" top=\"0.75\" bottom=\"0.75\" header=\"0.3\" footer=\"0.3\"/>";
    }

    public String getPageSetup() {
        return "<pageSetup paperSize=\"9\" orientation=\"portrait\"/>";
    }

    public String getHeaderFooter() {
        return "<headerFooter/>";
    }

    public String getWorksheetEnd() {
        return "</worksheet>";
    }

    public String getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(String cellWidth) {
        this.cellWidth = cellWidth;
    }
}
