package com.ymw.uoffice.service.xlsx;

import com.ymw.uoffice.constant.Constant;
import com.ymw.uoffice.domain.CellData;
import com.ymw.uoffice.domain.CellDataType;
import com.ymw.uoffice.domain.RowsData;
import com.ymw.uoffice.domain.XlsxData;
import com.ymw.uoffice.exception.UOfficeException;
import com.ymw.uoffice.service.UOfficeTemplateNew;
import com.ymw.uoffice.utils.ObjectUtils;
import com.ymw.uoffice.utils.PathUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PaddingTemplateGenerateXlsx extends UOfficeTemplateNew {
    private final String templateFilePath;

    public PaddingTemplateGenerateXlsx(String templateFilePath) {
        if (ObjectUtils.isEmpty(templateFilePath)) {
            throw new UOfficeException("文件模板路径为空");
        }
        this.templateFilePath = templateFilePath;
    }

    @Override
    public void generateToFile(XlsxData xlsxData, String filePath, boolean deleteTempFile) throws IOException {
        initSpecialTemplate();
        paddingData(xlsxData);
        writeToFile(filePath);
    }

    @Override
    public void generateToStream(XlsxData xlsxData, OutputStream outputStream, boolean deleteTempFile) throws IOException {
        initSpecialTemplate();
        paddingData(xlsxData);
        writeToStream(outputStream);
        if (deleteTempFile) {
            PathUtils.deleteAllDir(templatePath);
        }
    }

    public void initSpecialTemplate() throws FileNotFoundException {
        File file = new File(this.templateFilePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        initTemplate(fileInputStream);
    }

    public void paddingData(XlsxData xlsxData) throws IOException {
        RowsData rowsData = xlsxData.getRowsData();
        if (!ObjectUtils.isEmpty(rowsData)) {
            Map<String, CellData> rowData = rowsData.get(0).getRowData();
            if (!ObjectUtils.isEmpty(rowData)) {
                String sharedStrings = templatePath + File.separator + "xl" + File.separator + "sharedStrings.xml";
                File sharedStringsFile = new File(sharedStrings);
                if (sharedStringsFile.exists()) {
                    FileReader fileReader = new FileReader(sharedStringsFile);
                    int read;
                    char[] buffer = new char[1024];
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((read = fileReader.read(buffer)) != -1) {
                        stringBuilder.append(buffer, 0, read);
                    }
                    for (String key : rowData.keySet()) {
                        String replaceKey = "{{" + key + "}}";
                        int i = stringBuilder.indexOf(replaceKey);
                        if (i > 0) {
                            stringBuilder.replace(i, i + replaceKey.length(), rowData.get(key).getName());
                        }
                    }
                    fileReader.close();
                    FileOutputStream fileOutputStream = new FileOutputStream(sharedStringsFile);
                    fileOutputStream.write(stringBuilder.toString().getBytes());
                    fileOutputStream.close();
                }
            }
        }
    }

    private Map<String, String> replaceSharedStrings(Map<String, CellData> cellDataMap) throws IOException {
        String sharedStrings = Constant.getSharedStringsPath(templatePath);
        File sharedStringsFile = new File(sharedStrings);
        if (sharedStringsFile.exists()) {
            StringBuilder stringBuilder = read(sharedStringsFile);
            int start, end = 0;
            Integer index = 0;
            Map<String, Integer> keyIndexMap = new HashMap<>();
            Map<String, String> result = new HashMap<>();
            while ((start = stringBuilder.indexOf(Constant.SHARED_STRINGS_SI_START_TAB, end)) > 0) {
                end = stringBuilder.indexOf(Constant.SHARED_STRINGS_SI_END_TAB, end) + 5;
                String keyContent = stringBuilder.substring(start, end);
                if (keyContent.contains("{{") && keyContent.contains("}}")) {
                    int keyStart = keyContent.indexOf("{{");
                    int keyEnd = keyContent.indexOf("}}");
                    keyIndexMap.put(keyContent.substring(keyStart, keyEnd), index);
                }
                index++;
            }
            for (String key : keyIndexMap.keySet()) {
                String replaceKey = "{{"+key+"}}";
                int i = stringBuilder.indexOf(replaceKey);
                if (i > 0) {
                    if (CellDataType.STRING.equals(cellDataMap.get(key).getCellDataType())) {
                        stringBuilder.replace(i, i +replaceKey.length(), cellDataMap.get(key).getName());
                    } else {
                        result.put("<v>"+keyIndexMap.get(key)+"<v/>", "<v>"+cellDataMap.get(key).getName()+"<v/>");
                    }
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(sharedStringsFile);
            fileOutputStream.write(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
            fileOutputStream.close();
            return result;
        } else {
            throw new UOfficeException("模板解析失败，不存在xl/sharedStrings.xml文件");
        }
    }

    private StringBuilder read(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        int read;
        char[] buffer = new char[1024];
        StringBuilder stringBuilder = new StringBuilder();
        while ((read = fileReader.read(buffer)) != -1) {
            stringBuilder.append(buffer, 0, read);
        }
        fileReader.close();
        return stringBuilder;
    }


}
