package edu.illinois.cs.cogcomp.isa.learn;

import edu.illinois.cs.cogcomp.isa.math.DenseVector;
import edu.illinois.cs.cogcomp.isa.math.SparseVector;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hao on 1/19/17.
 */
public class AvgPerceptron implements Model{

    private DenseVector weight;
    private DenseVector sum;
    int iteration;
    private float learnRate;

    public AvgPerceptron(int dim, float learnRate) {
        this.iteration = 0;
        this.weight = new DenseVector(dim);
        this.sum = new DenseVector(dim);
        this.learnRate = learnRate;
    }

    @Override
    public void fit(List<Example> exampleList) {
        for (Example e : exampleList) {
            iteration++;
            int v = predictCurrent(e.x);
            if (e.y != v) {
                for (SparseVector.Entry entry : e.x.entries) {
                    weight.vs[entry.i] += (learnRate * entry.v * (e.y - v));
                    sum.vs[entry.i] += (iteration * learnRate * entry.v * (e
                            .y - v));
                }
            }
        }
    }

    public int predictCurrent(SparseVector v) {
        return weight.dot(v) > 0 ? 1 : 0;
    }

    @Override
    public int predict(SparseVector v) {
        return weight.dot(v) - sum.dot(v) / ((double) iteration) > 0 ? 1 : 0;
    }
}
