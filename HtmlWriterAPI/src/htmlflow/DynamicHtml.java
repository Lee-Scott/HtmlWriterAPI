/*
 * 
 */

package htmlflow;

import java.io.PrintStream;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Dynamic views can be bound to a domain object.
 *
 * @param <T> The type of domain object bound to this View.
 
 */
public class DynamicHtml<T> extends HtmlView<T> {

    private static final String WRONG_USE_OF_RENDER_WITHOUT_MODEL =
             "Wrong use of DynamicView! You should provide a " +
             "model parameter or use a static view instead!";
    /**
     * Used alternately with the field binder.
     * A template function receives 3 arguments:
     *   the view, the domain object and a varargs array of partial views.
     */
    private final HtmlTemplate<T> template;
    /**
     * Used alternately with the field template.
     * A binder function is responsible for binding the View with a domain object.
     * Thus, it is a function that receives two arguments: the view and the domain object.
     */
    private final BiConsumer<DynamicHtml<T>, T> binder;

    public static <U> DynamicHtml<U> view(PrintStream out, HtmlTemplate<U> template){
        return new DynamicHtml<>(out, template);
    }

    public static <U> DynamicHtml<U> view(PrintStream out, BiConsumer<DynamicHtml<U>, U> binder){
        return new DynamicHtml<>(out, binder);
    }

    public static <U> DynamicHtml<U> view(HtmlTemplate<U> template){
        return new DynamicHtml<>(template);
    }

    public static <U> DynamicHtml<U> view(BiConsumer<DynamicHtml<U>, U> binder) {
        return new DynamicHtml<>(binder);
    }

    /**
     * Auxiliary constructor used by clone().
     */
    private DynamicHtml(
        Supplier<HtmlVisitorCache> visitorSupplier,
        boolean threadSafe,
        HtmlTemplate<T> template,
        BiConsumer<DynamicHtml<T>, T> binder)
    {
        super(visitorSupplier, threadSafe);
        this.template = template;
        this.binder = binder;
    }

    private DynamicHtml(PrintStream out, HtmlTemplate<T> template) {
        super((() -> new HtmlVisitorPrintStream(out, true)), false);
        this.binder = null;
        this.template = template;
    }

    private DynamicHtml(PrintStream out, BiConsumer<DynamicHtml<T>, T> binder) {
        super(() -> new HtmlVisitorPrintStream(out, true), false);
        this.binder = binder;
        this.template = null;
    }

    private DynamicHtml(HtmlTemplate<T> template) {
        super(() -> new HtmlVisitorStringBuilder(true), false);
        this.binder = null;
        this.template = template;
    }

    private DynamicHtml(BiConsumer<DynamicHtml<T>, T> binder) {
        super(() -> new HtmlVisitorStringBuilder(true), false);
        this.binder = binder;
        this.template = null;
    }

    @Override
    public final String render() {
        throw new UnsupportedOperationException(WRONG_USE_OF_RENDER_WITHOUT_MODEL);
    }

    @Override
    public final String render(T model) {
        binder.accept(this, model);
        return getVisitor().finished();
    }

    public final String render(T model, HtmlView...partials) {
        template.resolve(this, model, partials);
        return getVisitor().finished();
    }

    @Override
    public final void write() {
        throw new UnsupportedOperationException(WRONG_USE_OF_RENDER_WITHOUT_MODEL);
    }

    @Override
    public final void write(T model) {
        this.render(model);
    }

    public final void write(T model, HtmlView...partials) {
        this.render(model, partials);
    }

    @Override
    protected final HtmlView<T> clone(
        Supplier<HtmlVisitorCache> visitorSupplier,
        boolean threadSafe)
    {
        return new DynamicHtml<>(visitorSupplier, threadSafe, template, binder);
    }
}