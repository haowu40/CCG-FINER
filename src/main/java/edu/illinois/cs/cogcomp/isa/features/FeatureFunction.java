package edu.illinois.cs.cogcomp.isa.features;

import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation
        .TextAnnotation;
import edu.illinois.cs.cogcomp.isa.math.SparseVector;

/**
 * Created by haowu4 on 1/19/17.
 */
public interface FeatureFunction {
    void addToFeatureVec(TextAnnotation ta, Constituent c1, Constituent
            c2, SparseVector sv, Lexicon lexicon);

    void previewFeatures(TextAnnotation ta, Constituent c1, Constituent
            c2, Lexicon lexicon);
}
