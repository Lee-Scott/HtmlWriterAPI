package htmlflow.util;

import java.io.OutputStream;
import java.io.PrintStream;

public class PrintStringBuilder extends PrintStream{

    private final StringBuilder sb = new StringBuilder();

    public PrintStringBuilder(OutputStream out) {
        super(out);
    }

    @Override
    public void print(char c) {
        super.print(c);
        sb.append(c);
    }

    @Override
    public void print(String s) {
        super.print(s);
        sb.append(s);
    }

    public String substring(int startingIndex) {
        return sb.substring(startingIndex);
    }

    public int length() {
        return sb.length();
    }
}