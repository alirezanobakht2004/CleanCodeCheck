package ir.ac.kntu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int line_count=0;
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/main/java/ir/ac/kntu/" + fileName));
            String line = reader.readLine();

            while (line != null) {
                
                if(line.startsWith("package ") && line_count!=0)
                {
                     System.out.printf("package_error in line %d",line_count);
                }
                
                
                line = reader.readLine();
                line_count++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
