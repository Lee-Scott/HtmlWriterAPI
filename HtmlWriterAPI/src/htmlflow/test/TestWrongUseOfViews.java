package htmlflow.test;

import htmlflow.DynamicHtml;
import htmlflow.StaticHtml;
import org.junit.Assert;
import org.junit.Test;

public class TestWrongUseOfViews {

    /**
     * A StaticHtml view should not use dynamic().
     */
    @Test(expected = IllegalStateException.class)
    public void testWrongUseOfDynamicInStaticHtml(){
        StaticHtml.view()
            .html()
                .head()
                    .title().text("Task Details").__()
                    .dynamic(head -> {
                        Assert.fail("It should not reach here!");
                    });
    }

    /**
     * A DynamicHtml view should use render() with a model.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testWrongUseOfRenderWithoutModelInDynamicView(){
        DynamicHtml
            .view((view, model) -> {
                view.html().head().title().text("Task Details").__();
            })
            .render(); // wrong use of render without a model

    }

    /**
     * A PrintStream DynamicHtml view should use write() with a model.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testWrongUseOfWriteWithoutModelInDynamicView(){
        DynamicHtml
            .view(System.out, (view, model) -> {
                view.html().head().title().text("Task Details").__();
            })
            .write(); // wrong use of write without a model

    }

    /**
     * A StaticHtml view cannot use render() with a model.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testWrongUseOfRenderWithModelInStaticView(){
        StaticHtml
            .view()
                .html()
                    .head()
                        .title()
                            .text("Task Details")
                        .__()
                    .__()
                .__()
            .render(new Object()); // wrong use of render with a model

    }

    /**
     * A StaticHtml view cannot use write() with a model.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testWrongUseOfWriteWithModelInStaticView(){
        StaticHtml
            .view(System.out)
                .html()
                    .head()
                        .title()
                            .text("Task Details")
                        .__()
                    .__()
                .__()
            .write(new Object()); // wrong use of write with a model

    }
    /**
     * A view with a PrintStream output cannot be set to thread-safety.
     */
    @Test(expected = IllegalStateException.class)
    public void testWrongUseOfThreadSafeInViewWithPrintStream(){
        StaticHtml
            .view(System.out)
            .threadSafe();
    }
    /**
     * A thread-safe view cannot be set with a PrintStream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWrongUseSetPrintStreamInThreadSafeView(){
        StaticHtml
            .view()
            .threadSafe()
            .setPrintStream(System.out);
    }

}
