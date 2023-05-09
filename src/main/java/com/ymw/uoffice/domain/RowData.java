package com.ymw.uoffice.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : yangmingwei
 * @description : 每一行的行数据
 * @since : 2023/4/11 14:07
 */
public class RowData {
    // Map的key为码值与TableHead中的code对应
    private final Map<String, CellData> rowData;

    public RowData() {
        this.rowData = new HashMap<>();
    }

    public void put(String key, String value) {
        rowData.put(key, new CellData(value, CellDataType.STRING));
    }

    public void put(String key, Integer value) {
        if (null != value) {
            rowData.put(key, new CellData(String.valueOf(value), CellDataType.NUMBER));
        } else {
            rowData.put(key, new CellData("", CellDataType.NUMBER));
        }
    }

    public void put(String key, BigDecimal value) {
        if (null != value) {
            rowData.put(key, new CellData(value.toString(), CellDataType.NUMBER));
        } else {
            rowData.put(key, new CellData(BigDecimal.ZERO.toString(), CellDataType.NUMBER));
        }
    }

    public void put(String key, CellData value) {
        rowData.put(key, new CellData(value.getName(), value.getCellDataType()));
    }

    public void put(String key, String name, CellDataType cellDataType) {
        rowData.put(key, new CellData(name, cellDataType));
    }

    public Map<String, CellData> getRowData() {
        return rowData;
    }

}
