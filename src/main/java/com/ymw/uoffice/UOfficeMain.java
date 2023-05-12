package com.ymw.uoffice;

import com.ymw.uoffice.domain.RowData;
import com.ymw.uoffice.domain.RowsData;
import com.ymw.uoffice.domain.TableHead;
import com.ymw.uoffice.domain.XlsxData;
import com.ymw.uoffice.service.doc.AnalysisDocFile;
import com.ymw.uoffice.service.xlsx.PaddingDataGenerateXlsx;
import com.ymw.uoffice.service.xlsx.PaddingTemplateGenerateXlsx;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UOfficeMain {
    public static void main(String[] args) {
        try {
            Date date1 = new Date();
            testAnalysisDoc();
            Date date2 = new Date();
            System.out.println(date2.getTime() - date1.getTime());
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

    static void testPaddingSum() throws IOException {
        String[] code = {"local1", "local2", "local3", "cls1", "cls2", "nm", "scale", "income1", "income2"};
        String[] name = {"省", "市", "县", "大类", "子类", "名称", "规模", "本月收入", "当年累计收入"};
        String[] length = {"20", "30", "35", "20", "20", "120", "20", "20", "20"};
        String[] gC = {"local1", "local2", "local3", "cls1", "cls2"};
        String[] sC = {"scale", "income1", "income2"};
        XlsxData xlsxData = new XlsxData();
        xlsxData.setTitle("各省市地区公司规模收入情况2023-04-30");
        List<TableHead> tableHeads = new ArrayList<>();
        for (int i = 0; i < code.length; i++) {
            TableHead tableHead = new TableHead(name[i], code[i], length[i]);
            tableHeads.add(tableHead);
        }
        xlsxData.setTableHead(tableHeads);
        RowsData rowsData = new RowsData(gC, sC);
        RowData rowData = new RowData();
        rowData.put("local1", "山东省");
        rowData.put("local2", "潍坊市");
        rowData.put("local3", "青州市");
        rowData.put("cls1", "制造业");
        rowData.put("cls2", "食品制造业");
        rowData.put("nm", "山东省某某某某果品制造有限公司");
        rowData.put("scale", new BigDecimal("4532156.12"));
        rowData.put("income1", new BigDecimal("7000.00"));
        rowData.put("income2", new BigDecimal("16884.45"));
        rowsData.add(rowData);

        RowData rowData1 = new RowData();
        rowData1.put("local1", "山东省");
        rowData1.put("local2", "潍坊市");
        rowData1.put("local3", "青州市");
        rowData1.put("cls1", "制造业");
        rowData1.put("cls2", "食品制造业");
        rowData1.put("nm", "山东省某某某某小零食制造有限公司");
        rowData1.put("scale", new BigDecimal("1532156.12"));
        rowData1.put("income1", new BigDecimal("300.00"));
        rowData1.put("income2", new BigDecimal("2784.95"));
        rowsData.add(rowData1);

        RowData rowData2 = new RowData();
        rowData2.put("local1", "山东省");
        rowData2.put("local2", "潍坊市");
        rowData2.put("local3", "青州市");
        rowData2.put("cls1", "制造业");
        rowData2.put("cls2", "汽车制造业");
        rowData2.put("nm", "山东省某某某某新能源汽车制造有限公司");
        rowData2.put("scale", new BigDecimal("15321560.12"));
        rowData2.put("income1", new BigDecimal("300144.00"));
        rowData2.put("income2", new BigDecimal("2784000.95"));
        rowsData.add(rowData2);

        RowData rowData3 = new RowData();
        rowData3.put("local1", "山东省");
        rowData3.put("local2", "潍坊市");
        rowData3.put("local3", "青州市");
        rowData3.put("cls1", "化工业");
        rowData3.put("cls2", "石油化工业");
        rowData3.put("nm", "山东省某某某某石油化工及化学原料加工股份有限公司");
        rowData3.put("scale", new BigDecimal("74321560.12"));
        rowData3.put("income1", new BigDecimal("600144.00"));
        rowData3.put("income2", new BigDecimal("7784000.95"));
        rowsData.add(rowData3);


        RowData rowData4 = new RowData();
        rowData4.put("local1", "山东省");
        rowData4.put("local2", "青岛市");
        rowData4.put("local3", "青岛市");
        rowData4.put("cls1", "旅游业");
        rowData4.put("cls2", "海洋旅游业");
        rowData4.put("nm", "山东省某某某某滨海海洋动物管理股份有限公司");
        rowData4.put("scale", new BigDecimal("7431560.12"));
        rowData4.put("income1", new BigDecimal("60144.00"));
        rowData4.put("income2", new BigDecimal("784000.95"));
        rowsData.add(rowData4);


        RowData rowData5 = new RowData();
        rowData5.put("local1", "北京市");
        rowData5.put("local2", "北京市");
        rowData5.put("local3", "北京市");
        rowData5.put("cls1", "制造业");
        rowData5.put("cls2", "电脑制造业");
        rowData5.put("nm", "中国某某某某电子精密制造股份有限公司");
        rowData5.put("scale", new BigDecimal("8431560.12"));
        rowData5.put("income1", new BigDecimal("61144.00"));
        rowData5.put("income2", new BigDecimal("794050.75"));
        rowsData.add(rowData5);
        xlsxData.setRowsData(rowsData);
        PaddingDataGenerateXlsx paddingDataGenerateXlsx = new PaddingDataGenerateXlsx();
        paddingDataGenerateXlsx.generateToFile(xlsxData, "D:\\DATA\\20230509\\tableSum.xlsx", false);
    }

    static void testPaddingTemplate() throws IOException {
        XlsxData xlsxData = new XlsxData();
        RowsData rowsData = new RowsData();
        RowData rowData = new RowData();
        rowData.put("name", "杨**");
        rowData.put("country", "中国");
        rowData.put("idType", "身份证");
        rowData.put("id", "1234567899876543210");
        rowData.put("phone", "12345678998");
        rowData.put("address", "**省**********");
        rowData.put("income1", new BigDecimal("1000"));
        rowData.put("income2", new BigDecimal("0"));
        rowData.put("income3", new BigDecimal("1000"));
        rowData.put("income4", new BigDecimal("1000"));
        rowData.put("income5", new BigDecimal("0"));
        rowData.put("income6", new BigDecimal("1000"));
        rowData.put("income7", new BigDecimal("1000"));
        rowData.put("income8", new BigDecimal("0"));
        rowData.put("income9", new BigDecimal("1000"));
        rowData.put("income10", new BigDecimal("1000"));
        rowData.put("income11", new BigDecimal("0"));
        rowData.put("income12", new BigDecimal("1000"));
        rowData.put("sum1", new BigDecimal("4000"));
        rowData.put("sum2", new BigDecimal("0"));
        rowData.put("sum3", new BigDecimal("4000"));
        rowData.put("b1", "");
        rowData.put("b2", "");
        rowData.put("b3", "");
        rowData.put("b4", "");
        rowsData.add(rowData);
        xlsxData.setRowsData(rowsData);
        PaddingTemplateGenerateXlsx paddingTemplateGenerateXlsx = new PaddingTemplateGenerateXlsx("D:\\DATA\\20230511\\1.xlsx");
        paddingTemplateGenerateXlsx.generateToFile(xlsxData, "D:\\DATA\\20230511\\test.xlsx", false);
    }

    static void testAnalysisDoc() throws IOException {
        AnalysisDocFile analysisDocFile = new AnalysisDocFile();
        analysisDocFile.analysis("D:\\DATA\\20230511\\1.doc");
    }
}
