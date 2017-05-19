package edu.illinois.cs.cogcomp.finer.datastructure;

import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.TextAnnotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by haowu4 on 5/17/17.
 */
public class FineTypeConstituent extends Constituent {
    public FineTypeConstituent(String label, String viewName, TextAnnotation text, int start, int end) {
        super(label, viewName, text, start, end);
        reasons = new HashMap<>();
    }

    public FineTypeConstituent(Map<String, Double> labelsToScores, String viewName, TextAnnotation text, int start, int end) {
        super(labelsToScores, viewName, text, start, end);
        reasons = new HashMap<>();
    }

    public FineTypeConstituent(String label, double score, String viewName, TextAnnotation text, int start, int end) {
        super(label, score, viewName, text, start, end);
        reasons = new HashMap<>();
    }

    private Map<String, List<String>> reasons;

    public void addReason(String type, Class annotatorClazz) {
        List<String> x = reasons.getOrDefault(type, new ArrayList<>());
        x.add(annotatorClazz.getCanonicalName());
        reasons.put(type, x);
    }

}
