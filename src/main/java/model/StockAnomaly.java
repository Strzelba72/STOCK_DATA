package model;

import java.time.LocalDateTime;

public class StockAnomaly {
    private LocalDateTime start;
    private LocalDateTime end;
    private String stock;
    private float highestPrice;
    private float lowestPrice;
    private float priceDifferenceRatio;

    public StockAnomaly(LocalDateTime start, LocalDateTime end, String stock, float highestPrice, float lowestPrice, float priceDifferenceRatio) {
        this.start = start;
        this.end = end;
        this.stock = stock;
        this.highestPrice = highestPrice;
        this.lowestPrice = lowestPrice;
        this.priceDifferenceRatio = priceDifferenceRatio;

    }

    public StockAnomaly() {
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public float getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(float highestPrice) {
        this.highestPrice = highestPrice;
    }

    public float getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(float lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public float getPriceDifferenceRatio() {
        return priceDifferenceRatio;
    }

    public void setPriceDifferenceRatio(float priceDifferenceRatio) {
        this.priceDifferenceRatio = priceDifferenceRatio;
    }

    @Override
    public String toString() {
        return "StockAnomaly{" +
                "start=" + start +
                ", end=" + end +
                ", stock='" + stock + '\'' +
                ", highestPrice=" + highestPrice +
                ", lowestPrice=" + lowestPrice +
                ", priceDifferenceRatio=" + priceDifferenceRatio +
                '}';
    }
}
