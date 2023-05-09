package com.ymw.uoffice.domain;

import com.ymw.uoffice.utils.ObjectUtils;

/**
 * @author : yangmingwei
 * @description : 单元格数据
 * @since : 2023/4/11 13:50
 */
public class CellData {
    // 单元格数据
    private String name;
    // 数据类型
    private CellDataType cellDataType;

    public CellData(String name, CellDataType cellDataType) {
        this.name = replaceErrorChar(name);
        this.cellDataType = cellDataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CellDataType getCellDataType() {
        return cellDataType;
    }

    public void setCellDataType(CellDataType cellDataType) {
        this.cellDataType = cellDataType;
    }

    private String replaceErrorChar(String source) {
        if (ObjectUtils.isEmpty(source)) {
            return source;
        }
        return source.replaceAll("[\\p{C}\\p{So}\uFE00-\uFE0F\\x{E0100}-\\x{E01EF}]", "")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
    }
}
