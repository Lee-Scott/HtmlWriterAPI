package htmlflow;

import htmlflow.util.PrintStringBuilder;

import java.io.PrintStream;


public class HtmlVisitorPrintStream extends HtmlVisitorCache {
    /**
     * The final PrintStream destination of the HTML content
     * produced by this visitor.
     */
    private final PrintStream out;
    /**
     * This is a PrintStringBuilder which collects all content into
     * an internal StringBuilder, which is read by the cache.
     * When first visit finishes we exchange current to the value of
     * field out, which is a PrintStream.
     */
    private PrintStream current;

    public HtmlVisitorPrintStream(PrintStream out, boolean isDynamic) {
        super(isDynamic);
        this.out = out;
        this.current = new PrintStringBuilder(out);
    }

    @Override
    protected void beginTag(String elementName) {
        Tags.printOpenTag(current, elementName); // "<elementName"
    }

    @Override
    protected void endTag(String elementName) {
        Tags.printCloseTag(current, elementName); // </elementName>
    }

    @Override
    protected void addAttribute(String attributeName, String attributeValue) {
        Tags.printAttribute(current, attributeName, attributeValue);
    }

    @Override
    protected void addComment(String comment) {
        Tags.printComment(current, comment);
    }

    @Override
    protected void write(String text) {
        current.print(text);
    }
    @Override
    protected void write(char c) {
        current.print(c);
    }

    @Override
    protected String substring(int staticBlockIndex) {
        /**
         * REMARK: we need to keep current field of type PrintStream because after
         * the first visit we exchange it to the value of field out, which is a PrintStream.
         * After that, the cache is finished and we are sure that substring() is no
         * longer invoked.
         */
        return ((PrintStringBuilder) current).substring(staticBlockIndex);
    }

    @Override
    protected int size() {
        /**
         * REMARK: we need to keep current field of type PrintStream because after
         * the first visit we exchange it to the value of field out, which is a PrintStream.
         * After that, the cache is finished and we are sure that size() is no
         * longer invoked.
         */
        return ((PrintStringBuilder) current).length();
    }

    @Override
    protected String readAndReset() {
        this.current = out;
        /**
         * This visitor writes the content to a PrintStream and we should not consider
         * the value returned by readAndReset().
         * For that reason we return null to avoid misleading uses of this result.
         * For anyone who tries to use it will get NullPointerException.
         */
        return null;
    }
}