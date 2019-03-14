package htmlflow.test;

import htmlflow.StaticHtml;
import junit.framework.Assert;
import org.junit.Test;

public class TestHtmlViewAsElement {

    @Test
    public void testSelf() {
        StaticHtml view = StaticHtml.view();
        Assert.assertSame(view, view.self());
        Assert.assertEquals("HtmlView", view.getName());
    }

    @Test(expected = IllegalStateException.class)
    public void testWrong__use() {
        StaticHtml.view().__();
    }

    @Test(expected = IllegalStateException.class)
    public void testWrongParentUse() {
        StaticHtml.view().getParent();
    }
}