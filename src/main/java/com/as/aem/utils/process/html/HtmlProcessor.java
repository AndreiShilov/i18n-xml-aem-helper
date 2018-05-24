package com.as.aem.utils.process.html;


import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.as.aem.utils.process.I18nProcessor;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class HtmlProcessor implements I18nProcessor {

    private Map<String, Map<String, String>> langKeValueMapping;

    private final String encodedCredentials;
    private final String url;

    public HtmlProcessor(String url, String login, String password) {

        Preconditions.checkNotNull(url, "Url is required for html processor");

        final String auth = login + ":" + password;
        this.encodedCredentials = new String(Base64.getEncoder().encode(auth.getBytes()));
        this.url = url;
    }

    void processTable(Element table) {

        final Map<Integer, String> languagesToImport = Maps.newHashMap();

        final Elements tableHeader = table.getElementsByTag("th");

        tableHeader.iterator().forEachRemaining(tableHeaderElement -> {
            String tableHeaderText = tableHeaderElement.text();

            // todo better recognition of Language name
            if (tableHeaderText.matches("[A-Z_-].+")) {
                languagesToImport.put(tableHeaderElement.siblingIndex(), tableHeaderText.toLowerCase().trim());
            }
        });

        langKeValueMapping = new HashMap<>(languagesToImport.size());

        languagesToImport.values().forEach(language -> langKeValueMapping.put(language, new LinkedHashMap<>()));

        final Elements tableBody = table.getElementsByTag("tbody");

        tableBody.get(0).getElementsByTag("tr").iterator().forEachRemaining(tableRow -> {
            final String key = CharMatcher.anyOf("\r\n\t \u00A0").trimFrom(tableRow.child(0).text());// should be key mandatory first element of any row

            tableRow.children().iterator().forEachRemaining(rowCell -> {
                int siblingIndex = rowCell.siblingIndex();

                if (languagesToImport.containsKey(siblingIndex)) {
                    langKeValueMapping.get(languagesToImport.get(siblingIndex)).put(key, escape(rowCell.text()).trim());
                }

            });
        });
    }

    @Override
    public Map<String, Map<String, String>> getI18nData() {
        return langKeValueMapping;
    }

    @Override
    public void init() {
        try {
            Document document = Jsoup.connect(url).header("Authorization", "Basic " + this.encodedCredentials).get();
            Elements table = document.getElementsByTag("table");

            table.iterator().forEachRemaining(this::processTable);
        } catch (IOException e) {
            throw new IllegalStateException("Can not get html from " + url);
        }
    }
}



