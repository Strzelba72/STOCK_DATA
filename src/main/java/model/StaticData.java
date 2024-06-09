package model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

public class StaticData  {
    public static HashMap<String, String> LoadFromCSV(String pathStatic) {
        HashMap<String, String> map = new HashMap<>();
        try (Stream<Path> path = Files.walk(Paths.get(pathStatic))) {
            path.filter(Files::isRegularFile)
                    .forEach(file -> {
                        try (Stream<String> lines = Files.lines(file)) {
                            lines.skip(1) // Skip header
                                    .map(line -> line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")) // Split CSV correctly handling quotes
                                    .forEach(array -> {
                                        if (array.length > 2) {
                                            String stock = array[1].trim();
                                            String stockFull = array[2].trim();
                                            map.put(stock, stockFull);
                                        }
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

