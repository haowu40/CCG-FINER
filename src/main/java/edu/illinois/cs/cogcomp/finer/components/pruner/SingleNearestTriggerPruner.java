package edu.illinois.cs.cogcomp.finer.components.pruner;

import edu.illinois.cs.cogcomp.core.datastructures.Pair;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Relation;
import edu.illinois.cs.cogcomp.finer.TriggerMentionPruner;
import net.sf.ehcache.config.TerracottaConfiguration;

import java.util.*;

/**
 * Created by haowu4 on 1/17/17.
 */
public class SingleNearestTriggerPruner implements TriggerMentionPruner {

    public static int distance(Constituent c1, Constituent c2) {
        if (c1.getStartSpan() < c2.getStartSpan() && c2.getStartSpan() <
                c1.getEndSpan())
            // Inside.
            return 0;

        if (c1.getEndSpan() < c2.getStartSpan()) {
            return c2.getStartSpan() - c1.getEndSpan();
        }


        if (c1.getStartSpan() > c2.getEndSpan()) {
            return c1.getStartSpan() - c2.getEndSpan();
        }

        return 0;
    }

    @Override
    /*
      Relation is pair of <Mention,Trigger>, but relation is the opposite
      direction.
     */
    public List<Relation> pruneRelations(List<Pair<Constituent, Constituent>>
                                                 relations) {
        List<Relation> ret = new ArrayList<>();
        Map<Constituent, List<Constituent>> constituentListMap = new
                HashMap<>();
        for (Pair<Constituent, Constituent> r : relations) {
            List<Constituent> triggers = constituentListMap.getOrDefault(r
                    .getFirst(), new
                    ArrayList<>());
            triggers.add(r.getSecond());
            constituentListMap.put(r.getFirst(), triggers);
        }

        for (Constituent mention : constituentListMap.keySet()) {
            List<Constituent> triggers = constituentListMap.get(mention);
            triggers.sort(Comparator.comparingInt(o -> distance(mention, o)));
            ret.add(new Relation("finer-triggering", triggers.get(0),
                    mention, 1));
        }
        return ret;
    }
}
