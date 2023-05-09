package com.ymw.uoffice.utils;

import com.sun.istack.internal.Nullable;

import java.lang.reflect.Array;
import java.util.*;

public class ObjectUtils {
    public static boolean isEmpty(@Nullable Object o) {
        if (o == null) {
            return true;
        } else if (o instanceof Optional) {
            return !((Optional) o).isPresent();
        } else if (o instanceof Collection) {
            return ((Collection) o).isEmpty();
        } else if (o.getClass().isArray()) {
            return Array.getLength(o) == 0;
        } else if (o instanceof CharSequence) {
            return ((CharSequence) o).length() == 0;
        } else {
            return o instanceof Map && ((Map) o).isEmpty();
        }

    }

    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }
}
