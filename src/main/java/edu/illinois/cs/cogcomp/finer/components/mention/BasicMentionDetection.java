package edu.illinois.cs.cogcomp.finer.components.mention;

import edu.illinois.cs.cogcomp.core.datastructures.ViewNames;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Sentence;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.View;
import edu.illinois.cs.cogcomp.finer.components.MentionDetecter;
import edu.illinois.cs.cogcomp.finer.datastructure.BaseTypes;
import edu.illinois.cs.cogcomp.finer.datastructure.FineNerType;
import edu.illinois.cs.cogcomp.finer.datastructure.FineTypeConstituent;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haowu4 on 1/15/17.
 */
public class BasicMentionDetection implements MentionDetecter {
    TypeMapper mapper;

    public BasicMentionDetection(TypeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<FineTypeConstituent> getMentionCandidates(Sentence sentence) {
        List<FineTypeConstituent> ret = new ArrayList<>();
        View ner = sentence.getView(ViewNames.NER_ONTONOTES);
        for (Constituent c : ner.getConstituents()) {
            FineTypeConstituent mention = new FineTypeConstituent("mention", "finer-mention", c
                    .getTextAnnotation
                            (), c.getStartSpan(), c.getEndSpan());
            String typeName = mapper.getType(c.getLabel()).toString();
            mention.getLabelsToScores().put(typeName, 1.0);
            mention.addReason(typeName, this.getClass());
            ret.add(mention);
        }
        return ret;
    }
}
