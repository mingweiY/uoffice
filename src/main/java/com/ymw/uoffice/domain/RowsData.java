package com.ymw.uoffice.domain;

import com.ymw.uoffice.constant.Constant;
import com.ymw.uoffice.utils.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author : yangmingwei
 * @description : 行数据集合
 * @since : 2023/4/13 15:02
 */
public class RowsData extends ArrayList<RowData> {
    private String[] groupColumn = null;
    private String[] sumColumn = null;
    private Map<String, Map<String, BigDecimal>> sumMap = null;
    private String firstKey = null;

    public RowsData(String[] groupColumn, String[] sumColumn, String... firstKey) {
        this.groupColumn = groupColumn;
        this.sumColumn = sumColumn;
        if (ObjectUtils.isNotEmpty(this.sumColumn)) {
            if (ObjectUtils.isNotEmpty(this.groupColumn)) {
                this.sumMap = new HashMap<>();
                this.firstKey = groupColumn[0];
                for (String s : this.groupColumn) {
                    Map<String, BigDecimal> tempMap = new HashMap<>();
                    for (String ss : this.sumColumn) {
                        tempMap.put(ss, BigDecimal.ZERO);
                    }
                    this.sumMap.put(s, tempMap);
                }
                Map<String, BigDecimal> tempMap = new HashMap<>();
                for (String ss : this.sumColumn) {
                    tempMap.put(ss, BigDecimal.ZERO);
                }
                this.sumMap.put(Constant.OFFICE_SUM_KEY, tempMap);
            } else if (ObjectUtils.isNotEmpty(firstKey)) {
                this.sumMap = new HashMap<>();
                this.firstKey = firstKey[0];
                Map<String, BigDecimal> tempMap = new HashMap<>();
                for (String ss : this.sumColumn) {
                    tempMap.put(ss, BigDecimal.ZERO);
                }
                this.sumMap.put(Constant.OFFICE_SUM_KEY, tempMap);
            }
        }
    }

    public RowsData() {
    }

    @Override
    public boolean add(RowData t) {
        if (this.sumMap != null) {
            Map<String, CellData> nextColumnData = t.getRowData();
            Map<String, BigDecimal> sum = this.sumMap.get(Constant.OFFICE_SUM_KEY);
            Map<String, BigDecimal> tempMap = new HashMap<>();
            for (String ss : this.sumColumn) {
                BigDecimal add = sum.get(ss).add(new BigDecimal(nextColumnData.get(ss).getName()));
                tempMap.put(ss, add);
            }
            this.sumMap.put(Constant.OFFICE_SUM_KEY, tempMap);
            if (ObjectUtils.isNotEmpty(this.groupColumn)) {
                if (super.size() > 0) {
                    RowData preRowData = super.get(super.size() - 1);
                    Map<String, CellData> columnDataMap = preRowData.getRowData();
                    for (int length = 0; length < this.groupColumn.length; length++) {
                        String name = columnDataMap.get(this.groupColumn[length]).getName();
                        String nextName = nextColumnData.get(this.groupColumn[length]).getName();
                        if (!name.equals(nextName)) {
                            for (int j = this.groupColumn.length - 1; j >= length; j--) {
                                RowData rowData = new RowData();
                                for (int i = 0; i <= j; i++) {
                                    CellData cellData = columnDataMap.get(this.groupColumn[i]);
                                    if (j == i) {
                                        rowData.put(this.groupColumn[i], cellData.getName() + " 合计：", cellData.getCellDataType());
                                    } else {
                                        rowData.put(this.groupColumn[i], cellData);
                                    }
                                }
                                tempMap = new HashMap<>();
                                for (String ss : this.sumColumn) {
                                    rowData.put(ss, this.sumMap.get(this.groupColumn[j]).get(ss));
                                    tempMap.put(ss, BigDecimal.ZERO);
                                }
                                this.sumMap.put(this.groupColumn[j], tempMap);
                                super.add(rowData);
                            }
                            break;
                        }
                    }
                }
                for (String s : this.groupColumn) {
                    tempMap = this.sumMap.get(s);
                    for (String ss : this.sumColumn) {
                        BigDecimal add = tempMap.get(ss).add(new BigDecimal(nextColumnData.get(ss).getName()));
                        tempMap.put(ss, add);
                    }
                    this.sumMap.put(s, tempMap);
                }
            }
        }
        return super.add(t);
    }

    public void generateEndSumInfo() {
        if (this.sumMap != null) {
            if (ObjectUtils.isNotEmpty(this.groupColumn)) {
                if (super.size() > 0) {
                    RowData preRowData = super.get(super.size() - 1);
                    Map<String, CellData> columnDataMap = preRowData.getRowData();
                    for (int j = this.groupColumn.length - 1; j >= 0; j--) {
                        RowData rowData = new RowData();
                        for (int i = 0; i <= j; i++) {
                            CellData cellData = columnDataMap.get(this.groupColumn[i]);
                            if (j == i) {
                                String name = cellData.getName() + " 合计：";
                                rowData.put(this.groupColumn[i], name, cellData.getCellDataType());
                            } else {
                                rowData.put(this.groupColumn[i], cellData);
                            }
                        }
                        Map<String, BigDecimal> map = this.sumMap.get(this.groupColumn[j]);
                        for (String key : map.keySet()) {
                            rowData.put(key, map.get(key));
                        }
                        super.add(rowData);
                    }
                }
            }
            RowData rowData = new RowData();
            rowData.put(this.firstKey, "总计：", CellDataType.STRING);
            Map<String, BigDecimal> map = this.sumMap.get(Constant.OFFICE_SUM_KEY);
            for (String key : map.keySet()) {
                rowData.put(key, map.get(key));
            }
            super.add(rowData);
            this.sumMap = null;
        }
    }
}
