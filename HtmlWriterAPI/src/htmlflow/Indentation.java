package htmlflow;

import static htmlflow.Tags.FINISH_TAG;

/**
 */
class Indentation {

    private static final int MAX_TABS = 1000;
    private static final char NEWLINE = '\n';
    private static final char TAB = '\t';
    private static final String[] tabs = createTabs(MAX_TABS);
    private static final String[] closedTabs = createClosedTabs(MAX_TABS);

    private Indentation(){ }

    private static String[] createTabs(int tabsMax){
        String[] tabs = new String[tabsMax];

        for (int i = 0; i < tabsMax; i++) {
            char[] newTab = new char[i + 1];
            newTab[0] = NEWLINE;

            tabs[i] = new String(newTab).replace('\0', TAB);
        }

        return tabs;
    }

    private static String[] createClosedTabs(int tabsMax){
        String[] closedTabs = new String[tabsMax];

        for (int i = 0; i < tabsMax; i++) {

            char[] newClosedTab = new char[i + 2];
            newClosedTab[0] = FINISH_TAG;
            newClosedTab[1] = NEWLINE;

            closedTabs[i] = new String(newClosedTab).replace('\0', TAB);
        }

        return closedTabs;
    }

    public static String tabs(int depth){
        return tabs[depth];
    }

    public static String closedTabs(int depth) {
        return closedTabs[depth];
    }
}