
package htmlflow;

import org.xmlet.htmlapifaster.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.function.Supplier;

import javax.lang.model.element.Element;

import static java.util.stream.Collectors.joining;

/**
 * The root container for HTML elements.
 * It it responsible for managing the {@code org.xmlet.htmlapi.ElementVisitor}
 * implementation, which is responsible for printing the tree of elements and
 * attributes.
 *
 * @param <T> The type of domain object bound to this View.
 *
 *        
 */
public abstract class HtmlView<T> implements HtmlWriter<T>, Element<HtmlView, Element> {
    static final String WRONG_USE_OF_PRINTSTREAM_ON_THREADSAFE_VIEWS =
            "Cannot use PrintStream output for thread-safe views!";

    static final String WRONG_USE_OF_THREADSAFE_ON_VIEWS_WITH_PRINTSTREAM =
            "Cannot set thread-safety for views with PrintStream output!";

    private static final String HEADER;
    private static final String NEWLINE = System.getProperty("line.separator");
    private static final String HEADER_TEMPLATE = "templates/HtmlView-Header.txt";

    static {
        try {
            URL headerUrl = HtmlView.class
                    .getClassLoader()
                    .getResource(HEADER_TEMPLATE);
            if(headerUrl == null)
                throw new FileNotFoundException(HEADER_TEMPLATE);
            InputStream headerStream = headerUrl.openStream();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(headerStream))) {
                HEADER = reader.lines().collect(joining(NEWLINE));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private final HtmlVisitorCache visitor;
    private final ThreadLocal<HtmlVisitorCache> threadLocalVisitor;
    private final Supplier<HtmlVisitorCache> visitorSupplier;
    private final boolean threadSafe;

    public HtmlView(Supplier<HtmlVisitorCache> visitorSupplier, boolean threadSafe) {
        this.visitorSupplier = visitorSupplier;
        this.threadSafe = threadSafe;
        if(threadSafe) {
            this.visitor = null;
            this.threadLocalVisitor = ThreadLocal.withInitial(visitorSupplier);
        } else {
            this.visitor = visitorSupplier.get();
            this.threadLocalVisitor = null;
        }
    }

    public final Html<HtmlView> html() {
        if (this.getVisitor().isWriting())
            this.getVisitor().write(HEADER);
        return new Html<>(this);
    }

    public final Div<HtmlView> div() {
        return new Div<>(this);
    }

    public final Tr<HtmlView> tr() {
        return new Tr<>(this);
    }

    public final Root<HtmlView> defineRoot(){
        return new Root<>(this);
    }

    @Override
    public final HtmlWriter<T> setPrintStream(PrintStream out) {
        if(threadSafe)
            throw new IllegalArgumentException(WRONG_USE_OF_PRINTSTREAM_ON_THREADSAFE_VIEWS);
        Supplier<HtmlVisitorCache> v = out == null
            ? () -> new HtmlVisitorStringBuilder(getVisitor().isDynamic)
            : () -> new HtmlVisitorPrintStream(out, getVisitor().isDynamic);
        return clone(v, false);
    }

    @Override
    public final HtmlView<T> self() {
        return this;
    }

    public final HtmlView<T> threadSafe(){
        /**
         * I don't like this kind of verification.
         * Yet, we need to keep backward compatibility with views based
         * on PrintStream output, which are not viable in a multi-thread scenario.
         */
        if(getVisitor() instanceof HtmlVisitorPrintStream) {
            throw new IllegalStateException(WRONG_USE_OF_THREADSAFE_ON_VIEWS_WITH_PRINTSTREAM);
        }
        return clone(visitorSupplier, true);
    }

    @Override
    public final HtmlVisitorCache getVisitor() {
        return threadSafe
            ? threadLocalVisitor.get()
            : visitor;
    }

    @Override
    public String getName() {
        return "HtmlView";
    }

    @Override
    public Element __() {
        throw new IllegalStateException("HtmlView is the root of Html tree and it has not any parent.");
    }

    @Override
    public Element getParent() {
        throw new IllegalStateException("HtmlView is the root of Html tree and it has not any parent.");
    }

    /**
     * Adds a partial view to this view.
     *
     * @param partial inner view.
     * @param model the domain object bound to the partial view.
     * @param <U> the type of the domain model of the partial view.
     */
    public final <U> void addPartial(HtmlView<U> partial, U model) {
        getVisitor().closeBeginTag();
        partial.getVisitor().depth = getVisitor().depth;
        if (this.getVisitor().isWriting())
            getVisitor().write(partial.render(model));
    }

    /**
     * Adds a partial view to this view.
     *
     * @param partial inner view.
     * @param <U> the type of the domain model of the partial view.
     */
    public final <U> void addPartial(HtmlView<U> partial) {
        getVisitor().closeBeginTag();
        partial.getVisitor().depth = getVisitor().depth;
        if (this.getVisitor().isWriting())
            getVisitor().write(partial.render());
    }

    protected abstract HtmlView<T> clone(Supplier<HtmlVisitorCache> visitorSupplier, boolean threadSafe);
}