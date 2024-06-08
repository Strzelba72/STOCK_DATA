import connector.Connectors;
import model.StaticData;
import model.StockAnnomalie;
import model.StockData;
import model.StockDataAgg;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import window.EventTrigger;
import window.MonthEventTrigger;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception {

        ParameterTool propertiesFromFile = ParameterTool.fromPropertiesFile("src/main/resources/flink.properties");
        ParameterTool propertiesFromArgs = ParameterTool.fromArgs(args);
        ParameterTool properties = propertiesFromFile.mergeWith(propertiesFromArgs);

        // Map with static data
        HashMap<String, String> map = StaticData.LoadFromCSV();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<String> inputStream = env.fromSource(
                Connectors.getFileSource(properties),
                WatermarkStrategy.noWatermarks(),
                "StockData"
        );
        //Create  DataStream<StockData> based on input data
        DataStream<StockData> stockDataDS = inputStream
                .map((MapFunction<String, String[]>) txt -> txt.split(","))
                .filter(array -> array.length == 8)
                .map(array -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                    LocalDateTime date = array[0].isEmpty() ? LocalDateTime.now() : ZonedDateTime.parse(array[0], formatter).toLocalDateTime();
                    float open = array[1].isEmpty() ? 0.0f : Float.parseFloat(array[1]);
                    float high = array[2].isEmpty() ? 0.0f : Float.parseFloat(array[2]);
                    float low = array[3].isEmpty() ? 0.0f : Float.parseFloat(array[3]);
                    float close = array[4].isEmpty() ? 0.0f : Float.parseFloat(array[4]);
                    float adjClose = array[5].isEmpty() ? 0.0f : Float.parseFloat(array[5]);
                    float volume = array[6].isEmpty() ? 0.0f : Float.parseFloat(array[6]);
                    String stock = array[7];
                    return new StockData(date, open, high, low, close, adjClose, volume, stock);
                })
                .map(new MapFunction<StockData, StockData>() {
                    @Override
                    public StockData map(StockData value) throws Exception {
                        //System.out.println("Timestamp: " + value.getDate());
                        return value;
                    }
                })
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                .<StockData>forBoundedOutOfOrderness(Duration.ofDays(1))
                                .withTimestampAssigner((event, timestamp) -> event.getDate().toEpochSecond(ZoneOffset.UTC) * 1000))
                .process(new ProcessFunction<StockData, StockData>() {
                @Override
                    public void processElement(StockData value, Context ctx, Collector<StockData> out) throws Exception {
                        long watermark = ctx.timerService().currentWatermark();
                        //System.out.println("Current watermark: " + watermark);
                        out.collect(value);
            }
        });

        //stockDataDS.print("StockData");



        DataStream<StockDataAgg> stockDataExtDS = stockDataDS
                .map(sd -> new StockDataAgg(
                        sd.getStock(),
                        sd.getVolume(),
                        sd.getLow(),
                        sd.getHigh(),
                        sd.getClose(),
                        map.get(sd.getStock()),
                        1,
                        sd.getClose(),
                        sd.getDate().getMonthValue()
                ))
                .keyBy(StockDataAgg::getStock)
                .window(new MonthEventTrigger("A"))
                .reduce(new MyReduceFunction());


            stockDataExtDS.print("StockDataAgg");
          /*
            DataStream<StockAnnomalie> stockDataAnnomalie = stockDataDS
                    .map(sd -> new StockAnnomalie(

                    ))
        */
        env.execute("Main");
    }
}
