package com.ymw.uoffice.utils;

import com.ymw.uoffice.domain.PathEntry;
import com.ymw.uoffice.exception.UOfficeException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PathUtils {
    public static void deleteAllDir(String path) {
        File tempFile = new File(path);
        String[] childPaths = tempFile.list();
        if (ObjectUtils.isNotEmpty(childPaths)) {
            for (String p : childPaths) {
                deleteAllDir(path+File.separator+p);
            }
        }
        if (!tempFile.delete()) {
            throw new UOfficeException("删除{}时出错", path);
        }
    }

    public static List<PathEntry> pathWalk(String path) {
        List<PathEntry> pathList = new ArrayList<PathEntry>();
        if (ObjectUtils.isNotEmpty(path)) {
            doWalk(path, path, pathList);
        }
        return pathList;
    }

    public static void createDir(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new UOfficeException("创建{}失败", file.getAbsolutePath());
            }
        }
    }

    private static void doWalk(String sourcePath, String targetPath, List<PathEntry> pathList) {
        File tempFile = new File(targetPath);
        if (tempFile.isDirectory()) {
            if (!targetPath.equals(sourcePath)) {
                pathList.add(new PathEntry(targetPath.replace(sourcePath, ""), true));
            }
            String[] childPaths = tempFile.list();
            if (!ObjectUtils.isEmpty(childPaths)) {
                for (String p : childPaths) {
                    String tempPath = targetPath+File.separator+p;
                    doWalk(sourcePath, tempPath, pathList);
                }
            }
        } else {
            pathList.add(new PathEntry(targetPath.replace(sourcePath, ""), false));
        }
    }
}
