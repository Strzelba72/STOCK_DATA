package model;

public class StockDataAgg {

    private String stock;
    private float volumeSum;
    private float minLow;
    private float maxHigh;
    private float avgClose;

    private String stockFull;
    private int count;
    private float sumClose;

    public StockDataAgg(String stock, float volumeSum, float minLow, float maxHigh, float avgClose,String stockFull, int count, float sumClose) {
        this.stock = stock;
        this.volumeSum = volumeSum;
        this.minLow = minLow;
        this.maxHigh = maxHigh;
        this.avgClose = avgClose;
        this.stockFull = stockFull;
        this.sumClose = sumClose;
        this.count = count;
    }

    @Override
    public String toString() {
        return "Stock data aggregate {"+stock+" "+volumeSum+" "+minLow+" "+maxHigh+" "+avgClose+" "+stockFull+" "+sumClose+" "+count+"}";
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getStockFull() {
        return stockFull;
    }

    public void setStockFull(String stockFull) {
        this.stockFull = stockFull;
    }

    public float getVolumeSum() {
        return volumeSum;
    }

    public void setVolumeSum(float volumeSum) {
        this.volumeSum = volumeSum;
    }

    public float getMinLow() {
        return minLow;
    }

    public void setMinLow(float minLow) {
        this.minLow = minLow;
    }

    public float getMaxHigh() {
        return maxHigh;
    }

    public void setMaxHigh(float maxHigh) {
        this.maxHigh = maxHigh;
    }

    public float getAvgClose() {
        return avgClose;
    }

    public void setAvgClose(float avgClose) {
        this.avgClose = avgClose;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getSumClose() {
        return sumClose;
    }

    public void setSumClose(float sumClose) {
        this.sumClose = sumClose;
    }
}

