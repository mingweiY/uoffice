package com.ymw.uoffice.domain;

/**
 * @author : yangmingwei
 * @description : 标头的单元格
 * @since : 2023/4/12 14:05
 */
public class TableHead {
    // 表头的名称
    private String name;
    // 表头的码值，用于映射RowData中的key
    private String code;
    // 确定每一列的宽度
    private String length;


    public TableHead(String name) {
        this.name = name;
    }

    public TableHead(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public TableHead(String name, String code, String length) {
        this.name = name;
        this.code = code;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
