package com.ymw.uoffice.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : yangmingwei
 * @description : SharedString.xml对应的实体类
 * @since : 2023/4/10 11:23
 */
public class SharedStrings {
    private int count;
    private int uniqueCount;
    private Map<String, Integer> index;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getUniqueCount() {
        return uniqueCount;
    }

    public void setUniqueCount(int uniqueCount) {
        this.uniqueCount = uniqueCount;
    }

    public void addOne(String s) {
        if (s != null) {
            this.count += 1;
            if (this.index == null) {
                this.index = new HashMap<>();
            }
            if (this.index.get(s) == null) {
                this.index.put(s, uniqueCount);
                uniqueCount += 1;
            }
        }
    }

    public String getSstStart() {
        return "<sst xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\" count=\"" +
                count +
                "\" uniqueCount=\"" +
                uniqueCount +
                "\">";
    }

    public String[] getSi() {
        String[] strings = new String[uniqueCount];
        for (String key : index.keySet()) {
            strings[index.get(key)] = "<si><t>" + key + "</t></si>";
        }
        return strings;
    }

    public Map<String, Integer> getIndex() {
        return index;
    }
}
