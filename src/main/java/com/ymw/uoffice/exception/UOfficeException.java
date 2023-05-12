package com.ymw.uoffice.exception;

import com.ymw.uoffice.utils.ReplaceUtils;

public class UOfficeException extends RuntimeException {

    public UOfficeException(String msg, Object... params) {
        super(ReplaceUtils.replaceWithArray("UOffice exception: " + msg, params));
    }
}
