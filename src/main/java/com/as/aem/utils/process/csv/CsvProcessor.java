package com.as.aem.utils.process.csv;


import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.log4j.Logger;

import com.as.aem.utils.FileUtils;
import com.as.aem.utils.process.I18nProcessor;
import com.google.common.base.Preconditions;

/**
 * CSV contract
 * key, lang1, lang2, lang3||no
 *
 * ||no means that we do not want to import this lang for no
 */
public class CsvProcessor implements I18nProcessor {

    private static final Logger LOGGER = Logger.getLogger(CsvProcessor.class);

    private List<String> languagesToImport;
    private Map<String, Map<String, String>> langKeValueMapping;

    private final String csvPath;
    private final FileUtils fileUtils;

    public CsvProcessor(String pathToCsvFile, FileUtils fileUtils) {
        Preconditions.checkNotNull(pathToCsvFile, "Path to scv file is required when you use csv processor");
        this.csvPath = pathToCsvFile;
        this.fileUtils = fileUtils;
    }

    private static List<String> getLanguagesToImport(Map<String, Integer> headerMap) {
        return headerMap.keySet()
                .stream()
                .filter(CsvProcessor::isLanguageAllowedToBeImported)
                .collect(toList());
    }

    private static boolean isLanguageAllowedToBeImported(final String key) {
        if ("key".equalsIgnoreCase(key)) {
            return false;
        }

        String[] split = key.split("\\|\\|");

        return split.length == 1 || !"no".equalsIgnoreCase(split[1]);
    }

    @Override
    public Map<String, Map<String, String>> getI18nData() {
        return langKeValueMapping;
    }

    @Override
    public void init() {
        File csvFile = fileUtils.getFile(this.csvPath);

        if (!csvFile.isFile()) {
            LOGGER.warn("Path = [" + this.csvPath + "] is not a file. Stopping to process all");
            throw new IllegalArgumentException();
        }

        try {
            Reader in = fileUtils.getFileReader(csvFile);

            CSVParser parse = CSVFormat.EXCEL.withHeader().parse(in);

            Map<String, Integer> headerMap = parse.getHeaderMap();

            languagesToImport = getLanguagesToImport(headerMap);

            LOGGER.info(String.format("Languages to be imported from csv = [%s]", String.join(", ", languagesToImport)));

            langKeValueMapping = new HashMap<>(languagesToImport.size());

            for (String lang : languagesToImport) {
                langKeValueMapping.put(lang, new LinkedHashMap<>());
            }

            parse.getRecords().forEach(record -> {
                for (String lang : languagesToImport) {
                    langKeValueMapping.get(lang).put(record.get("key"), escape(record.get(lang)));
                }
            });

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        if (languagesToImport == null || languagesToImport.isEmpty()) {
            langKeValueMapping = Collections.emptyMap();
        }

    }
}
