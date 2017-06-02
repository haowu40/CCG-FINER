package edu.illinois.cs.cogcomp.finer.components.hyp_typer;

import edu.illinois.cs.cogcomp.core.datastructures.ViewNames;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Sentence;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.View;
import edu.illinois.cs.cogcomp.finer.components.IFinerTyper;
import edu.illinois.cs.cogcomp.finer.datastructure.AnnotationReason;
import edu.illinois.cs.cogcomp.finer.datastructure.FineTypeConstituent;
import edu.illinois.cs.cogcomp.finer.datastructure.types.FinerType;
import edu.illinois.cs.cogcomp.finer.utils.WordNetUtils;
import edu.illinois.cs.cogcomp.wsd.WSD;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.Synset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by haowu4 on 5/21/17.
 */
public class SimpleHypernymTyper implements IFinerTyper {
    Map<String, List<FinerType>> typeToSynsets;
    WordNetUtils wordNetUtils;

    public SimpleHypernymTyper(Map<String, List<FinerType>> typeToSynsets) {
        this.typeToSynsets = typeToSynsets;
        try {
            wordNetUtils = WordNetUtils.getInstance();
        } catch (JWNLException e) {
            e.printStackTrace();
        }
    }

    public void annotateOneMention(FineTypeConstituent mention, Sentence sentence) throws JWNLException {
        FinerType coarseType = mention.getCoarseType();
        int start = mention.getStartSpan();
        int end = mention.getEndSpan();
        if (start > 0) {
            Constituent word_before = mention.getTextAnnotation().getView(ViewNames.TOKENS).getConstituents().get(start - 1);
            if (word_before.getSentenceId() == mention.getSentenceId()) {
                start = start - 1;
            }
        }

        View wsdView = mention.getTextAnnotation().getView(WSD.VIEWNAME);
        View posView = mention.getTextAnnotation().getView(ViewNames.POS);

        for (Constituent c : wsdView.getConstituentsCoveringSpan(start, end)) {
            if (posView.getConstituentsCovering(c).get(0).getLabel().startsWith("N")) {
                Synset synset = wordNetUtils.getSynsetFromNLTKString(c.getLabel());
                String synset_offset_pos = synset.getOffset() + "" + synset.getPOS();
                List<FinerType> infered = typeToSynsets.getOrDefault(synset_offset_pos, new ArrayList<>());
                for (FinerType t : infered) {
                    if (t.isChildOf(coarseType)) {
                        mention.addFineType(t);
                        AnnotationReason ar = new AnnotationReason(SimpleHypernymTyper.class);
                        ar.addClue(c);
                        mention.addReason(t.getType(), ar);
                    }
                }
            }
        }
    }

    @Override
    public void annotate(List<FineTypeConstituent> mentions, Sentence sentence) {
        for (FineTypeConstituent m : mentions) {
            try {
                annotateOneMention(m, sentence);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
