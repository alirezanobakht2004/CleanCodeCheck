package ir.ac.kntu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.Element;
import javax.lang.model.util.ElementScanner14;

import java.lang.reflect.*;

public class Main {

    public static void main(String[] args) {
        int koroCount = 0;
        int lineCount = 1;
        String defCheck = "";
        String loopCheck = "";
        boolean swiCheck = false;
        int korocheck1 = 1;
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader("src/main/java/ir/ac/kntu/" + fileName));
            String line = reader.readLine();

            while (line != null) {

                koroCount += checkKoro(strCheck(line));
                koroCount += checkKoroOne(strCheck(line), defCheck);

                swiCheck = defExCheck(strCheck(line), swiCheck, koroCount, korocheck1, defCheck);
                defCheck = defCeck(strCheck(line), defCheck);
                loopCheck = loopCheck(strCheck(line), loopCheck);

                checkSpace(strCheck(line), koroCount, lineCount);
                koroCount += checkKoroTwo(strCheck(line));
                koroCount += checkKoroThree(strCheck(line), lineCount, loopCheck);
                defCheck = defCeck(strCheck(line), defCheck);
                checkCase(strCheck(line), lineCount);
                checkLoopIf(strCheck(line), lineCount);
                checkVar(line.trim(), lineCount);
                checkSemi(strCheck(line), lineCount);
                checkLength(line, lineCount);
                checkPackage(strCheck(line), lineCount);
                checkClassName(strCheck(line), lineCount);
                checkMet(strCheck(line), lineCount);

                line = reader.readLine();
                lineCount++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String strCheck(String line) {
        String regex = "\"[^\"]*\"";
        String placeholder = "###";
        String modkhat = line.replaceAll(regex, placeholder);
        return modkhat;
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
        if (line.contains("while") || line.contains("for") || line.contains("if") || line.contains("else")
                || line.contains("switch")) {
            if (!line.contains("{")) {
                System.out.println("khat_chini in line: " + lineN);
            } else if (line.matches(".*[{].*[:].*") || line.matches(".*[{].*[;].*")) {
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
        int count1 = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ';' && !line.trim().startsWith("for")) {
                count++;
                if (count > 0) {
                    for (int j = i + 1; j < line.length(); j++) {
                        if (line.charAt(j) != ' ') {
                            count1++;
                        }

                    }
                }
            }
        }
        if (count > 1 && !line.contains("for")) {
            System.out.printf("semicolon error in line %d\n", lineN);
        }
        if (count1 > 0) {
            System.out.printf("khat_chini error in line %d\n", lineN);
        }
    }

    public static void checkLength(String line, int lineN) {
        if (line.length() > 80) {
            System.out.printf("line.length in line %d\n", lineN);
            System.out.println(line.substring(0, line.indexOf(',', 80) + 1));
            System.out.println("                          " + line.substring(line.indexOf(',', 80) + 1, line.length()));
        }
    }

    public static void checkPackage(String line, int lineN) {
        if (line.trim().startsWith("package ") && lineN != 1) {
            System.out.printf("package_error in line %d\n", lineN);
        }
    }

    public static void checkClassName(String line, int lineN) {
        if (line.trim().startsWith("public class ")) {
            if (!line.trim().matches("public class [A-Z][a-zA-Z].*")) {
                System.out.printf("classname_error in line %d\n", lineN);
            }
        }
    }

    public static int checkKoro(String line) {
        if (line.contains("}")) {
            return -1;
        } else {
            return 0;
        }
    }

    public static int checkKoroTwo(String line) {
        if (line.contains("{")) {
            return +1;
        } else {
            return 0;
        }
    }

    public static void checkSpace(String line, int koroCount, int lineCount) {
        if (!line.trim().equals("") && !checkSp(line, koroCount)) {
            System.out.println("khat_chini in line: " + lineCount);
        }
    }

    public static void checkCase(String line, int lineN) {
        if (line.trim().startsWith("case") || line.trim().startsWith("default")) {
            if (line.contains(";")) {
                System.out.printf("case_or_default_error in line %d\n", lineN);
            }
        }
    }

    public static int checkKoroThree(String line, int lineCount, String loopCheck) {
        if (line.contains("break") && loopCheck.equals("")) {

            if (line.contains(":")) {
                System.out.printf("semicolon error in line %d\n", lineCount);
            }
            return -1;
        }
        if (line.contains("case")) {
            return 1;
        }
        if (line.contains("default") && !line.contains(";")) {
            return 1;
        } else {
            return 0;
        }
    }

    public static String defCeck(String line, String defCheck) {
        if (line.contains("}") && defCheck.trim().startsWith("default")) {
            return "";
        }
        if (line.contains("default") && !line.contains(";")) {
            return line;
        } else if (defCheck.contains("default")) {
            return defCheck;
        } else {
            return "";
        }
    }

    public static int checkKoroOne(String line, String defCheck) {
        if (line.contains("}") && defCheck.trim().startsWith("default")) {
            return -1;
        } else {
            return 0;
        }
    }

    public static String loopCheck(String line, String loopCheck) {
        if (line.contains("for") || line.contains("while")) {
            return "loop";
        } else if (loopCheck.equals("loop") && !line.contains("break")) {
            return "loop";
        } else if (loopCheck.equals("loop") && line.contains("break")) {
            return "loop_not";
        } else {

            return "";
        }
    }

    public static boolean defExCheck(String line, Boolean swiCheck, int koroCount, int korocheck1, String defExCheck) {
        if (line.contains("switch") && swiCheck == false) {
            korocheck1 = koroCount;
            return true;
        }
        if (line.contains("}") && swiCheck == true && defExCheck == "") {
            if (defExCheck == "") {
                System.out.println("hjj");
            }
            return false;
        }

        else if (swiCheck == true) {
            return true;
        } else {
            return false;
        }
    }
}
