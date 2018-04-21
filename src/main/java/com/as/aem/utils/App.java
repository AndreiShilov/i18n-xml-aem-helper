package com.as.aem.utils;

import static com.as.aem.utils.Constants.DICTIONARY_BASE_NAME;
import static com.as.aem.utils.Constants.PROCESSOR_TYPE;
import static com.as.aem.utils.Constants.XML.BASE_NAME_TEMPLATE;
import static com.as.aem.utils.Constants.XML.XML_FOLDER_PATH;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.as.aem.utils.process.I18nProcessor;
import com.as.aem.utils.process.I18nProcessorFactory;

public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class);

    private final I18nProcessor initialDictionaryProcessor;
    private final I18nProcessor dictionaryUpdatesProcessor;

    private final Map<String, String> config;
    private final String baseNameStr;

    public App(final Map<String, String> config) {
        this.config = config;
        initialDictionaryProcessor = I18nProcessorFactory.getI18nProcessor("xml", config);
        dictionaryUpdatesProcessor = I18nProcessorFactory.getI18nProcessor(config.get(PROCESSOR_TYPE), config);

        String baseName = config.get(DICTIONARY_BASE_NAME);

        if (baseName == null) {
            this.baseNameStr = "";
        } else {
            this.baseNameStr = String.format(BASE_NAME_TEMPLATE, baseName);
        }

    }

    //    TODO reduce complexity
    public void process() {

        Map<String, Map<String, String>> initialDictionary = initialDictionaryProcessor.getI18nData();
        Map<String, Map<String, String>> dictionaryUpdates = dictionaryUpdatesProcessor.getI18nData();

        for (Map.Entry<String, Map<String, String>> mapEntry : dictionaryUpdates.entrySet()) {
            String key = mapEntry.getKey();


            if (initialDictionary.containsKey(key)) {

                Map<String, String> stringStringMap = initialDictionary.get(key);

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
                initialDictionary.put(key, mapEntry.getValue());
            }
        }

        LOGGER.debug("Merging was finished");

        writeToFiles(config.get(XML_FOLDER_PATH), initialDictionary);

    }


    private void writeToFiles(final String folder, final Map<String, Map<String, String>> data) {
        for (Map.Entry<String, Map<String, String>> langMap : data.entrySet()) {
            writeToFile(folder, langMap);
        }
    }

    private void writeToFile(String folder, Map.Entry<String, Map<String, String>> langMap) {
        final StringBuilder stringBuilder = new StringBuilder(String.format(Constants.XML.FILE_HEADER, langMap.getKey(), baseNameStr));

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
