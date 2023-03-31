package ir.ac.kntu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.reflect.*;

public class Main {

    public static void main(String[] args) {
        int line_count = 1;
        int public_class_arrived = 0;
        int public_class_main = 0;
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader("src/main/java/ir/ac/kntu/" + fileName));
            String line = reader.readLine();

            while (line != null) {
                check_var(line,line_count);
                int count = 0;
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == ';') {
                        count++;
                    }
                }
                if (count > 1) {
                    System.out.printf("semicolon error in line %d\n", line_count);
                }
                if (line.length() > 80) {
                    System.out.printf("line.length in line %d\n", line_count);
                }
                if (line.startsWith("package ") && line_count != 1) {
                    System.out.printf("package_error in line %d\n", line_count);
                }
                if (line.startsWith("import ") && public_class_arrived != 0) {
                    System.out.printf("import_error in line %d\n", line_count);
                }
                if (line.startsWith("public class ")) {
                    public_class_arrived = 1;
                    if (!line.matches("public class [A-Z][a-zA-Z].*")) {
                        System.out.printf("classname_error in line %d\n", line_count);
                    }
                }
                if (line.startsWith("    public static void main")) {
                    public_class_main = 1;
                }
                if (line.startsWith("    public static ")) {
                    check_met(line, line_count);
                }
                line = reader.readLine();
                line_count++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void check_met(String line, int line_N) {
        int openParenIndex = line.indexOf("(");
        int lastSpaceIndex = line.lastIndexOf(" ", openParenIndex);
        String methodName = line.substring(lastSpaceIndex + 1, openParenIndex);
        if (!methodName.matches("^[a-z]+([A-Z][a-z]*)*$")) {
            System.out.println("methoderror method:  " + methodName + " in Line: " + line_N);
        }
    }

    public static void check_var(String line, int line_N) {
        String pattern = "(\\b[a-zA-Z_]\\W*)\\s*=.*";

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(line);
        if(m.find())
        {
            String variableName = m.group(1);
            if(!variableName.matches("^[a-z]+([A-Z][a-z]*)*$"))
            {
                System.out.println("variable_error in line :" + line_N);
            }
        }
    }
}
