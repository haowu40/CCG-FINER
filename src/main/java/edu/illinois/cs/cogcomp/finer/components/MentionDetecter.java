package edu.illinois.cs.cogcomp.finer.components;

import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Sentence;

import java.util.List;

/**
 * Created by haowu4 on 1/15/17.
 */
public interface MentionDetecter {
    List<Constituent> getMentionCandidates(Sentence sentence);
}
