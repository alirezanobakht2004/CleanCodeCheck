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
        int koro_count = 0;
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

                if (line.contains("}")) {
                    koro_count--;
                }
                if (!line.trim().equals("") && !check_sp(line, koro_count)) {
                    System.out.println("Space_error in line: " + line_count);
                }
                if (line.contains("{")) {
                    koro_count++;
                }

                check_var(line.trim(), line_count);
                int count = 0;
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == ';') {
                        count++;
                    }
                }
                if (count > 1) {
                    System.out.printf("semicolon error in line %d\n", line_count);
                }
                if (line.trim().length() > 80) {
                    System.out.printf("line.length in line %d\n", line_count);
                }
                if (line.trim().startsWith("package ") && line_count != 1) {
                    System.out.printf("package_error in line %d\n", line_count);
                }
                if (line.trim().startsWith("import ") && public_class_arrived != 0) {
                    System.out.printf("import_error in line %d\n", line_count);
                }
                if (line.trim().startsWith("public class ")) {
                    public_class_arrived = 1;
                    if (!line.trim().matches("public class [A-Z][a-zA-Z].*")) {
                        System.out.printf("classname_error in line %d\n", line_count);
                    }
                }
                if (line.trim().startsWith("public static void main")) {
                    public_class_main = 1;
                }
                if (line.trim().startsWith("public static ")) {
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
        if (line.matches("int.*") || line.matches("float.*") || line.matches("double.*") || line.matches("String.*")
                || line.matches("boolean.*") || line.matches("char.*") || line.matches("long.*")
                || line.matches("byte.*")) {
            int endIndex = 1000;

            for (int i = line.indexOf(" ") + 1; i < line.length(); i++) {
                if (line.charAt(i) == ' ' || line.charAt(i) == ';' || line.charAt(i) == '=') {
                    endIndex = i;
                    break;
                }
            }
            String varName = line.substring(line.indexOf(" ") + 1, endIndex);
            if (!varName.matches("^[a-z]+([A-Z][a-z]*)*$")) {
                System.out.println("varerror : " + varName + " in Line: " + line_N);
            }
        }
    }

    public static boolean check_sp(String line, int num) {
        for (int h = 0; h < num * 4; h++) {
            if (line.charAt(h) != ' ') {
                return false;
            }
        }
        if (line.charAt(num * 4) == ' ') {
            return false;
        } else {
            return true;
        }
    }
}
