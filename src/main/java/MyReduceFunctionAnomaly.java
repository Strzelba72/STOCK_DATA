
import model.StockAnomalyAgg;

import org.apache.flink.api.common.functions.ReduceFunction;


public class MyReduceFunctionAnomaly implements ReduceFunction<StockAnomalyAgg> {
    @Override
    public StockAnomalyAgg reduce(StockAnomalyAgg stockdata1, StockAnomalyAgg stockdata2) throws Exception {
        return new StockAnomalyAgg(
                stockdata1.getStock(),
                stockdata1.getMax() > stockdata2.getMax() ?  stockdata1.getMax() : stockdata2.getMax(),
                stockdata1.getMin() < stockdata2.getMin() ? stockdata1.getMin() : stockdata2.getMin()
        );
    }
}
