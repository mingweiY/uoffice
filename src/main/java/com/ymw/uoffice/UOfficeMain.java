package com.ymw.uoffice;

import com.ymw.uoffice.domain.RowData;
import com.ymw.uoffice.domain.RowsData;
import com.ymw.uoffice.domain.TableHead;
import com.ymw.uoffice.domain.XlsxData;
import com.ymw.uoffice.service.xlsx.PaddingDataGenerateXlsx;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UOfficeMain {
    public static void main(String[] args) {
        try {
            testPadding();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void testPadding() throws IOException {
        String[] code = {"local1", "local2", "local3", "cls1", "cls2", "nm", "scale", "income1", "income2"};
        String[] name = {"省", "市", "县", "大类", "子类", "名称", "规模", "本月收入", "当年累计收入"};
        String[] length = {"20", "30", "35", "20", "20", "120", "20", "20", "20"};
        XlsxData xlsxData = new XlsxData();
        xlsxData.setTitle("各省市地区公司规模收入情况2023-04-30");
        List<TableHead> tableHeads = new ArrayList<>();
        for (int i = 0; i < code.length; i++) {
            TableHead tableHead = new TableHead(name[i], code[i], length[i]);
            tableHeads.add(tableHead);
        }
        xlsxData.setTableHead(tableHeads);
        RowsData rowsData = new RowsData();
        for (int i = 0; i < 30000; i++) {
            RowData rowData = new RowData();
            rowData.put("local1", "山东省");
            rowData.put("local2", "潍坊市");
            rowData.put("local3", "青州市");
            rowData.put("cls1", "制造业");
            rowData.put("cls2", "食品制造业");
            rowData.put("nm", "山东省某某某某果品厂制造有限公司");
            rowData.put("scale", new BigDecimal("4532156.12"));
            rowData.put("income1", new BigDecimal("7000.00"));
            rowData.put("income2", new BigDecimal("16884.45"));
            rowsData.add(rowData);
        }
        xlsxData.setRowsData(rowsData);
        PaddingDataGenerateXlsx paddingDataGenerateXlsx = new PaddingDataGenerateXlsx();
        paddingDataGenerateXlsx.generateToFile(xlsxData, "D:\\DATA\\20230509\\table.xlsx", false);
    }
}
