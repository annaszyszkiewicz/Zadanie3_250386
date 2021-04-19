package edu.ib;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FASTA {
    public static String read(File filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(filePath);
        scanner.useDelimiter("\\Z");
        String string = scanner.next();
        String[] lines = string.split("\n");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < lines.length; i++) {
            stringBuilder.append(lines[i]);
        }
        return stringBuilder.toString();
    }
}
