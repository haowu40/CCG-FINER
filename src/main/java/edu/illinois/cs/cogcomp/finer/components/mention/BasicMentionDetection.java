package edu.illinois.cs.cogcomp.finer.components.mention;

import edu.illinois.cs.cogcomp.core.datastructures.ViewNames;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Sentence;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.View;
import edu.illinois.cs.cogcomp.finer.components.MentionDetecter;
import edu.illinois.cs.cogcomp.finer.datastructure.BaseTypes;
import edu.illinois.cs.cogcomp.finer.datastructure.FineNerType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haowu4 on 1/15/17.
 */
public class BasicMentionDetection implements MentionDetecter {
    public static String getLabel(String conllTypeString) {
        if (conllTypeString.equals("PER")) {
            return "person";
        }

        if (conllTypeString.equals("ORG")) {
            return "organization";
        }

        if (conllTypeString.equals("LOC")) {
            return "location";
        }

        if (conllTypeString.equals("MISC")) {
            return "other";
        }

        throw new RuntimeException("Unknown types : " + conllTypeString);
    }

    @Override
    public List<Constituent> getMentionCandidates(Sentence sentence) {
        List<Constituent> ret = new ArrayList<>();
        View ner = sentence.getView(ViewNames.NER_CONLL);
        for (Constituent c : ner.getConstituents()) {
            Constituent mention = new Constituent("mention", "finer-mention", c
                    .getTextAnnotation
                            (), c.getStartSpan(), c.getEndSpan());
            mention.addAttribute("coarse-type", getLabel(c.getLabel()));
            ret.add(mention);
        }
        return ret;
    }
}
