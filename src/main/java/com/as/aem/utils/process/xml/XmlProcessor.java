package com.as.aem.utils.process.xml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.as.aem.utils.Constants;
import com.as.aem.utils.FileUtils;
import com.as.aem.utils.process.I18nProcessor;
import com.google.common.base.Preconditions;

public class XmlProcessor implements I18nProcessor {

    private static final Logger LOGGER = Logger.getLogger(XmlProcessor.class);

    private static final Pattern FILE_NAME_PATTERN = Pattern.compile("^[a-zA-Z_\\-]+\\.xml$");

    private Map<String, Map<String, String>> langToKeyValueMap = new HashMap<>();

    private final String filePath;
    private final FileUtils fileUtils;

    public XmlProcessor(String pathToDictionariesFolder, FileUtils fileUtils) {

        Preconditions.checkNotNull(pathToDictionariesFolder, "You should pass path to the dictionaries folder '%s=folder/path'", Constants.XML.XML_FOLDER_PATH);

        this.fileUtils = fileUtils;
        this.filePath = pathToDictionariesFolder;
    }

    private void parseXmlAndAddDataToMap(File langFile, Map<String, Map<String, String>> langToKeyValueMap) {
        final String fileName = langFile.getName();
        LOGGER.debug("File name = [" + fileName + "]");

        final Map<String, String> map = new LinkedHashMap<>();
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

            lang = documentElement.getAttribute("jcr:language").toLowerCase().trim();

            if (!lang.equals(fileName.substring(0, fileName.lastIndexOf('.')))) {
                throw new IllegalStateException("File name language = [" + lang + "] and jcr:language = [" + fileName + "] are not equals"); // todo custom exception
            }

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
        } catch (ParserConfigurationException | IOException | SAXException e) {
            LOGGER.error(e.getMessage(),e);
        }

        if (lang != null) {
            langToKeyValueMap.put(lang, map);
        }
    }

    @Override
    public Map<String, Map<String, String>> getI18nData() {
        return langToKeyValueMap;
    }

    @Override
    public void init() {

        final File xmlLangsFolder = fileUtils.getFile(filePath);

        if (!xmlLangsFolder.isDirectory()) {
            LOGGER.warn("Path = [" + filePath + "] is not a directory. Stopping to process all");
            throw new IllegalArgumentException();
        }

        final File[] files = xmlLangsFolder.listFiles();

        if (files == null) {
            LOGGER.debug("No files under folder = [" + filePath + "]");
        } else {

            for (File langFile : files) {

                final String fileName = langFile.getName();

                if (FILE_NAME_PATTERN.matcher(fileName).matches()) {
                    parseXmlAndAddDataToMap(langFile, langToKeyValueMap);
                } else {
                    LOGGER.debug("File name = [" + fileName + "] does not match pattern = [" + FILE_NAME_PATTERN.pattern() + "]");
                }
            }
        }

    }
}
