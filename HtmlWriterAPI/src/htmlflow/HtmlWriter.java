package htmlflow;

import java.io.PrintStream;

/**
 * Writes HTML content into a {@link java.io.PrintStream}.
 *
 * @param <T> The type of domain object bound to this View.
 *
 */
public interface HtmlWriter<T>{
    /**
     * Writes into an internal PrintStream the HTML content.
     */
    void write();
    /**
     * Writes into an internal PrintStream the HTML content
     * binding the object model with the HTML elements.
     *
     * @param model An object model that could be bind to each element.
     */
    void write(T model);

    /**
     * Sets the current PrintStream.
     */
    HtmlWriter<T> setPrintStream(PrintStream out);

    /**
     * Returns a new String with the HTML content of this view.
     */
    String render();

    /**
     * Returns a new String with the HTML content of this view.
     *
     * @param model An object model that could be bind to each element of the view.
     */
    String render(T model);
}