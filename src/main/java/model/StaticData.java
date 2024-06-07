package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class StaticData {
    public static HashMap<String, String> LoadFromCSV() {
        HashMap<String, String> map = new HashMap<>();
        try (Scanner scanner = new Scanner(new File("symbols_valid_meta.csv"))) {
            // Read the header line
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Read the rest of the file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",", -1);

                // Handle quoted values that may contain commas
                String symbol = values[1];
                String securityName = values[2];
                if (securityName.startsWith("\"")) {
                    securityName = securityName.substring(1, securityName.length() - 1);
                }

                map.put(symbol, securityName);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }


}

