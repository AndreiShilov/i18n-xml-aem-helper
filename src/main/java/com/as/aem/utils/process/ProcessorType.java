package com.as.aem.utils.process;

import static com.as.aem.utils.Constants.CSV.CSV_FILE_PATH;
import static com.as.aem.utils.Constants.HTML.HTML_LOGIN;
import static com.as.aem.utils.Constants.HTML.HTML_PASSWORD;
import static com.as.aem.utils.Constants.HTML.HTML_URL;
import static com.as.aem.utils.Constants.XML.XML_FOLDER_PATH;
import static com.as.aem.utils.FileUtils.FILE_UTILS;

import java.util.Map;
import java.util.function.Function;

import com.as.aem.utils.Constants;
import com.as.aem.utils.process.csv.CsvProcessor;
import com.as.aem.utils.process.html.HtmlProcessor;
import com.as.aem.utils.process.xml.XmlProcessor;

public enum ProcessorType {
    XML(Constants.XML.TYPE_NAME, map -> new XmlProcessor(map.get(XML_FOLDER_PATH), FILE_UTILS)),
    CSV(Constants.CSV.TYPE_NAME, map -> new CsvProcessor(map.get(CSV_FILE_PATH),FILE_UTILS)),
    HTML(Constants.HTML.TYPE_NAME, map -> new HtmlProcessor(map.get(HTML_URL), map.get(HTML_LOGIN), map.get(HTML_PASSWORD)));

    private String strName;
    private Function<Map<String, String>, I18nProcessor> lazyInitFunc;

    ProcessorType(String strName, Function<Map<String, String>, I18nProcessor> lazyInitFunc) {
        this.strName = strName;
        this.lazyInitFunc = lazyInitFunc;
    }

    public String getStrName() {
        return strName;
    }

    public Function<Map<String, String>, I18nProcessor> getLazyInitFunc() {
        return lazyInitFunc;
    }

    public static ProcessorType fromString(final String strName) {
        ProcessorType[] values = ProcessorType.values();

        String cleanedStrName = strName.toLowerCase().trim();

        for (ProcessorType processorType : values) {
            if (processorType.getStrName().equals(cleanedStrName)) {
                return processorType;
            }
        }
        throw new IllegalStateException("Could not find processor for name = " + strName);
    }
}
