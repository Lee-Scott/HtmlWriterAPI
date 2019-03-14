package htmlflow;

@FunctionalInterface
public interface HtmlTemplate<T> {

    void resolve(DynamicHtml<T> view, T model, HtmlView...partials);
}