package com.ymw.uoffice.service;

import com.ymw.uoffice.domain.PathEntry;
import com.ymw.uoffice.domain.XlsxData;
import com.ymw.uoffice.exception.UOfficeException;
import com.ymw.uoffice.utils.ObjectUtils;
import com.ymw.uoffice.utils.PathUtils;

import java.io.*;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public abstract class UOfficeTemplateNew {
    protected String templatePath;
    protected void initTemplate(InputStream inputStream) {
        String path = File.separator + UUID.randomUUID().toString();
        try (ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            ZipEntry nextEntry;
            while ((nextEntry = zipInputStream.getNextEntry()) != null) {
                if (nextEntry.isDirectory()) {
                    String tempPath = path + File.separator + nextEntry.getName();
                    PathUtils.createDir(tempPath);
                } else {
                    String tempPath = path + File.separator + nextEntry.getName();
                    File tempFile = new File(tempPath);
                    PathUtils.createDir(tempFile.getParent());
                    FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
                    byte[] buffer = new byte[1024];
                    int read = 0;
                    while ((read = zipInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, read);
                    }
                    fileOutputStream.close();
                }
                zipInputStream.closeEntry();
            }
            templatePath = path;
        } catch (Exception e) {
            e.printStackTrace();
            PathUtils.deleteAllDir(path);
        }
    }

    public abstract void generateToFile(XlsxData xlsxData, String filePath, boolean deleteTempFile) throws IOException;

    public abstract void generateToStream(XlsxData xlsxData, OutputStream outputStream, boolean deleteTempFile) throws IOException;


    protected void writeToStream(OutputStream outputStream) {
        List<PathEntry> pathEntries = PathUtils.pathWalk(templatePath);
        if (ObjectUtils.isNotEmpty(outputStream)) {
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
                for (PathEntry p : pathEntries) {
                    ZipEntry zipEntry = new ZipEntry(p.getName());
                    zipOutputStream.putNextEntry(zipEntry);
                    if (!p.isWhetherDir()) {
                        String s = templatePath + File.separator + p.getName();
                        FileInputStream fileInputStream = new FileInputStream(new File(s));
                        byte[] buffer = new byte[1024];
                        int read = 0;
                        while ((read = fileInputStream.read(buffer)) != -1) {
                            zipOutputStream.write(buffer, 0, read);
                        }
                        fileInputStream.close();
                    }
                    zipOutputStream.closeEntry();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void writeToFile(String fileName) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        writeToStream(byteArrayOutputStream);
        FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName));
        fileOutputStream.write(byteArrayOutputStream.toByteArray());
        fileOutputStream.close();
        byteArrayOutputStream.close();
    }


}
