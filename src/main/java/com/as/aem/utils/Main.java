package com.as.aem.utils;

import org.apache.log4j.Logger;

import com.as.aem.utils.process.csv.CsvProcessor;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class);

    private CsvProcessor processor;

    public static void main(String[] args) {

        if (args.length < 2) {
            LOGGER.info("Not enough arguments, simple execution should be like:");
            LOGGER.info("java -jar utils.jar path/to/csv path/to/dictionaries/folder");
        }

        final String pathToCsvFile = args[0];
        final String pathToDictionariesFolder = args[1];

        App app = new App(pathToCsvFile, pathToDictionariesFolder);

        app.process();

    }
}
