package edu.illinois.cs.cogcomp.finer;

import edu.illinois.cs.cogcomp.core.datastructures.Pair;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Relation;

import java.util.List;

/**
 * Created by haowu4 on 1/17/17.
 */
public interface TriggerMentionPruner {
    List<Relation> pruneRelations(List<Pair<Constituent, Constituent>>
                                          relations);
}
