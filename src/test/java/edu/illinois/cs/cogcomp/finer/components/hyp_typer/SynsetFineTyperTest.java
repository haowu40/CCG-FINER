package edu.illinois.cs.cogcomp.finer.components.hyp_typer;

import edu.illinois.cs.cogcomp.finer.utils.WordNetUtils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by haowu4 on 5/21/17.
 */
public class SynsetFineTyperTest {
    @Test
    public void loadSynsetFineTyper() throws Exception {
        WordNetUtils wordNetUtils = WordNetUtils.getInstance();
        System.out.println(wordNetUtils.getSynsetFromNLTKString("entity.n.01").equals(wordNetUtils.getSynsetFromNLTKString("entity.n.01")));
//        System.out.printf("");
    }

}