package edu.illinois.cs.cogcomp.finer.distribuational_semantic;

import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation
        .TextAnnotation;

import java.util.List;

/**
 * Created by haowu4 on 1/15/17.
 */
public interface Extractor {
    List<String> extract(TextAnnotation ta, Constituent mention);
    public String getName();
}
