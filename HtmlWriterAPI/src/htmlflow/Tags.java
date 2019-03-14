package htmlflow;

import java.io.PrintStream;

/**
 */
class Tags {
    private static final char BEGIN_TAG = '<';
    private static final String BEGIN_CLOSE_TAG = "</";
    private static final String BEGIN_COMMENT_TAG = "<!-- ";
    private static final String END_COMMENT_TAG = " -->";
    private static final String ATTRIBUTE_MID = "=\"";
    static final char FINISH_TAG = '>';
    private static final char SPACE = ' ';
    private static final char QUOTATION = '"';

    private Tags() { }

    static void printOpenTag(PrintStream out, String elementName) {
        out.print(BEGIN_TAG);
        out.print(elementName);
    }

    static void printAttribute(PrintStream out, String attributeName, String attributeValue) {
        out.print(SPACE);
        out.print(attributeName);
        out.print(ATTRIBUTE_MID);
        out.print(attributeValue);
        out.print(QUOTATION);
    }

    static void printComment(PrintStream out, String comment) {
        out.print(BEGIN_COMMENT_TAG);
        out.print(comment);
        out.print(END_COMMENT_TAG);
    }

    static void printCloseTag(PrintStream out, String elementName) {
        out.print(BEGIN_CLOSE_TAG);     // </
        out.print(elementName);         // </name
        out.print(FINISH_TAG);          // </name>
    }

    static void appendOpenTag(StringBuilder sb, String elementName) {
        sb.append(BEGIN_TAG);
        sb.append(elementName);
    }

    static void appendAttribute(StringBuilder sb, String attributeName, String attributeValue) {
        sb.append(SPACE);
        sb.append(attributeName);
        sb.append(ATTRIBUTE_MID);
        sb.append(attributeValue);
        sb.append(QUOTATION);
    }

    static void appendComment(StringBuilder sb, String comment) {
        sb.append(BEGIN_COMMENT_TAG);   // <!--
        sb.append(comment);
        sb.append(END_COMMENT_TAG);     // -->
    }

    static void appendCloseTag(StringBuilder sb, String elementName) {
        sb.append(BEGIN_CLOSE_TAG);     // </
        sb.append(elementName);         // </name
        sb.append(FINISH_TAG);          // </name>
    }
}