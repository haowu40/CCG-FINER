package edu.illinois.cs.cogcomp.finer.components.hyp_typer.filters;

import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Sentence;
import edu.illinois.cs.cogcomp.finer.components.hyp_typer.TriggerWordFilter;

/**
 * Created by haowu4 on 1/15/17.
 */
public class QuotationFilter implements TriggerWordFilter {

    private static final int MAX_WIN = 5;

    @Override
    public boolean filterTriggerWord(Sentence sentence, Constituent
            triggerWord, Constituent mention) {
        int triggerStart = triggerWord.getStartSpan();
        int triggerEnd = triggerWord.getEndSpan();

        int mentionStart = mention.getStartSpan();
        int mentionEnd = mention.getEndSpan();

//        if (triggerStart < (mentionStart - MAX_WIN) || triggerEnd > (mentionEnd + MAX_WIN)) {
//            return true;
//        }

        for (int i = triggerEnd; i < mentionStart; i++) {
            String c = triggerWord.getTextAnnotation().getToken(i);
            if (c.equals("\"")) {
                return false;
            }
        }

        for (int i = mentionEnd; i < triggerStart; i++) {
            String c = triggerWord.getTextAnnotation().getToken(i);
            if (c.equals("\"")) {
                return false;
            }
        }

        return true;
    }
}
