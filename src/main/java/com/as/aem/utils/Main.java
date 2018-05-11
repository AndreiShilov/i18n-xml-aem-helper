package com.as.aem.utils;

import java.util.Map;

import org.apache.log4j.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        LOGGER.debug("Starting app");

        Map<String, String> config = Utils.argsToMap(args);

        LOGGER.debug("Parsed config map = [" + config + "]"); //todo

        final App app = App.of(config);

        app.process();
    }
}
