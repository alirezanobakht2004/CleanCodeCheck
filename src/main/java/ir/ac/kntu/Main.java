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
        int koroCount = 0;
        int lineCount = 1;
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader("src/main/java/ir/ac/kntu/" + fileName));
            String line = reader.readLine();

            while (line != null) {

                if (line.contains("}")) {
                    koroCount--;
                }
                if (!line.trim().equals("") && !checkSp(line, koroCount)) {
                    System.out.println("khat_chini in line: " + lineCount);
                }
                if (line.contains("{")) {
                    koroCount++;
                }

                checkLoopIf(line, lineCount);
                checkVar(line.trim(), lineCount);
                checkSemi(line, lineCount);
                checkLength(line, lineCount);
                checkPackage(line, lineCount);
                checkImport(line, lineCount);
                checkClassName(line, lineCount);
                checkMet(line, lineCount);

                line = reader.readLine();
                lineCount++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkMet(String line, int lineN) {
        if (line.trim().startsWith("public static ")) {
            int openParenIndex = line.indexOf("(");
            int lastSpaceIndex = line.lastIndexOf(" ", openParenIndex);
            String methodName = line.substring(lastSpaceIndex + 1, openParenIndex);
            if (!methodName.matches("^[a-z]+([A-Z][a-z]*)*$")) {
                System.out.println("methoderror method:  " + methodName + " in Line: " + lineN);
            }
        }
    }

    public static void checkVar(String line, int lineN) {
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
                System.out.println("varerror : " + varName + " in Line: " + lineN);
            }
        }
    }

    public static boolean checkSp(String line, int num) {
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

    public static void checkLoopIf(String line, int lineN) {
        if (line.contains("while") || line.contains("for") || line.contains("if") || line.contains("else")) {
            if (!line.contains("{")) {
                System.out.println("khat_chini in line: " + lineN);
            } else if (line.matches(".*[{].*[;].*")) {
                System.out.println("khat_chini in line: " + lineN);
            }
        }

        if (line.contains("else")) {
            if (!line.contains("}")) {
                System.out.println("khat_chini in line: " + lineN);
            }
        }
    }

    public static void checkSemi(String line, int lineN) {
        int count = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ';') {
                count++;
            }
        }
        if (count > 1 && !line.contains("for")) {
            System.out.printf("semicolon error in line %d\n", lineN);
        }
    }

    public static void checkLength(String line, int lineN) {
        if (line.length() > 80) {
            System.out.printf("line.length in line %d\n", lineN);
        }
    }

    public static void checkPackage(String line, int lineN) {
        if (line.trim().startsWith("package ") && lineN != 1) {
            System.out.printf("package_error in line %d\n", lineN);
        }
    }

    public static void checkImport(String line, int lineN) {
        if (line.trim().startsWith("import ")) {
            System.out.printf("import_error in line %d\n", lineN);
        }
    }

    public static void checkClassName(String line, int lineN) {
        if (line.trim().startsWith("public class ")) {
            if (!line.trim().matches("public class [A-Z][a-zA-Z].*")) {
                System.out.printf("classname_error in line %d\n", lineN);
            }
        }
    }

}
