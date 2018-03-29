package com.as.aem.utils;

public final class Constants {


    private Constants(){
    }

    public static final class XML {
        private XML(){}

        public static final String MESSAGE_NODE_TEMPLATE = "    <%s\n" +
                "            jcr:mixinTypes=\"[sling:Message]\"\n" +
                "            jcr:primaryType=\"nt:folder\"\n" +
                "            sling:message=\"%s\"/>\n";

        public static final String FILE_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<jcr:root xmlns:sling=\"http://sling.apache.org/jcr/sling/1.0\" xmlns:jcr=\"http://www.jcp.org/jcr/1.0\"\n" +
                "          xmlns:nt=\"http://www.jcp.org/jcr/nt/1.0\"\n" +
                "          jcr:language=\"de\"\n" +
                "          jcr:mixinTypes=\"[mix:language]\"\n" +
                "          jcr:primaryType=\"sling:Folder\">\n";

        public static final String FILE_FOOTER = "</jcr:root>";

    }

}
