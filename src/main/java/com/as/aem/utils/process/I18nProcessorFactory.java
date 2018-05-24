package com.as.aem.utils.process;

import java.util.Map;

public class I18nProcessorFactory {

    private I18nProcessorFactory() {
    }

    public static I18nProcessor getI18nProcessor(final String name, final Map<String, String> config) {
        final ProcessorType processorType = ProcessorType.fromString(name);

        I18nProcessor i18nProcessor = processorType.getLazyInitFunc().apply(config);

        i18nProcessor.init();

        return i18nProcessor;
    }
}
