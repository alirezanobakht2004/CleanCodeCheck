package ir.ac.kntu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int line_count=1;
        int public_class_arrived=0;
        int public_class_main=0;
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/main/java/ir/ac/kntu/" + fileName));
            String line = reader.readLine();

            while (line != null) {
                if(line.length()>80)
                {
                    System.out.printf("line.length in line %d\n",line_count);
                }
                if(line.startsWith("package ") && line_count!=1)
                {
                     System.out.printf("package_error in line %d\n",line_count);
                }
                if(line.startsWith("import ") && public_class_arrived!=0)
                {
                    System.out.printf("import_error in line %d\n",line_count);
                }
                if(line.startsWith("public class "))
                {
                    public_class_arrived=1;
                    if(!line.matches("public class ^[A-Z][a-zA-Z0-9]*$"))
                    {
                        System.out.printf("classname_error in line %d\n",line_count);
                    }
                }
                if(line.startsWith("    public static void main"))
                {
                    public_class_main=1;
                }
                if(public_class_main==1)
                {
                    int count=0;
                    for(int i=0;i<line.length();i++)
                    {
                        if(line.charAt(i)==';')
                        {
                            count++;
                        }
                    }
                    if(count>1)
                    {
                        System.out.printf("semi-column error in line %d\n",line_count);
                    }
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
