package project;

import java.io.*;
import java.util.*;

public class file {
    public static void main(String[] args) {
        // Specify the path to your current items.txt file
        String inputFile = "items.txt";
        String outputFile = "sorted_items.txt";
        
        List<String> books = new ArrayList<>();
        
        // Read the current items.txt and store each line in a list
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                books.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Sort the books by title (assuming the title is the 4th element in the data, based on your format)
        books.sort((line1, line2) -> {
            String title1 = line1.split("\\|")[3].trim();
            String title2 = line2.split("\\|")[3].trim();
            return title1.compareToIgnoreCase(title2);  // Case-insensitive comparison
        });
        
        // Write the sorted data to a new file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String book : books) {
                writer.write(book);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("Books sorted successfully into " + outputFile);
    }
}