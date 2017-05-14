package edu.illinois.cs.cogcomp.isa.learn;

import edu.illinois.cs.cogcomp.isa.math.DenseVector;
import edu.illinois.cs.cogcomp.isa.math.SparseVector;

import java.util.List;

/**
 * Created by hao on 1/19/17.
 */
public class AdaGradPerceptron implements Model {
    private static final double EPISON = 0.00000001;
    private DenseVector weight;
    private DenseVector g;
    private float learnRate;

    public AdaGradPerceptron(int dim, float v) {
        this.weight = new DenseVector(dim);
        this.g = new DenseVector(dim);
        this.learnRate = v;
    }

    @Override
    public void fit(List<Example> exampleList) {
        for (Example e : exampleList) {
            int v = predict(e.x);
            if (e.y != v) {
                for (SparseVector.Entry entry : e.x.entries) {
                    double lr = learnRate / Math.sqrt(g.vs[entry.i] + EPISON);
                    weight.vs[entry.i] += (lr * entry.v * (e.y - v));
                    g.vs[entry.i] += (entry.v * entry.v);
                }
            }
        }
    }

    @Override
    public int predict(SparseVector v) {
        return weight.dot(v) > 0 ? 1 : 0;
    }


}
