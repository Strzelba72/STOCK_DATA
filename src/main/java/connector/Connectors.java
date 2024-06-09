package connector;
import model.StockDataAgg;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.PreparedStatement;
import java.sql.SQLException;


import java.time.Duration;
public class Connectors {
    public static SinkFunction<StockDataAgg> getMySQLSink(ParameterTool properties) {
        JdbcStatementBuilder<StockDataAgg> statementBuilder =
                new JdbcStatementBuilder<StockDataAgg>() {
                    @Override
                    public void accept(PreparedStatement ps, StockDataAgg data) throws SQLException {
                        ps.setString(1, data.getStock());
                        ps.setString(2, data.getStockFull());
                        ps.setFloat(3, data.getMinLow());
                        ps.setFloat(4, data.getMaxHigh());
                        ps.setFloat(5, data.getAvgClose());
                        ps.setFloat(6,data.getVolumeSum());
                        ps.setInt(7,data.getMonth());
                    }
                };
        JdbcConnectionOptions connectionOptions = new
                JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                .withUrl(properties.getRequired("mysql.url"))
                .withDriverName("com.mysql.jdbc.Driver")
                .withUsername(properties.getRequired("mysql.username"))
                .withPassword(properties.getRequired("mysql.password"))
                .build();
        JdbcExecutionOptions executionOptions = JdbcExecutionOptions.builder()
                .withBatchSize(100)
                .withBatchIntervalMs(200)
                .withMaxRetries(5)
                .build();
        SinkFunction<StockDataAgg> jdbcSink =
                JdbcSink.sink("insert into stock_data" +
                                "(stock, full_stock, " +
                                "min_low, max_high, avg_close, volume_sum, month) \n" +
                                "values (?, ?, ?, ?, ?, ?, ?)",
                        statementBuilder,
                        executionOptions,
                        connectionOptions);
        return jdbcSink;
    }

}