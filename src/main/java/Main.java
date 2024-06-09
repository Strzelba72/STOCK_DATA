import connector.Connectors;
import model.*;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;
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
        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(
                "flights-in-us",
                new SimpleStringSchema(),
                properties.getProperties()
        );
        // Map with static data
        HashMap<String, String> map = StaticData.LoadFromCSV(properties.get("fileInput.uri.static"));
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        /*
        DataStream<String> inputStream = env.fromSource(
                Connectors.getFileSource(properties),
                WatermarkStrategy.noWatermarks(),
                "StockData"
        );
        */
        DataStream<String> inputStream = env.addSource(consumer);

        //Create  DataStream<StockData> based on input data
        DataStream<StockData> stockDataDS = inputStream
                .map((MapFunction<String, String[]>) txt -> txt.split(","))
                .filter(array -> array.length == 8)
                .filter(array ->!array[0].startsWith("Date"))
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
                .window(new MonthEventTrigger(properties.get("delay")))
                .reduce(new MyReduceFunction());


//            stockDataExtDS.print("StockDataAgg");



            //methods MyReduceFunctionAnomaly are
            DataStream<StockAnomaly> stockAnomalyDataStream = stockDataDS
                    .map(sd -> new StockAnomalyAgg(
                            sd.getStock(),
                            sd.getHigh(),
                            sd.getLow()
                    ))
                    .keyBy(StockAnomalyAgg::getStock)
                    .window(SlidingEventTimeWindows.of(Time.days(properties.getInt("D", 7)), Time.days(1)))
                    .reduce(new MyReduceFunctionAnomaly(),new AnomalyFunction())
                    .filter(stockAnomaly -> stockAnomaly.getPriceDifferenceRatio() > properties.getFloat("P", 0.4F))
                    .process(new ProcessFunction<StockAnomaly, StockAnomaly>() {
            @Override
            public void processElement(StockAnomaly stockAnomaly, Context context, Collector<StockAnomaly> collector) throws Exception {
                //.out.println("Aktualny znacznik wodny: " + context.timerService().currentWatermark());
                collector.collect(stockAnomaly);
            }});

//            stockAnomalyDataStream.print();
        stockDataExtDS.addSink(Connectors.getMySQLSink(properties));
        stockAnomalyDataStream.map((MapFunction<StockAnomaly, String>) Object::toString).sinkTo(KafkaSink.<String>builder()
               .setBootstrapServers(properties.get("bootstrap.servers"))
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                       .setTopic("stock-anomalies")
                       .setValueSerializationSchema(new SimpleStringSchema())
                      .build()
              ).setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
               .build());
        env.execute("Main");
    }
}
