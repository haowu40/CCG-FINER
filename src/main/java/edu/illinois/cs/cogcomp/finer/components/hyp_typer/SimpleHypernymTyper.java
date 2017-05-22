package edu.illinois.cs.cogcomp.finer.components.hyp_typer;

import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Sentence;
import edu.illinois.cs.cogcomp.finer.components.IFinerTyper;
import edu.illinois.cs.cogcomp.finer.datastructure.FineTypeConstituent;
import edu.illinois.cs.cogcomp.finer.datastructure.types.FinerType;
import net.sf.extjwnl.data.Synset;

import java.util.List;
import java.util.Map;

/**
 * Created by haowu4 on 5/21/17.
 */
public class SimpleHypernymTyper implements IFinerTyper {
    TriggerWordDetecter twd;
    Map<String, List<Synset>> typeToSynsets;

    public void annotateOneMention(FineTypeConstituent mentions, FinerType coarseType, Sentence sentence) {

    }

    @Override
    public void annotate(List<FineTypeConstituent> mentions, FinerType coarseType, Sentence sentence) {
        for (FineTypeConstituent m : mentions) {
            annotateOneMention(m, coarseType, sentence);
        }
    }
}
