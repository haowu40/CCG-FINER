package edu.illinois.cs.cogcomp.finer.distribuational_semantic.extractors;

import edu.illinois.cs.cogcomp.core.datastructures.ViewNames;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation
        .TextAnnotation;

import edu.illinois.cs.cogcomp.core.datastructures.textannotation.View;
import edu.illinois.cs.cogcomp.finer.distribuational_semantic.Extractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haowu4 on 1/18/17.
 */
public class NearestVerbExtractor implements Extractor {
    public static final int MAX_WINDOW = 5;

    @Override
    public List<String> extract(TextAnnotation ta, Constituent mention) {
        List<String> ret = new ArrayList<>();
        View pos = ta.getView(ViewNames.POS);
        int start = Math.max(0, mention.getStartSpan() - MAX_WINDOW);
        int end = Math.min(mention.getEndSpan() + MAX_WINDOW, ta.getTokens
                ().length);

        for (int i = mention.getStartSpan(); i > start; i--) {
            Constituent p = pos.getConstituentsCoveringToken(i).get(0);
            if (p.getLabel().startsWith("V")) {
                ret.add("L=" + p.getSurfaceForm());
            }
        }

        for (int i = mention.getEndSpan(); i < end; i++) {
            Constituent p = pos.getConstituentsCoveringToken(i).get(0);
            if (p.getLabel().startsWith("V")) {
                ret.add("R=" + p.getSurfaceForm());
            }
        }

        return ret;
    }

    @Override
    public String getName() {
        return "NearestVerb";
    }
}
