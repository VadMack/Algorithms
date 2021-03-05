package com.ga;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileHandler {

  public static List<City> importFromFile(String path) {
    List<City> cities = new ArrayList<>();
    try {
      Scanner scanner = new Scanner(new File(path));
      String line;
      scanner.nextLine(); //skip first line

      while (scanner.hasNextLine()) {
        line = scanner.nextLine();
        String[] data = line.split(",");
        cities.add(new City(parseInt(data[0]), parseDouble(data[1]), parseDouble(data[2])));
      }

    } catch (FileNotFoundException e) {
      System.err.println("FileNotFoundException: " + e.getMessage());
    }
    return cities;
  }
}
