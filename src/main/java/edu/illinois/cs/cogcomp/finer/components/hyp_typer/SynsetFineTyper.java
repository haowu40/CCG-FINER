package edu.illinois.cs.cogcomp.finer.components.hyp_typer;

import edu.illinois.cs.cogcomp.finer.datastructure.types.FinerType;
import edu.illinois.cs.cogcomp.finer.utils.WordNetUtils;
import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.Pointer;
import net.sf.extjwnl.data.PointerType;
import net.sf.extjwnl.data.Synset;

import java.util.*;

/**
 * Created by haowu4 on 5/21/17.
 */
public class SynsetFineTyper {
    Map<String, Set<String>> type_to_positive_type;
    Map<String, Set<String>> type_to_negative_type;

    public Synset ENTITY;

    WordNetUtils wordNetUtils;

    public SynsetFineTyper() throws JWNLException {
        try {
            wordNetUtils = WordNetUtils.getInstance();
        } catch (JWNLException e) {
            e.printStackTrace();
        }

        ENTITY = wordNetUtils.getSynsetFromNLTKString("entity.n.01");
    }

    public List<FinerType> get_fine_types(Synset synset) throws JWNLException {
        List<FinerType> ret = new ArrayList<>();
        Queue<Synset> queue = new LinkedList<>();
        queue.add(synset);
        for (Pointer hypPointer : wordNetUtils.getPointers(synset, PointerType.HYPERNYM)) {
            Synset c = hypPointer.getTargetSynset();

        }
        return ret;
    }


    public static SynsetFineTyper loadSynsetFineTyper() {
        return null;
    }
}
