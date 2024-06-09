package model;

import org.apache.flink.streaming.api.transformations.SideOutputTransformation;
import window.EventTrigger;

public class StockAnomalyAgg {
    private String stock;
    private float max;
    private float min;

    public StockAnomalyAgg(String stock, float max, float min) {
        this.stock = stock;
        this.max = max;
        this.min = min;

    }

    @Override
    public String toString() {
        return "StockAnomalyAgg{" +
                "stock='" + stock + '\'' +
                ", max=" + max +
                ", min=" + min +
                '}';
    }

    public StockAnomalyAgg() {
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }


}
