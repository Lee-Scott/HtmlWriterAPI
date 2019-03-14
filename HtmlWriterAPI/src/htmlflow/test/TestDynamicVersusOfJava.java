package htmlflow.test;

import htmlflow.test.model.Stock;
import htmlflow.test.views.HtmlDynamic;
import htmlflow.test.views.HtmlDynamicStocks;
import org.junit.Test;

import java.util.Iterator;

import static htmlflow.test.Utils.NEWLINE;
import static junit.framework.Assert.assertEquals;

public class TestDynamicVersusOf {

    @Test
    public void testRightDynamicWithTwoDifferentModels(){
        /*
         * First render with Stock.dummy3Items()
         */
        String actual = HtmlDynamicStocks
            .stocksViewOk
            .render(Stock.dummy3Items());
        assertLines("stocks3items.html", actual);
        /*
         * Then render with Stock.dummy5Items()
         */
        actual = HtmlDynamicStocks
            .stocksViewOk
            .render(Stock.other3Items());
        assertLines("stocks3others.html", actual);
    }

    @Test
    public void testOfWrongUseWithTwoDifferentModels(){
        /*
         * First render with Stock.dummy3Items()
         */
        String actual = HtmlDynamicStocks
            .stocksViewWrong
            .render(Stock.dummy3Items());
        assertLines("stocks3items.html", actual);
        /*
         * Then render with Stock.dummy5Items() but it will return
         * again the same previous 3 items that were stored in cache
         * of stocksViewWrong due to its wrong use with of() instead of dynamic.
         */
        actual = HtmlDynamicStocks
            .stocksViewWrong
            .render(Stock.dummy5Items());
        assertLines("stocks3items.html", actual);
    }

    private static void assertLines(String pathToExpected, String actual) {
        Iterator<String> iter = NEWLINE
            .splitAsStream(actual)
            .map(String::toLowerCase)
            .iterator();
        Utils
            .loadLines(pathToExpected)
            .map(String::toLowerCase)
            .forEach(expected -> {
                String line = iter.next();
                // System.out.println(line);
                assertEquals(expected, line);
            });
    }
}