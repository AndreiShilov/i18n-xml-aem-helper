package com.as.aem.utils.process.xml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlProcessor {

    private static final Logger LOGGER = Logger.getLogger(XmlProcessor.class);

    private static final Pattern FILE_NAME_PATTERN = Pattern.compile("^[a-z]+\\.xml$");

    private Map<String, Map<String, String>> langToKeyValueMap= new HashMap<>();

    public XmlProcessor(String pathToDictionariesFolder) {

        final File xmlLangsFolder = new File(pathToDictionariesFolder);

        if (!xmlLangsFolder.isDirectory()) {
            LOGGER.warn("Path = [" + pathToDictionariesFolder + "] is not a directory. Stopping to process all");
            throw new IllegalArgumentException();
        }

        final File[] files = xmlLangsFolder.listFiles();

        if (files == null) {
            LOGGER.debug("No files under folder = [" + pathToDictionariesFolder + "]");
        } else {

            for (File langFile : files) {

                final String fileName = langFile.getName();

                if (FILE_NAME_PATTERN.matcher(fileName).matches()) {
                    parseXmlAndAddDataToMap(langFile, langToKeyValueMap);
                } else {
                    LOGGER.debug("File name = [" + fileName + "] does not match pattern = []");
                }
            }
        }


    }

    public Map<String, Map<String, String>> getLangToKeyValueMap() {
        return langToKeyValueMap;
    }

    private void parseXmlAndAddDataToMap(File langFile, Map<String, Map<String, String>> langToKeyValueMap) {
        LOGGER.debug("File name = [" + langFile.getName() + "]");

        final Map<String, String> map = new HashMap<>();
        String lang = null;

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(langFile);
            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            Element documentElement = doc.getDocumentElement();

            documentElement.normalize();

            lang = documentElement.getAttribute("jcr:language");

            NodeList childNodes = documentElement.getChildNodes();

            for (int i = 0; i < childNodes.getLength(); i++) {
                Node item = childNodes.item(i);

                short nodeType = item.getNodeType();

                if (nodeType == Node.ELEMENT_NODE) {
                    Element element = (Element) item;
                    map.put(item.getNodeName(), element.getAttribute("sling:message"));

                    LOGGER.debug(String.format("Key = [%s] and value = [%s] were added", item.getNodeName(), element.getAttribute("sling:message")));
                }

            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lang != null) {
            langToKeyValueMap.put(lang, map);
        }
    }
}
