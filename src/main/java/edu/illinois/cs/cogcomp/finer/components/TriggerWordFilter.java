package edu.illinois.cs.cogcomp.finer.components;

import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Sentence;

/**
 * Created by haowu4 on 1/15/17.
 */
public interface TriggerWordFilter {
    boolean filterTriggerWord(Sentence sentence, Constituent triggerWord,
                              Constituent mention);
}
