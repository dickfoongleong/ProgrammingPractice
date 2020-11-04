package com.lafarleaf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OSScanner {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
    private static final SimpleDateFormat DATE_ONLY_FORMAT = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private static final File EXCLUDED_FILE = new File("output/excluded.log");
    private static final File INCLUDED_FILE = new File("output/included.log");

    private static Date time;
    private static FileWriter incWriter;
    private static FileWriter excWriter;

    public void start(final File file) {
        try {
            time = Calendar.getInstance().getTime();

            excWriter = new FileWriter(EXCLUDED_FILE, true);
            excWriter.write(DATE_FORMAT.format(time) + "\n");

            incWriter = new FileWriter(INCLUDED_FILE, true);
            incWriter.write(DATE_FORMAT.format(time) + "\n");

            scan(file);

            excWriter.close();
            incWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scan(File file) throws IOException {
        try {
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    scan(f);
                }
            } else if (file.isFile()) {
                printDate(file);
            }
        } catch (Exception e) {
            excWriter.write(file.getAbsolutePath() + "\n");
        }
    }

    private void printDate(File file) throws Exception {
        String outFormat = "%s - %s";

        BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        long date = attrs.lastAccessTime().toMillis();

        if (DATE_ONLY_FORMAT.format(date).equals(DATE_ONLY_FORMAT.format(time))) {
            String output = String.format(outFormat, DATE_FORMAT.format(date), file.getAbsolutePath());
            incWriter.write(output + "\n");
        }
    }
}
