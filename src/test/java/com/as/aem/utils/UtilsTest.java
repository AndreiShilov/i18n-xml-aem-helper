package com.as.aem.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UtilsTest {

    @Test
    @DisplayName("Test CSV process with base name")
    public void parseArgsTest1() {
        final String[] args = ("processor.type=csv csv.file=<PATH_TO_PROJECT>/i18ndictionaryhelper/example/example.csv " +
                "xml.folder.path=<PATH_TO_PROJECT>/i18ndictionaryhelper/example dictionary.base.name=dictionaryBaseName").split(" ");

        Map<String, String> stringStringMap = Utils.argsToMap(args);

        assertEquals(4, stringStringMap.size());
        assertEquals("csv", stringStringMap.get(Constants.PROCESSOR_TYPE));
        assertEquals("<PATH_TO_PROJECT>/i18ndictionaryhelper/example", stringStringMap.get(Constants.XML.XML_FOLDER_PATH));
        assertEquals("<PATH_TO_PROJECT>/i18ndictionaryhelper/example/example.csv", stringStringMap.get(Constants.CSV.CSV_FILE_PATH));
        assertEquals("dictionaryBaseName", stringStringMap.get(Constants.DICTIONARY_BASE_NAME));
    }

    @Test
    @DisplayName("Test CSV process without base name")
    public void parseArgsTest2() {
        final String[] args = ("processor.type=csv csv.file=<PATH_TO_PROJECT>/i18ndictionaryhelper/example/example.csv " +
                "xml.folder.path=<PATH_TO_PROJECT>/i18ndictionaryhelper/example").split(" ");

        Map<String, String> stringStringMap = Utils.argsToMap(args);

        assertEquals(3, stringStringMap.size());
        assertEquals("csv", stringStringMap.get(Constants.PROCESSOR_TYPE));
        assertEquals("<PATH_TO_PROJECT>/i18ndictionaryhelper/example", stringStringMap.get(Constants.XML.XML_FOLDER_PATH));
        assertEquals("<PATH_TO_PROJECT>/i18ndictionaryhelper/example/example.csv", stringStringMap.get(Constants.CSV.CSV_FILE_PATH));
        assertNull(stringStringMap.get(Constants.DICTIONARY_BASE_NAME));
    }

    @Test
    @DisplayName("Test HTML process without credentials")
    public void parseArgsTest3() {
        final String[] args = ("processor.type=html " +
                "xml.folder.path=<PATH_TO_PROJECT>/i18ndictionaryhelper/example " +
                "html.url=https://github.com/AndreiShilov/i18n-xml-aem-helper/blob/master/example/ExampleTable.md").split(" ");

        Map<String, String> stringStringMap = Utils.argsToMap(args);

        assertEquals(3, stringStringMap.size());
        assertEquals("html", stringStringMap.get(Constants.PROCESSOR_TYPE));
        assertEquals("<PATH_TO_PROJECT>/i18ndictionaryhelper/example", stringStringMap.get(Constants.XML.XML_FOLDER_PATH));
        assertEquals("https://github.com/AndreiShilov/i18n-xml-aem-helper/blob/master/example/ExampleTable.md", stringStringMap.get(Constants.HTML.HTML_URL));
        assertNull(stringStringMap.get(Constants.HTML.HTML_LOGIN));
        assertNull(stringStringMap.get(Constants.HTML.HTML_PASSWORD));
    }

    @Test
    @DisplayName("Test HTML process with credentials")
    public void parseArgsTest4() {
        final String[] args = ("processor.type=html " +
                "xml.folder.path=<PATH_TO_PROJECT>/i18ndictionaryhelper/example " +
                "html.url=https://github.com/AndreiShilov/i18n-xml-aem-helper/blob/master/example/ExampleTable.md " +
                "html.login=myLogin html.password=myPassword").split(" ");

        Map<String, String> stringStringMap = Utils.argsToMap(args);

        assertEquals(5, stringStringMap.size());
        assertEquals("html", stringStringMap.get(Constants.PROCESSOR_TYPE));
        assertEquals("<PATH_TO_PROJECT>/i18ndictionaryhelper/example", stringStringMap.get(Constants.XML.XML_FOLDER_PATH));
        assertEquals("https://github.com/AndreiShilov/i18n-xml-aem-helper/blob/master/example/ExampleTable.md", stringStringMap.get(Constants.HTML.HTML_URL));
        assertEquals("myLogin", stringStringMap.get(Constants.HTML.HTML_LOGIN));
        assertEquals("myPassword", stringStringMap.get(Constants.HTML.HTML_PASSWORD));
    }

    @Test
    @DisplayName("Test HTML process with credentials and url contains '='")
    public void parseArgsTest5() {
        final String[] args = ("processor.type=html " +
                "xml.folder.path=<PATH_TO_PROJECT>/i18ndictionaryhelper/example " +
                "html.url=https://github.com/?param=param1&param=param2 " +
                "html.login=myLogin html.password=myPassword").split(" ");

        Map<String, String> stringStringMap = Utils.argsToMap(args);

        assertEquals(5, stringStringMap.size());
        assertEquals("html", stringStringMap.get(Constants.PROCESSOR_TYPE));
        assertEquals("<PATH_TO_PROJECT>/i18ndictionaryhelper/example", stringStringMap.get(Constants.XML.XML_FOLDER_PATH));
        assertEquals("https://github.com/?param=param1&param=param2", stringStringMap.get(Constants.HTML.HTML_URL));
        assertEquals("myLogin", stringStringMap.get(Constants.HTML.HTML_LOGIN));
        assertEquals("myPassword", stringStringMap.get(Constants.HTML.HTML_PASSWORD));
    }

    @Test
    @DisplayName("Several values with the same key")
    public void parseArgsTest6() {
        final String[] args = ("processor.type=html processor.type=csv").split(" ");

        assertThrows(IllegalArgumentException.class, () -> Utils.argsToMap(args));
    }
}