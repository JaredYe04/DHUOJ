/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static Logger instance;
    private PrintWriter writer;
    private String logFileName;
    private static final String LOG_FILE_PREFIX = "log";
    private static final String LOG_FILE_SUFFIX = ".txt";
    private static final long HOUR_IN_MILLISECONDS = 3600000L; // 1 hour in milliseconds
    private static String Logdir = Config.getLogPath();
    private Logger() {
        createLogFile();
        startFileUpdateThread();
    }

    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    private void createLogFile() {
        LocalDateTime now = LocalDateTime.now();
        logFileName = Logdir+File.separator+LOG_FILE_PREFIX + "_" + now.format(DateTimeFormatter.ofPattern("yyyyMMddHH")) + LOG_FILE_SUFFIX;
        System.out.println("日志文件"+logFileName+"已创建！");
        try {
            writer = new PrintWriter(new FileWriter(logFileName, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startFileUpdateThread() {
        Thread fileUpdateThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(HOUR_IN_MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateLogFile();
            }
        });
        fileUpdateThread.setDaemon(true);
        fileUpdateThread.start();
    }

    private void updateLogFile() {
        writer.close(); // Close the current log file
        createLogFile(); // Create a new log file
    }

    public void log(String msg, LogLevel level) {
        String txt = getTimeStamp() + "[" + level.getString() + "]" + msg;
        System.out.println(txt);
        writer.println(txt);
        writer.flush();
    }
    private String getTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static void main(String[] args) {
        Logger logger = Logger.getInstance();
        for (int i = 0; i < 10; i++) {
            logger.log("Log message " + i, LogLevel.INFO);
            try {
                Thread.sleep(50); // Sleep for 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

