package edu.illinois.cs.cogcomp.isa.learn;

import edu.illinois.cs.cogcomp.isa.math.SparseVector;

import java.util.List;

/**
 * Created by hao on 1/19/17.
 */
public interface Model {
    void fit(List<Example> exampleList);

    int predict(SparseVector v);
}
