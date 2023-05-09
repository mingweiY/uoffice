package com.ymw.uoffice.domain;

/**
 * @author : yangmingwei
 * @description :
 * @since : 2023/4/7 17:57
 */
public enum CellDataType {
    STRING("1"),
    NUMBER("2");
    private String type;
    CellDataType(String type) {
        this.type = type;
    }
}
