package com.as.aem.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class Utils {

    private static final Logger LOGGER = Logger.getLogger(Utils.class);

    private Utils() {
        throw new IllegalStateException();
    }

    static Map<String, String> argsToMap(final String[] args) {

        final Map<String, String> config = Maps.newHashMap();

        for (String arg : args) {
            String[] split = arg.split("=");

            if (split.length == 1) {
                LOGGER.info("Passed argument does not have a value. Arg = [" + arg + "]");
            } else {
                final String key = split[0];

                Preconditions.checkArgument(!config.containsKey(key), "You have several values with the same key = [" + key + "]");

                if (split.length > 2) {
                    config.put(key, String.join("=", Arrays.copyOfRange(split, 1, split.length)));
                } else {
                    config.put(key, split[1]);
                }
            }

        }

        return Collections.unmodifiableMap(config);
    }
}
