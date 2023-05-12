package com.ymw.uoffice.service.doc;

import com.ymw.uoffice.domain.DocHeadBlock;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnalysisDocFile {

    public void analysis(String file) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        DocHeadBlock docHeadBlock = analysisHeadBlock(randomAccessFile);
        byte[][] allBlock = getAllBlock(randomAccessFile, docHeadBlock);
        randomAccessFile.close();
        analysisBlock(docHeadBlock, allBlock);
    }

    private byte[][] getAllBlock(RandomAccessFile randomAccessFile, DocHeadBlock docHeadBlock) throws IOException {
        long fileSize = randomAccessFile.length();
        int blockNumber = (int) ((fileSize - 512) / docHeadBlock.getBlockSize());
        byte[][] allBlock = new byte[blockNumber][docHeadBlock.getBlockSize()];
        int index = 1;
        int read;
        while (true) {
            randomAccessFile.seek(512 + index * docHeadBlock.getBlockSize());
            byte[] block = new byte[docHeadBlock.getBlockSize()];
            read = randomAccessFile.read(block);
            if (read <= 0) {
                break;
            }
            allBlock[index] = block;
            index++;
        }
        return allBlock;
    }

    private DocHeadBlock analysisHeadBlock(RandomAccessFile randomAccessFile) throws IOException {
        DocHeadBlock docHeadBlock = new DocHeadBlock();
        byte[] block = new byte[512];
        int read = randomAccessFile.read(block);
        docHeadBlock.setBlockSize(block[0x1E]);
        docHeadBlock.setMiniBlockSize(block[0X20]);
        docHeadBlock.setDirIndex(toInt(block, 0x30));
        docHeadBlock.setFatNumber(toInt(block, 0x2C));
        docHeadBlock.setMiniFatIndex(toInt(block, 0x3C));
        int[] fats = new int[docHeadBlock.getFatNumber()];
        for (int i = 0; i < docHeadBlock.getFatNumber(); i++) {
            fats[i] = toInt(block, 0x4c + i * 4);
        }
        docHeadBlock.setFatIndex(fats);
        return docHeadBlock;
    }

    private void analysisBlock(DocHeadBlock docHeadBlock, byte[][] allBlock) throws IOException {
        int[] blockRelax = analysisFatBlock(docHeadBlock, allBlock);
        analysisDirBlock(docHeadBlock, allBlock, blockRelax);



    }

    private void analysisDirBlock(DocHeadBlock docHeadBlock, byte[][] allBlock, int[] blockRelax) {
        int dirIndex = docHeadBlock.getDirIndex();
        List<Integer> dirIndexS = new ArrayList<>();
        dirIndexS.add(dirIndex);
        getDirBlock(blockRelax, dirIndex, dirIndexS);
        for (Integer index : dirIndexS) {
            byte[] bytes = allBlock[index];
            int docNumber = docHeadBlock.getBlockSize() / 128;
            for (int i = 0; i < docNumber; i++) {
                byte[] tempByte = new byte[128];
                System.arraycopy(bytes, i * 128, tempByte, 0, 128);
                analysisOneDirBlock(tempByte);
            }
        }
    }

    private void analysisOneDirBlock(byte[] block) {
        byte dirNameSize = block[64];
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < dirNameSize; i+=2) {
            stringBuilder.append(toChar(block, i));
        }
        System.out.println(stringBuilder.toString());
    }

    private void getDirBlock(int[] blockRelax, int dirIndex, List<Integer> dirIndexS) {
        if (blockRelax[dirIndex] > 0) {
            dirIndexS.add(blockRelax[dirIndex]);
            getDirBlock(blockRelax, blockRelax[dirIndex], dirIndexS);
        }
    }

    private int[] analysisFatBlock(DocHeadBlock docHeadBlock, byte[][] allBlock) throws IOException {
        int blockSize = docHeadBlock.getBlockSize();
        int fatNumber = docHeadBlock.getFatNumber();
        int intNumber = blockSize / 4;
        int[] blockRelax =  new int[fatNumber * intNumber];
        int[] fatIndex = docHeadBlock.getFatIndex();
        for (int i = 0; i < fatNumber; i++) {
            byte[] bytes = allBlock[fatIndex[i]];
            for (int j = 0; j < bytes.length; j+=4) {
                blockRelax[i * intNumber + j / 4] = toInt(bytes, j);
            }
        }
        return blockRelax;
    }



    private int toInt(byte[] buffer, int i) {
        int b1 = buffer[i++] & 0xFF;
        int b2 = buffer[i++] & 0xFF;
        int b3 = buffer[i++] & 0xFF;
        int b4 = buffer[i] & 0xFF;
        return (b4 << 24) + (b3 << 16) + (b2 << 8) + b1;
    }

    private char toChar(byte[] buffer, int i) {
        int b1 = buffer[i++] & 0xFF;
        int b2 = buffer[i] & 0xFF;
        return (char) ((b2 << 8) + b1);
    }

}
