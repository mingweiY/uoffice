package com.ymw.uoffice.service.xlsx;

import com.ymw.uoffice.constant.Constant;
import com.ymw.uoffice.domain.*;
import com.ymw.uoffice.service.UOfficeTemplateNew;
import com.ymw.uoffice.utils.Base64Utils;
import com.ymw.uoffice.utils.ObjectUtils;
import com.ymw.uoffice.utils.PathUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaddingDataGenerateXlsx extends UOfficeTemplateNew {
    @Override
    public void generateToFile(XlsxData xlsxData, String filePath, boolean deleteTempFile) throws IOException {
        initNormalTemplate();
        paddingData(xlsxData);
        writeToFile(filePath);
        if (deleteTempFile) {
            PathUtils.deleteAllDir(templatePath);
        }
    }

    @Override
    public void generateToStream(XlsxData xlsxData, OutputStream outputStream, boolean deleteTempFile) throws IOException {
        initNormalTemplate();
        paddingData(xlsxData);
        writeToStream(outputStream);
        if (deleteTempFile) {
            PathUtils.deleteAllDir(templatePath);
        }
    }

    private void initNormalTemplate() {
        byte[] xlsxTemplateBytes = Base64Utils.decodeFromString(Constant.BASE64_XLSX_TEMPLATE);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xlsxTemplateBytes);
        initTemplate(byteArrayInputStream);
    }

    private void paddingData(XlsxData xlsxData) throws IOException {
        if (!xlsxData.check()) {
            xlsxData.getRowsData().generateEndSumInfo();
            SharedStrings sharedStrings = convertToSharedStrings(xlsxData);
            generateSharedStrings(sharedStrings);
            SheetData sheetData = convertToSheetData(xlsxData, sharedStrings);
            generateSheetData(sheetData);
        }
    }

    private SharedStrings convertToSharedStrings(XlsxData xlsxData) {
        SharedStrings sharedStrings = new SharedStrings();
        sharedStrings.addOne(xlsxData.getTitle());
        if (ObjectUtils.isNotEmpty(xlsxData.getTableHead())) {
            for (TableHead t : xlsxData.getTableHead()) {
                sharedStrings.addOne(t.getName());
            }
        }
        if (ObjectUtils.isNotEmpty(xlsxData.getRowsData())) {
            for (RowData c : xlsxData.getRowsData()) {
                Map<String, CellData> rowData = c.getRowData();
                for (CellData cellData : rowData.values()) {
                    if (CellDataType.STRING.equals(cellData.getCellDataType())) {
                        sharedStrings.addOne(cellData.getName());
                    }
                }
            }
        }
        return sharedStrings;
    }

    private SheetData convertToSheetData(XlsxData xlsxData, SharedStrings sharedStrings) {
        SheetData sheetData = new SheetData();
        int maxWidth = xlsxData.getMaxWidth();
        int maxHeight = xlsxData.getMaxHeight();
        String rowCode = SheetData.getRowCode(maxWidth-1);
        sheetData.setMergeCells(generateMergeCells(xlsxData, rowCode));
        rowCode += maxHeight;
        sheetData.setDimension(rowCode);
        sheetData.setSheetDataS(generateSheetData(xlsxData, sharedStrings, maxWidth));
        sheetData.setCellWidth(generateCellWidth(xlsxData));
        return sheetData;
    }

    private String generateCellWidth(XlsxData xlsxData) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!ObjectUtils.isEmpty(xlsxData.getTableHead())) {
            if (xlsxData.getTableHead().stream().anyMatch(x->ObjectUtils.isNotEmpty(x.getLength()))) {
                stringBuilder.append("<cols>");
                for (int i = 0; i < xlsxData.getTableHead().size(); i++) {
                    if (!ObjectUtils.isEmpty(xlsxData.getTableHead().get(i).getLength())) {
                        stringBuilder.append("<col min=\"");
                        stringBuilder.append(i + 1);
                        stringBuilder.append("\" max=\"");
                        stringBuilder.append(i + 1);
                        stringBuilder.append("\" width=\"");
                        stringBuilder.append(xlsxData.getTableHead().get(i).getLength());
                        stringBuilder.append("\"/>");
                    }
                }
                stringBuilder.append("</cols>");
            }
        }
        return stringBuilder.toString();
    }

    private String generateMergeCells(XlsxData xlsxData, String rowCode) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!ObjectUtils.isEmpty(xlsxData.getTitle())) {
            stringBuilder.append("<mergeCells count=\"1\">");
            stringBuilder.append("<mergeCell ref=\"A1:");
            stringBuilder.append(rowCode);
            stringBuilder.append("1\"/>");
            stringBuilder.append("</mergeCells>");
        }
        return stringBuilder.toString();
    }

    private String generateSheetData(XlsxData xlsxData, SharedStrings sharedStrings, int maxWidth) {
        Map<String, Integer> indexMap = sharedStrings.getIndex();
        StringBuilder stringBuilder = new StringBuilder("<sheetData>");
        int height = 0;
        if (!ObjectUtils.isEmpty(xlsxData.getTitle())) {
            height++;
            generateSheetData(maxWidth, stringBuilder, indexMap, xlsxData.getTitle());
        }
        if (!ObjectUtils.isEmpty(xlsxData.getTableHead())) {
            height++;
            generateSheetData(height, stringBuilder, indexMap, xlsxData.getTableHead());
        }
        if (!ObjectUtils.isEmpty(xlsxData.getRowsData())) {
            height++;
            generateSheetData(height, stringBuilder, indexMap, xlsxData.getRowsData(), xlsxData.getTableHead());
        }
        stringBuilder.append("</sheetData>");
        return stringBuilder.toString();
    }

    private void generateSheetData(int maxWidth, StringBuilder stringBuilder, Map<String, Integer> indexMap,
                                   String title) {
        stringBuilder.append("<row r=\"1\" ht=\"25.5\" spans=\"1:");
        stringBuilder.append(maxWidth);
        stringBuilder.append("\">");
        for (int w = 0; w < maxWidth; w++) {
            stringBuilder.append("<c r=\"");
            stringBuilder.append(SheetData.getRowCode(w));
            if (w == 0) {
                stringBuilder.append("1\" s=\"1\" t=\"s\"><v>");
                stringBuilder.append(indexMap.get(title));
                stringBuilder.append("</v></c>");
            } else {
                stringBuilder.append("1\" s=\"1\"/>");
            }
        }
        stringBuilder.append("</row>");
    }

    private void generateSheetData(int height, StringBuilder stringBuilder, Map<String, Integer> indexMap,
                                   List<TableHead> tableHeads) {
        stringBuilder.append("<row r=\"");
        stringBuilder.append(height);
        stringBuilder.append("\" ht=\"14.25\" spans=\"1:");
        stringBuilder.append(tableHeads.size());
        stringBuilder.append("\">");
        for (int w = 0; w < tableHeads.size(); w++) {
            stringBuilder.append("<c r=\"");
            stringBuilder.append(SheetData.getRowCode(w));
            stringBuilder.append(height);
            stringBuilder.append("\" s=\"2\" t=\"s\"><v>");
            stringBuilder.append(indexMap.get(tableHeads.get(w).getName()));
            stringBuilder.append("</v></c>");
        }
        stringBuilder.append("</row>");
    }

    private void generateSheetData(int height, StringBuilder stringBuilder, Map<String, Integer> indexMap,
                                   RowsData rowsData, List<TableHead> tableHeads) {
        if (ObjectUtils.isEmpty(tableHeads)) {
            generateSheetDataNotBandingTableHead(height, stringBuilder, indexMap, rowsData);
        } else {
            generateSheetDataBandingTableHead(height, stringBuilder, indexMap, rowsData, tableHeads);
        }
    }

    private void generateSheetDataBandingTableHead(int height, StringBuilder stringBuilder,
                                                   Map<String, Integer> indexMap,
                                                   RowsData rowsData, List<TableHead> tableHeads) {
        for (int h = 0; h < rowsData.size(); h++) {
            Map<String, CellData> rowData = rowsData.get(h).getRowData();
            stringBuilder.append("<row r=\"");
            stringBuilder.append(height+h);
            stringBuilder.append("\" ht=\"14.25\" spans=\"1:");
            stringBuilder.append(tableHeads.size());
            stringBuilder.append("\">");
            for (int w = 0; w < tableHeads.size(); w++) {
                stringBuilder.append("<c r=\"");
                stringBuilder.append(SheetData.getRowCode(w));
                stringBuilder.append(height+h);
                CellData cellData = rowData.get(tableHeads.get(w).getCode());
                if (cellData == null || cellData.getName() == null) {
                    stringBuilder.append("\" s=\"3\"/>");
                } else {
                    switch (cellData.getCellDataType()) {
                        case STRING:
                            stringBuilder.append("\" s=\"3\" t=\"s\"><v>");
                            stringBuilder.append(indexMap.get(cellData.getName()));
                            stringBuilder.append("</v></c>");
                            break;
                        case NUMBER:
                            stringBuilder.append("\" s=\"3\"><v>");
                            stringBuilder.append(cellData.getName());
                            stringBuilder.append("</v></c>");
                            break;
                    }
                }
            }
            stringBuilder.append("</row>");
        }
    }

    private void generateSheetDataNotBandingTableHead(int height, StringBuilder stringBuilder,
                                                      Map<String, Integer> indexMap, RowsData rowsData) {
        for (int h = 0; h < rowsData.size(); h++) {
            List<CellData> values = new ArrayList<>(rowsData.get(h).getRowData().values());
            stringBuilder.append("<row r=\"");
            stringBuilder.append(height+h);
            stringBuilder.append("\" ht=\"14.25\" spans=\"1:");
            stringBuilder.append(values.size());
            stringBuilder.append("\">");
            for (int w = 0; w < values.size(); w++) {
                stringBuilder.append("<c r=\"");
                stringBuilder.append(SheetData.getRowCode(w));
                stringBuilder.append(height+h);
                if (values.get(w) == null || values.get(w).getName() == null) {
                    stringBuilder.append("\" s=\"3\"/>");
                } else {
                    switch (values.get(w).getCellDataType()) {
                        case STRING:
                            stringBuilder.append("\" s=\"3\" t=\"s\"><v>");
                            stringBuilder.append(indexMap.get(values.get(w).getName()));
                            stringBuilder.append("</v></c>");
                            break;
                        case NUMBER:
                            stringBuilder.append("\" s=\"3\"><v>");
                            stringBuilder.append(values.get(w).getName());
                            stringBuilder.append("</v></c>");
                            break;
                    }
                }
            }
            stringBuilder.append("</row>");
        }
    }

    private void generateSharedStrings(SharedStrings sharedStrings) throws IOException {
        if (!ObjectUtils.isEmpty(sharedStrings)) {
            String filePath = templatePath + File.separator + "xl" + File.separator + "sharedStrings.xml";
            FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath));
            fileOutputStream.write(Constant.XML_HEAD.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.write(sharedStrings.getSstStart().getBytes(StandardCharsets.UTF_8));
            String[] si = sharedStrings.getSi();
            for (String s : si) {
                fileOutputStream.write(s.getBytes(StandardCharsets.UTF_8));
            }
            fileOutputStream.write("</sst>".getBytes(StandardCharsets.UTF_8));
            fileOutputStream.close();
        }
    }

    private void generateSheetData(SheetData sheetData) throws IOException {
        String filePath = templatePath + File.separator + "xl"+ File.separator + "worksheets" + File.separator +"sheet1.xml";
        FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath));
        fileOutputStream.write(Constant.XML_HEAD.getBytes(StandardCharsets.UTF_8));
        fileOutputStream.write(sheetData.getWorkSheet().getBytes(StandardCharsets.UTF_8));
        fileOutputStream.write(sheetData.getSheetPr().getBytes(StandardCharsets.UTF_8));
        fileOutputStream.write(sheetData.getDimension().getBytes(StandardCharsets.UTF_8));
        fileOutputStream.write(sheetData.getSheetViews().getBytes(StandardCharsets.UTF_8));
        fileOutputStream.write(sheetData.getSheetFormatPr().getBytes(StandardCharsets.UTF_8));
        fileOutputStream.write(sheetData.getCellWidth().getBytes(StandardCharsets.UTF_8));
        fileOutputStream.write(sheetData.getSheetDataS().getBytes(StandardCharsets.UTF_8));
        fileOutputStream.write(sheetData.getMergeCells().getBytes(StandardCharsets.UTF_8));
        fileOutputStream.write(sheetData.getPageMargins().getBytes(StandardCharsets.UTF_8));
        fileOutputStream.write(sheetData.getPageSetup().getBytes(StandardCharsets.UTF_8));
        fileOutputStream.write(sheetData.getHeaderFooter().getBytes(StandardCharsets.UTF_8));
        fileOutputStream.write(sheetData.getWorksheetEnd().getBytes(StandardCharsets.UTF_8));
        fileOutputStream.close();
    }

}
