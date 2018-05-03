package com.as.aem.utils;

public final class Constants {

    private Constants() {
    }

    public static final String PROCESSOR_TYPE = "processor.type";
    public static final String DICTIONARY_BASE_NAME = "dictionary.base.name";
    public static final String DICTIONARY_SORTING = "dictionary.sorting";

    public static final class XML {
        private XML() {
        }

        public static final String MESSAGE_NODE_TEMPLATE = "    <%s\n" +
                "            jcr:mixinTypes=\"[sling:Message]\"\n" +
                "            jcr:primaryType=\"nt:folder\"\n" +
                "            sling:message=\"%s\"/>\n";

        public static final String FILE_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<jcr:root xmlns:sling=\"http://sling.apache.org/jcr/sling/1.0\" xmlns:jcr=\"http://www.jcp.org/jcr/1.0\"\n" +
                "          xmlns:nt=\"http://www.jcp.org/jcr/nt/1.0\"\n" +
                "          jcr:language=\"%s\"\n" +
                "          jcr:mixinTypes=\"[mix:language]\"\n%s" +
                "          jcr:primaryType=\"sling:Folder\">\n";

        public static final String BASE_NAME_TEMPLATE = "          sling:basename=\"%s\"\n";

        public static final String FILE_FOOTER = "</jcr:root>";

        public static final String XML_FOLDER_PATH = "xml.folder.path";
    }

    public static final class CSV {
        private CSV() {
        }

        public static  final String CSV_FILE_PATH = "csv.file";
    }

    public static final class HTML {
        private HTML() {
        }

        public static  final String HTML_URL = "html.url";
        public static  final String HTML_LOGIN = "html.login";
        public static  final String HTML_PASSWORD = "html.password";
    }

}
