package model;

import org.apache.flink.api.java.utils.ParameterTool;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Stream;

public class StaticData  {
    public static HashMap<String, String> LoadFromCSV(String pathStatic) {
        HashMap<String, String> map = new HashMap<>();
        try (Stream<Path> path = Files.walk(Paths.get(pathStatic))) {
            path.filter(Files::isRegularFile)
                    .forEach(file -> {
                        try (Stream<String> lines = Files.lines(file)) {
                            lines.map(line -> line.split(","))
                                    .filter(array -> array.length == 8)
                                    .filter(array -> !array[0].startsWith("Date"))
                                    .forEach(array -> {
                                        String stock = array[1];
                                        String StockFull = array[2];
                                        map.put(stock,StockFull);
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

}

