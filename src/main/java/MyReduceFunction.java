import model.StockDataAgg;
import org.apache.flink.api.common.functions.ReduceFunction;

public class MyReduceFunction implements ReduceFunction<StockDataAgg> {
    @Override
    public StockDataAgg reduce(StockDataAgg sd1, StockDataAgg sd2) throws Exception {
        return new StockDataAgg(
                sd1.getStock(),
                sd1.getVolumeSum() + sd2.getVolumeSum(),
                sd1.getMinLow() < sd2.getMinLow() ?  sd1.getMinLow() :  sd2.getMinLow(),
                sd1.getMaxHigh() >  sd2.getMaxHigh() ? sd1.getMaxHigh() : sd2.getMaxHigh(),
                (sd1.getSumClose() + sd2.getSumClose())/(sd1.getCount()+sd2.getCount()),
                sd1.getStockFull(),
                sd1.getCount()+sd2.getCount(),
                sd1.getSumClose() + sd2.getSumClose(),
                sd1.getMonth()
        );
    }
}
