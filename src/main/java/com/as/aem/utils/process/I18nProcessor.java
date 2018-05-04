package com.as.aem.utils.process;

import java.util.Map;

import com.google.common.xml.XmlEscapers;

public interface I18nProcessor {

    /**
     * Return i18nData if format.
     *
     * language is a key -> Map as a value where -> key -> i18n key and value -> actual translation
     *
     * @return map representation of i18n data
     */
    Map<String, Map<String, String>> getI18nData();

    default String escape(final String str) {
        return XmlEscapers.xmlAttributeEscaper().escape(str);
    }
}
