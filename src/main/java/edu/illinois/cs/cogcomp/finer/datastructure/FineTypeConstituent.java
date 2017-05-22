package edu.illinois.cs.cogcomp.finer.datastructure;

import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.TextAnnotation;
import edu.illinois.cs.cogcomp.finer.datastructure.types.FinerType;

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

    private Map<String, List<AnnotationReason>> reasons;

    public void addReason(String type, AnnotationReason reason) {
        List<AnnotationReason> x = reasons.getOrDefault(type, new ArrayList<>());
        reasons.put(type, x);
    }

    public void addType(FinerType t) {
        if (t.isVisible()) {
            this.labelsToScores.put(t.getType(), 1.0);
        }
    }

}
