package com.as.aem.utils.process.csv;


import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.log4j.Logger;

import com.google.common.xml.XmlEscapers;

/**
 * CSV contract
 * key, lang1, lang2, lang3||no
 *
 * ||no means that we do not want to import this lang for no
 */
public class CsvProcessor {

    private static final Logger LOGGER = Logger.getLogger(CsvProcessor.class);


    private List<String> languagesToImport;
    private Map<String, Map<String, String>> langKeValueMapping;

    public CsvProcessor(String pathToCsvFile) {

        File csvFile = new File(pathToCsvFile);

        if (!csvFile.isFile()) {
            LOGGER.warn("Path = [" + pathToCsvFile + "] is not a file. Stopping to process all");
            throw new IllegalArgumentException();
        }

        try {
            Reader in = new FileReader(pathToCsvFile);

            CSVParser parse = CSVFormat.EXCEL.withHeader().parse(in);

            Map<String, Integer> headerMap = parse.getHeaderMap();

            languagesToImport = getLanguagesToImport(headerMap);

            LOGGER.info(String.format("Languages to be imported from csv = [%s]", String.join(", ", languagesToImport)));

            langKeValueMapping = new HashMap<>(languagesToImport.size());

            for (String lang : languagesToImport) {
                langKeValueMapping.put(lang, new HashMap<>());
            }

            StreamSupport.stream(parse.getRecords().spliterator(), false).forEach(record -> {
                for (String lang : languagesToImport) {
                    langKeValueMapping.get(lang).put(record.get("key"), escape(record.get(lang)));
                }
            });

        } catch (IOException e) {
            LOGGER.error("Exception", e);
        }

        if (languagesToImport == null || languagesToImport.isEmpty()) {
            languagesToImport = Collections.emptyList();
        }

    }

    public Map<String, Map<String, String>> getLangKeValueMapping() {
        return langKeValueMapping;
    }


    private String escape(final String string) {
        return XmlEscapers.xmlContentEscaper().escape(string);
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


//    Option with XML DOM, but in this case it is hard to manage indent
//    private void xmlProcessor(Map.Entry<String, Map<String, String>> langMap) throws ParserConfigurationException, TransformerException {
//        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//
//        // root elements
//        Document doc = docBuilder.newDocument();
//        Element rootElement = doc.createElement("jcr:root");
//
//        rootElement.setAttribute("xmlns:sling", "http://sling.apache.org/jcr/sling/1.0");
//        rootElement.setAttribute("xmlns:jcr", "http://www.jcp.org/jcr/1.0");
//        rootElement.setAttribute("xmlns:nt", "http://www.jcp.org/jcr/nt/1.0");
//
//        rootElement.setAttribute("jcr:language", langMap.getKey().toLowerCase());
//        rootElement.setAttribute("jcr:mixinTypes", "[mix:language]");
//        rootElement.setAttribute("jcr:primaryType", "sling:Folder");
//
//        doc.appendChild(rootElement);
//
//        langMap.getValue()
//                .entrySet()
//                .stream()
//                .sorted(Comparator.comparing(Map.Entry::getKey))
//                .forEach(entry -> {
//                    Element element = doc.createElement(entry.getKey());
//                    element.setAttribute("jcr:mixinTypes", "[sling:Message]");
//                    element.setAttribute("jcr:primaryType", "nt:folder");
//                    element.setAttribute("sling:message", entry.getValue());
//                    rootElement.appendChild(element);
//                });
//
//
//        // write the content into xml file
//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        Transformer transformer = transformerFactory.newTransformer();
//        DOMSource source = new DOMSource(doc);
//        StreamResult result = new StreamResult(new File("/Users/andreishilov/git/i18ndictionaryhelper/test" + langMap.getKey().toLowerCase() + ".xml"));
//
//        transformer.transform(source, result);
//    }

}
