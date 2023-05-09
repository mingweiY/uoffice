package com.ymw.uoffice.domain;

import java.io.File;

public class PathEntry {
    private final String name;

    private final boolean whetherDir;

    public PathEntry(String name, boolean whetherDir) {
        this.whetherDir = whetherDir;
        if (this.whetherDir) {
            name = name + File.separator;
        }
        this.name = name.replace("\\", "/").substring(1);
    }


    public String getName() {
        return name;
    }

    public boolean isWhetherDir() {
        return whetherDir;
    }
}
