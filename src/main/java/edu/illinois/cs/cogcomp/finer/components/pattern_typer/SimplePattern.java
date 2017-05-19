package edu.illinois.cs.cogcomp.finer.components.pattern_typer;

import java.util.Arrays;

/**
 * Created by haowu4 on 5/15/17.
 */
public class SimplePattern {
    private int before;
    private int after;
    private String[] tokens;

    public SimplePattern(int before, int after, String[] tokens) {
        this.before = before;
        this.after = after;
        this.tokens = tokens;
    }

    public int getBefore() {
        return before;
    }

    public int getAfter() {
        return after;
    }

    public String[] getTokens() {
        return tokens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimplePattern that = (SimplePattern) o;

        if (before != that.before) return false;
        if (after != that.after) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(tokens, that.tokens);
    }

    @Override
    public int hashCode() {
        int result = before;
        result = 31 * result + after;
        result = 31 * result + Arrays.hashCode(tokens);
        return result;
    }
}
