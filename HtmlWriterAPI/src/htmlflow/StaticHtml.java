package htmlflow;

import java.io.PrintStream;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Static Html view.
 *
 */
public class StaticHtml extends HtmlView<Object> {

    private static final String WRONG_USE_OF_RENDER_WITH_MODEL =
             "Wrong use of StaticView! Model object not " +
             "supported or you should use a dynamic view instead!";

    private final Consumer<StaticHtml> template;

    public static StaticHtml view(PrintStream out, Consumer<StaticHtml> template){
        return new StaticHtml(out, template);
    }

    public static StaticHtml view(PrintStream out){
        return new StaticHtml(out);
    }

    public static StaticHtml view(Consumer<StaticHtml> template){
        return new StaticHtml(template);
    }

    public static StaticHtml view(){
        return new StaticHtml();
    }

    /**
     * Auxiliary constructor used by clone().
     */
    private StaticHtml(
        Supplier<HtmlVisitorCache> visitorSupplier,
        boolean threadSafe,
        Consumer<StaticHtml> template)
    {
        super(visitorSupplier, threadSafe);
        this.template = template;
    }

    private StaticHtml() {
        this((Consumer<StaticHtml>) null);
    }

    private StaticHtml(Consumer<StaticHtml>  template) {
        super(() -> new HtmlVisitorStringBuilder(false), false);
        this.template = template;
    }

    private StaticHtml(PrintStream out) {
        this(out, null);
    }

    private StaticHtml(PrintStream out, Consumer<StaticHtml> template) {
        super(() -> new HtmlVisitorPrintStream(out, false), false);
        this.template = template;
    }

    @Override
    public final String render() {
        if(template != null)
            template.accept(this);
        return getVisitor().finished();

    }

    @Override
    public final String render(Object model) {
        throw new UnsupportedOperationException(WRONG_USE_OF_RENDER_WITH_MODEL);
    }

    @Override
    public final void write() {
        if(template != null)
            template.accept(this);
        getVisitor().finished();
    }

    @Override
    public final void write(Object model) {
        throw new UnsupportedOperationException(WRONG_USE_OF_RENDER_WITH_MODEL);
    }

    @Override
    protected final HtmlView<Object> clone(Supplier<HtmlVisitorCache> visitorSupplier, boolean threadSafe) {
        return new StaticHtml(visitorSupplier, threadSafe, template);
    }
}