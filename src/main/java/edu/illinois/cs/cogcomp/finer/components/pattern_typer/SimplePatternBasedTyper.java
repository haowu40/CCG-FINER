package edu.illinois.cs.cogcomp.finer.components.pattern_typer;

import edu.illinois.cs.cogcomp.core.datastructures.Pair;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.finer.components.IFinerTyper;
import edu.illinois.cs.cogcomp.finer.datastructure.FineNerType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Map;

/**
 * Created by haowu4 on 5/15/17.
 */
public class SimplePatternBasedTyper implements IFinerTyper {

    private Map<SimplePattern, List<FineNerType>> allPatterns;
    private int windowSize = 5;

    public static List<SimplePattern> extractAllPattern(Constituent c) {
        throw new NotImplementedException();
    }

    @Override
    public List<Pair<Constituent, List<FineNerType>>> annotate(List<Constituent> mentions) {
        return null;
    }
}
