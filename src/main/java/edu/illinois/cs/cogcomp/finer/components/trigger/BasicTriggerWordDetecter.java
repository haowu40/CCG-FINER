package edu.illinois.cs.cogcomp.finer.components.trigger;

import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Sentence;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.View;
import edu.illinois.cs.cogcomp.finer.components.TriggerWordDetecter;
import edu.illinois.cs.cogcomp.finer.utils.WordNetUtils;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.Synset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static edu.illinois.cs.cogcomp.wsd.Constants.VIEWNAME;

/**
 * Created by haowu4 on 1/16/17.
 */
public class BasicTriggerWordDetecter implements TriggerWordDetecter {
    WordNetUtils wordNetUtils;

    public BasicTriggerWordDetecter() {
        try {
            wordNetUtils = WordNetUtils.getInstance();
        } catch (JWNLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Constituent> getTriggerWords(Sentence sentence, Map<String,
            List<Synset>> types) {
        List<Constituent> triggers = new ArrayList<>();
        View wsds = sentence.getView(VIEWNAME);
        for (Constituent c : wsds.getConstituents()) {
            try {
                Map<String, Double> typeScores = wordNetUtils.getTypeScores(c.getLabel(), types);
                if (typeScores.isEmpty()) {
                    continue;
                }
                triggers.add(new Constituent(typeScores, "finer-trigger", c.getTextAnnotation(), c.getStartSpan(), c.getEndSpan()));
            } catch (JWNLException e) {
                e.printStackTrace();
            }
        }
        return triggers;
    }
}
