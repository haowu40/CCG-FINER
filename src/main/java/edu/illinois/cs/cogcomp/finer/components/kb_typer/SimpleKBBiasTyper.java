package edu.illinois.cs.cogcomp.finer.components.kb_typer;

import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Sentence;
import edu.illinois.cs.cogcomp.finer.components.IFinerTyper;
import edu.illinois.cs.cogcomp.finer.datastructure.AnnotationReason;
import edu.illinois.cs.cogcomp.finer.datastructure.FineTypeConstituent;
import edu.illinois.cs.cogcomp.finer.datastructure.types.FinerType;

import java.io.BufferedReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by haowu4 on 5/15/17.
 */
public class SimpleKBBiasTyper implements IFinerTyper {
    Map<String, Map<FinerType, Double>> surfaceToTypeDB;

    public SimpleKBBiasTyper(Map<String, Map<FinerType, Double>> surfaceToTypeDB) {
        this.surfaceToTypeDB = surfaceToTypeDB;
    }

    public List<FinerType> annotateSingleMention(Constituent mention, FinerType coarseType) {
        Set<FinerType> ret = new HashSet<>();
        String surface = mention.getSurfaceForm();
        Map<FinerType, Double> candidates = surfaceToTypeDB.getOrDefault(surface, new HashMap<>());

        double bestScore = Double.NEGATIVE_INFINITY;
        FinerType bestType = null;
        double norm = 0.0;

        for (Map.Entry<FinerType, Double> entry : candidates.entrySet()) {
            FinerType t = entry.getKey();
            double v = entry.getValue();
            if (coarseType.isParentOf(t)) {
                if (v == 1.0) {
                    ret.add(t);
                } else {
                    norm += v;
                    if (bestScore < v) {
                        bestScore = v;
                        bestType = t;
                    }
                }
            }
        }

        if (bestType != null && bestScore/norm > 0.4) {
            ret.add(bestType);
        }

        return ret.stream().collect(Collectors.toList());

    }

    @Override
    public void annotate(List<FineTypeConstituent> mentions, Sentence sentence) {
        for (FineTypeConstituent mention : mentions) {
            List<FinerType> annotated = annotateSingleMention(mention, mention.getCoarseType());
            for (FinerType t : annotated) {
                String typeName = t.getType();
                mention.addFineType(t);
                AnnotationReason reason = new AnnotationReason(SimpleKBBiasTyper.class);
                mention.addReason(typeName, reason);
            }
        }

    }
}
