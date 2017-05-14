package edu.illinois.cs.cogcomp.isa.math;

/**
 * Created by hao on 1/19/17.
 */
public class DenseVector {
    public float[] vs;

    public DenseVector(int d) {
        this.vs = new float[d];
    }

    public void iadd(SparseVector sparseVector, float weight) {
        for (SparseVector.Entry e : sparseVector.entries) {
            this.vs[e.i] += (e.v * weight);
        }
    }

    public void iminus(SparseVector sparseVector, float weight) {
        for (SparseVector.Entry e : sparseVector.entries) {
            this.vs[e.i] -= (e.v * weight);
        }
    }

    public float dot(SparseVector sparseVector) {
        float ret = 0;
        for (SparseVector.Entry e : sparseVector.entries) {
            float k = this.vs[e.i] * e.v;
            ret += k;
        }
        return ret;
    }

    public void iadd(SparseVector sparseVector) {
        for (SparseVector.Entry e : sparseVector.entries) {
            this.vs[e.i] += e.v;
        }
    }

    public void iminus(SparseVector sparseVector) {
        for (SparseVector.Entry e : sparseVector.entries) {
            this.vs[e.i] -= e.v;
        }
    }

}
