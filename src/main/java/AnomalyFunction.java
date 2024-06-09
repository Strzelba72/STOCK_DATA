import model.StockAnomaly;
import model.StockAnomalyAgg;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Instant;
import java.time.ZoneOffset;

public class AnomalyFunction extends ProcessWindowFunction<StockAnomalyAgg, StockAnomaly, String, TimeWindow> {
    @Override
    public void process(String stock, Context context, Iterable<StockAnomalyAgg> elements, Collector<StockAnomaly> out) {
        for (StockAnomalyAgg anomaly : elements) {
            float priceDifferenceRatio = (anomaly.getMax() - anomaly.getMin()) / anomaly.getMax();

            out.collect(new StockAnomaly(
                    Instant.ofEpochMilli(context.window().getStart()).atOffset(ZoneOffset.UTC).toLocalDateTime(),
                    Instant.ofEpochMilli(context.window().getEnd()).atOffset(ZoneOffset.UTC).toLocalDateTime(),
                    anomaly.getStock(),
                    anomaly.getMax(),
                    anomaly.getMin(),
                    priceDifferenceRatio
            ));
        }
    }
}
