package com.as.aem.utils.process;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.as.aem.utils.Constants;
import com.as.aem.utils.process.csv.CsvProcessor;
import com.as.aem.utils.process.html.HtmlProcessor;
import com.as.aem.utils.process.xml.XmlProcessor;
import com.google.common.collect.ImmutableMap;

class ProcessorTypeTest {

    @Test
    @DisplayName("Validate Xml Processor")
    public void test1() {
        ProcessorType processorType = ProcessorType.fromString("xml");

        I18nProcessor processor = processorType.getLazyInitFunc().apply(ImmutableMap.of(Constants.XML.XML_FOLDER_PATH, "/fake/path"));

        assertTrue(processor instanceof XmlProcessor);
    }

    @Test
    @DisplayName("Validate Csv Processor")
    public void test2() {
        ProcessorType processorType = ProcessorType.fromString("csv");

        I18nProcessor processor = processorType.getLazyInitFunc().apply(ImmutableMap.of(Constants.CSV.CSV_FILE_PATH, "/fake/path"));

        assertTrue(processor instanceof CsvProcessor);
    }

    @Test
    @DisplayName("Validate Html Processor")
    public void test3() {
        ProcessorType processorType = ProcessorType.fromString("html");

        I18nProcessor processor = processorType.getLazyInitFunc().apply(ImmutableMap.of(Constants.HTML.HTML_URL, "/fake/path"));

        assertTrue(processor instanceof HtmlProcessor);
    }

    @Test
    @DisplayName("Validate non existing Processor")
    public void test4() {
        assertThrows(IllegalStateException.class, () -> ProcessorType.fromString("magic"));
    }

}