package htmlflow;

/**
 * This is the implementation of the ElementVisitor (from HtmlApiFaster
 * library) which uses an internal StringBuilder to collect information
 * about visited Html elements of a HtmlView.
 *
 */
public class HtmlVisitorStringBuilder extends HtmlVisitorCache {
    /**
     * The main StringBuilder. Read by the finished() to return the
     * resulting string with the Html content.
     */
    private final StringBuilder sb = new StringBuilder();

    public HtmlVisitorStringBuilder(boolean isDynamic) {
        super(isDynamic);
    }

    @Override
    protected void beginTag(String elementName) {
        Tags.appendOpenTag(sb, elementName); // "<elementName"
    }

    @Override
    protected void endTag(String elementName) {
        Tags.appendCloseTag(sb, elementName); // </elementName>
    }

    @Override
    protected void addAttribute(String attributeName, String attributeValue) {
        Tags.appendAttribute(sb, attributeName, attributeValue);
    }

    @Override
    protected void addComment(String comment) {
        Tags.appendComment(sb, comment);
    }

    @Override
    protected void write(String text) {
        sb.append(text);
    }
    @Override
    protected void write(char c) {
        sb.append(c);
    }

    @Override
    protected String substring(int staticBlockIndex) {
        return sb.substring(staticBlockIndex);
    }

    @Override
    protected int size() {
        return sb.length();
    }

    @Override
    protected String readAndReset() {
        String data = sb.toString();
        sb.setLength(0);
        return data;
    }

}