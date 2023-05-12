package com.ymw.uoffice.domain;

public class DocHeadBlock {
    private int blockSize;
    private int miniBlockSize;
    private int fatNumber;
    private int dirIndex;
    private int miniFatIndex;
    private int[] fatIndex;


    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = (int) Math.pow(2, blockSize);
    }

    public int getMiniBlockSize() {
        return miniBlockSize;
    }

    public void setMiniBlockSize(int miniBlockSize) {
        this.miniBlockSize = (int) Math.pow(2, miniBlockSize);
    }

    public int getFatNumber() {
        return fatNumber;
    }

    public void setFatNumber(int fatNumber) {
        this.fatNumber = fatNumber;
    }

    public int getDirIndex() {
        return dirIndex;
    }

    public void setDirIndex(int dirIndex) {
        this.dirIndex = dirIndex;
    }

    public int getMiniFatIndex() {
        return miniFatIndex;
    }

    public void setMiniFatIndex(int miniFatIndex) {
        this.miniFatIndex = miniFatIndex;
    }

    public int[] getFatIndex() {
        return fatIndex;
    }

    public void setFatIndex(int[] fatIndex) {
        this.fatIndex = fatIndex;
    }
}
