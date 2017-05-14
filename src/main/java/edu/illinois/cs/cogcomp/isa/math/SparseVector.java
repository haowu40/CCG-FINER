package edu.illinois.cs.cogcomp.isa.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hao on 1/19/17.
 */
public class SparseVector {
    public static class Entry {
        public int i;
        public float v;

        public Entry(int i, float v) {
            this.i = i;
            this.v = v;
        }
    }

    public List<Entry> entries;
    public int dim;

    public SparseVector(int d) {
        this.dim = d;
        this.entries = new ArrayList<>();
    }

    public void addEntry(int i, float v) {
        entries.add(new Entry(i, v));
    }
}
