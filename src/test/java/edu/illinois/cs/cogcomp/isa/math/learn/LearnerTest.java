package edu.illinois.cs.cogcomp.isa.math.learn;

import edu.illinois.cs.cogcomp.isa.learn.*;
import edu.illinois.cs.cogcomp.isa.math.SparseVector;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haowu4 on 1/19/17.
 */
public class LearnerTest {
    public static List<Example> loadExamples(String path, int dim) throws
            IOException {
        List<String> strings = FileUtils.readLines
                (new File(path));
        List<Example> examples = new ArrayList<>();
        for (String line : strings) {
            String[] parts = line.split(" ");
            SparseVector sparseVector = new SparseVector(dim);
            for (int i = 1; i < parts.length; i++) {
                sparseVector.addEntry(Integer.valueOf(parts[i].split
                        (":")[0]), Float.valueOf(parts[i].split
                        (":")[1]));
            }
            examples.add(new Example(sparseVector, parts[0].equals("+1") ? 1 :
                    0));
        }
        return examples;
    }

    public static void main(String[] args) throws IOException {
        int dim = 301;

        List<Example> training = loadExamples
                ("/home/haowu4/data/uci/binary/train", dim);


        List<Example> testing = loadExamples
                ("/home/haowu4/data/uci/binary/test", dim);

        Model perceptron = new Perceptron(dim, 0.2f);
        Model avgPerceptron = new AvgPerceptron(dim, 0.2f);
        Model adagradPerceptron = new AdaGradPerceptron(dim, 0.2f);
        perceptron.fit(training);
        perceptron.fit(training);
        perceptron.fit(training);
        test(perceptron, testing);
//
        avgPerceptron.fit(training);
        test(avgPerceptron, testing);
//
        adagradPerceptron.fit(training);
        adagradPerceptron.fit(training);
        adagradPerceptron.fit(training);
        test(adagradPerceptron, testing);
    }

    public static void test(Model model, List<Example> examples) {
        int tp = 0;
        int tn = 0;
        int fn = 0;
        int fp = 0;

        for (Example r : examples) {
            int ty = model.predict(r.x);
            if (ty == r.y) {
                if (ty == 1) {
                    tp++;
                } else {
                    tn++;
                }
            } else {
                if (ty == 1) {
                    fp++;
                } else {
                    fn++;
                }
            }
        }

        System.out.printf("TP %d, TN %d, FP %d, FN %d\n", tp, tn, fp, fn);

    }
}
