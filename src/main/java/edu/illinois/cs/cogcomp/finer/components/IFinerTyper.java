package edu.illinois.cs.cogcomp.finer.components;

import edu.illinois.cs.cogcomp.core.datastructures.Pair;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.finer.datastructure.FineNerType;

import java.util.List;

/**
 * Created by haowu4 on 5/15/17.
 */
public interface IFinerTyper {
    List<Pair<Constituent, List<FineNerType>>> annotate(List<Constituent> mentions);
}
