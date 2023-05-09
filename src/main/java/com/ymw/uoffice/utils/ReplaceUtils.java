package com.ymw.uoffice.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplaceUtils {
    private static final Pattern pattern = Pattern.compile("\\{(.*?)}");

    public static String replaceWithArray(String source, Object[] params) {
        if (ObjectUtils.isEmpty(source) || ObjectUtils.isEmpty(params)) {
            return source;
        }
        Matcher matcher = pattern.matcher(source);
        int index = 0;
        while (matcher.find() && index < params.length) {
            source = source.replaceFirst("\\{}", params[index].toString());
            index++;
        }
        return source;
    }
}
