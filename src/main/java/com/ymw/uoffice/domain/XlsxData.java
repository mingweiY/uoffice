package com.ymw.uoffice.domain;

import com.ymw.uoffice.utils.ObjectUtils;

import java.util.List;

/**
 * @author : yangmingwei
 * @description :
 * @since : 2023/4/7 17:36
 */
public class XlsxData {
    private String title;
    private List<TableHead> tableHead;
    private RowsData rowsData;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTableHead(List<TableHead> tableHead) {
        this.tableHead = tableHead;
    }

    public boolean check() {
        return ObjectUtils.isEmpty(this.title) && ObjectUtils.isEmpty(this.tableHead) && ObjectUtils.isEmpty(this.rowsData);
    }

    public int getMaxWidth() {
        int width = 0;
        if (!ObjectUtils.isEmpty(this.tableHead)) {
            width = this.tableHead.size();
        } else if (!ObjectUtils.isEmpty(this.rowsData)) {
            width = this.rowsData.get(0).getRowData().size();
        }
        return width;
    }

    public int getMaxHeight() {
        int height = 1;
        if (!ObjectUtils.isEmpty(this.tableHead)) {
            height++;
        }
        if (!ObjectUtils.isEmpty(this.rowsData)) {
            height += this.rowsData.size();
        }
        return height;
    }

    public String getTitle() {
        return title;
    }

    public List<TableHead> getTableHead() {
        return tableHead;
    }

    public RowsData getRowsData() {
        return rowsData;
    }

    public void setRowsData(RowsData rowsData) {
        this.rowsData = rowsData;
    }
}
