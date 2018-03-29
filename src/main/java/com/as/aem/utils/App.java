package com.as.aem.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.as.aem.utils.process.csv.CsvProcessor;
import com.as.aem.utils.process.xml.XmlProcessor;

public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class);

    private final CsvProcessor csvProcessor;
    private final XmlProcessor xmlProcessor;

    private final String pathToDictionariesFolder;

    public App(String pathToCsvFile, String pathToDictionariesFolder) {
        xmlProcessor = new XmlProcessor(pathToDictionariesFolder);
        csvProcessor = new CsvProcessor(pathToCsvFile);
        this.pathToDictionariesFolder = pathToDictionariesFolder;
    }

    //    TODO reduce complexity
    public void process() {

        Map<String, Map<String, String>> xmlProcessorLangToKeyValueMap = xmlProcessor.getLangToKeyValueMap();
        Map<String, Map<String, String>> csvProcessorLangKeValueMapping = csvProcessor.getLangKeValueMapping();


        for (Map.Entry<String, Map<String, String>> mapEntry : csvProcessorLangKeValueMapping.entrySet()) {
            String key = mapEntry.getKey();


            if (xmlProcessorLangToKeyValueMap.containsKey(key)) {

                Map<String, String> stringStringMap = xmlProcessorLangToKeyValueMap.get(key);

                for (Map.Entry<String, String> entry : mapEntry.getValue().entrySet()) {
                    String dictionaryKey = entry.getKey();

                    if (stringStringMap.containsKey(dictionaryKey)) {
                        String previousValue = stringStringMap.put(dictionaryKey, entry.getValue());

                        if (entry.getValue().equals(previousValue)) {
                            LOGGER.debug(String.format("Same value was already at the dictionary. Dictionary = [%s], key = [%s], value = [%s]",
                                    key, dictionaryKey, entry.getValue()));
                        } else {
                            LOGGER.info(String.format("Value was updated, Dictionary = [%s], key = [%s], from = [%s] to [%s]",
                                    key, dictionaryKey, previousValue, entry.getValue()));
                        }

                    } else {
                        stringStringMap.put(dictionaryKey, entry.getValue());
                    }

                }

            } else {
                LOGGER.info("Seems to be a new language found in csv. Language key = [" + key + "]");
                xmlProcessorLangToKeyValueMap.put(key, mapEntry.getValue());
            }
        }

        LOGGER.debug("Merging was finished");

        writeToFiles(pathToDictionariesFolder, xmlProcessorLangToKeyValueMap);

    }


    private void writeToFiles(final String folder, final Map<String, Map<String, String>> data) {
        for (Map.Entry<String, Map<String, String>> langMap : data.entrySet()) {
            writeToFile(folder, langMap);
        }
    }

    private void writeToFile(String folder, Map.Entry<String, Map<String, String>> langMap) {
        final StringBuilder stringBuilder = new StringBuilder(String.format(Constants.XML.FILE_HEADER, langMap.getKey()));

        langMap.getValue()
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .forEach(entry -> stringBuilder.append(String.format(Constants.XML.MESSAGE_NODE_TEMPLATE, entry.getKey(), entry.getValue())));

//        closing tag for jcr:root
        stringBuilder.append(Constants.XML.FILE_FOOTER);

        try {
            Files.write(Paths.get(folder + "/" + langMap.getKey().toLowerCase() + ".xml"), stringBuilder.toString().getBytes());
        } catch (IOException e) {
//                TODO
        }
    }
}
