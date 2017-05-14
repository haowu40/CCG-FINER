package edu.illinois.cs.cogcomp.other.embeddings;

import java.util.List;

/**
 * Created by pavankumar on 1/13/17.
 */
public class DoubleArrayVectorOps {

    public static double getNorm(double[] vector) {
        double norm = 0;
        for (int i = 0; i < vector.length; i++)
            norm += vector[i] * vector[i];
        norm = Math.sqrt(norm);
        return norm;
    }

    public static double[] getNormalized(double[] vector) {
        double[] normalizedVector = new double[vector.length];

        double norm = getNorm(vector);
        for (int i = 0; i < vector.length; i++)
            normalizedVector[i] = vector[i] / norm;

        return normalizedVector;
    }

    public static double dot(double[] vec1, double[] vec2) {
        double dot = 0;
        for (int i = 0; i < vec1.length; i++)
            dot += vec1[i] * vec2[i];
        return dot;
    }

    public static double[] dot(List<double[]> vec1List, double[] vec2) {
        double[] result = new double[vec1List.size()];
        for (int i = 0; i < vec1List.size(); i++)
            result[i] = dot(vec1List.get(i), vec2);
        return result;
    }

    public static double cosine(double[] vec1, double[] vec2) {
        if (vec1.length != vec2.length)
            throw new RuntimeException("Cosine only allowed for vectors of equal length. Lengths of Vectors --> Vector1 = " + vec1.length + ", Vector2 = " + vec2.length);

        double norm1 = getNorm(vec1);
        double norm2 = getNorm(vec2);
        double dot = dot(vec1, vec2);

        return dot / ((norm1 + Double.MIN_NORMAL) * (norm2 + Double.MIN_NORMAL));
    }

    public static double[] add(double[] vec1, double[] vec2) {
        if (vec1.length != vec2.length)
            throw new RuntimeException("Addition only allowed for vectors of equal length. Lengths of Vectors --> Vector1 = " + vec1.length + ", Vector2 = " + vec2.length);
        int size = vec1.length;
        double[] sum = new double[size];

        for (int i = 0; i < size; i++)
            sum[i] = vec1[i] + vec2[i];

        return sum;
    }

}