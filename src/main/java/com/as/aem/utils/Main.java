package com.as.aem.utils;

import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.collect.Maps;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        final App app = new App(argsToMap(args));

        app.process();
    }


    private static Map<String, String> argsToMap(final String[] args) {

        final Map<String, String> config = Maps.newHashMap();

        for (String arg : args) {
            String[] split = arg.split("=");
            if (split.length == 1) {
                LOGGER.info("Passed argument does not have a value. Arg = [" + arg + "]");
            } else {
                // todo html might contain '=' in query

                if (split.length > 2) {
                    config.put(split[0], split[1] + "=" + split[2]);
                } else {
                    config.put(split[0], split[1]);
                }
            }

        }

        return Collections.unmodifiableMap(config);
    }
}
