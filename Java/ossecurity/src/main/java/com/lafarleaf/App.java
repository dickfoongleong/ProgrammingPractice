package com.lafarleaf;

import java.io.File;

public class App {
    public static void main(String[] args) {
        OSScanner scanner = new OSScanner();

        File fileToScan = null;
        if (args.length == 0) {
            fileToScan = new File(System.getProperty("user.home"));
        } else {
            fileToScan = new File(args[0]);
        }

        if (fileToScan != null) {
            scanner.start(fileToScan);
        }

    }
}
