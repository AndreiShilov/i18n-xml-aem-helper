package com.as.aem.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class FileUtils {

    public static final FileUtils FILE_UTILS = new FileUtils();

    public File getFile(final String path) {
        return new File(path);
    }

    public Reader getFileReader(final File file) throws FileNotFoundException {
        return new FileReader(file);
    }
}
