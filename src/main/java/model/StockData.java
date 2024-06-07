package model;


import java.time.LocalDateTime;

public class StockData {
    private LocalDateTime date;
    private float open;
    private float high;
    private float low;
    private float close;
    private float adjClose;
    private float volume;
    private String stock;


    public StockData(LocalDateTime date, float open, float high, float low, float close, float adjClose, float volume, String stock) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.adjClose = adjClose;
        this.volume = volume;
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Stock Data{ "+stock+" "+date+" "+open+" "+high+" "+low+" "+close+" "+adjClose+" "+volume+"}";

    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public float getAdjClose() {
        return adjClose;
    }

    public void setAdjClose(float adjClose) {
        this.adjClose = adjClose;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }




}
