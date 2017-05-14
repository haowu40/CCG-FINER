package edu.illinois.cs.cogcomp.finer.components.filters;

import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Sentence;
import edu.illinois.cs.cogcomp.finer.components.TriggerWordFilter;
import edu.illinois.cs.cogcomp.finer.datastructure.BaseTypes;

import java.util.Map;

/**
 * Created by hao on 1/16/17.
 */
public class TypeFilter implements TriggerWordFilter {
    @Override
    public boolean filterTriggerWord(Sentence sentence, Constituent triggerWord, Constituent mention) {
        String mentionType = mention.getAttribute("coarse-type");
        Map<String, Double> scores = triggerWord.getLabelsToScores();
        double maxScore = Double.NEGATIVE_INFINITY;
        for (double d : scores.values()) {
            maxScore = d > maxScore ? d : maxScore;
        }
        for (Map.Entry<String, Double> kv : scores.entrySet()) {
            if (kv.getValue() < maxScore) {
                continue;
            }
            if (!BaseTypes.typeMatches(kv.getKey(), mentionType))
                return false;
        }
        return true;
    }
}
