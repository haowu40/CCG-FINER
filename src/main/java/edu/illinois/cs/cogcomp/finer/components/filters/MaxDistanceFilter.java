package edu.illinois.cs.cogcomp.finer.components.filters;

import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Sentence;
import edu.illinois.cs.cogcomp.finer.components.TriggerWordFilter;

/**
 * Created by haowu4 on 1/15/17.
 */
public class MaxDistanceFilter implements TriggerWordFilter {

    private static final int MAX_WIN = 5;

    @Override
    public boolean filterTriggerWord(Sentence sentence, Constituent
            triggerWord, Constituent mention) {
        int triggerStart = triggerWord.getStartSpan();
        int triggerEnd = triggerWord.getEndSpan();

        int mentionStart = mention.getStartSpan();
        int mentionEnd = mention.getEndSpan();

        if (mention.doesConstituentCover(triggerWord)) {
            return true;
        }

        if (triggerStart < (mentionStart - MAX_WIN) || triggerEnd >
                (mentionStart + MAX_WIN)) {
            return false;
        }

        return true;
    }
}
