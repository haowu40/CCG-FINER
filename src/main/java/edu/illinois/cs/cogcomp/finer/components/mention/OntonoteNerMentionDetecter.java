package edu.illinois.cs.cogcomp.finer.components.mention;

import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Sentence;
import edu.illinois.cs.cogcomp.finer.components.MentionDetecter;

import java.util.List;

/**
 * Created by haowu4 on 5/14/17.
 */
public class OntonoteNerMentionDetecter implements MentionDetecter{
    @Override
    public List<Constituent> getMentionCandidates(Sentence sentence) {
        return null;
    }
}
