package edu.illinois.cs.cogcomp.isa.features;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by haowu4 on 1/19/17.
 */
public class LexiconTest {

    public static Lexicon getLexicon() {
        Lexicon lexicon = new Lexicon();
        lexicon.seeFeature("0");

        for (int i = 1; i < 100; i++) {
            String featureName = String.valueOf(i);

            for (int j = 0; j < i * 3; j++) {
                lexicon.seeFeature(featureName);
            }
        }
        return lexicon;
    }

    @Test
    public void seeFeature() throws Exception {
        Lexicon lexicon = getLexicon();
        assertEquals(9, lexicon.getIdxOrNegativeOne("9"));
        assertEquals(8, lexicon.getIdxOrNegativeOne("8"));
        assertEquals(7, lexicon.getIdxOrNegativeOne("7"));
        assertEquals(6, lexicon.getIdxOrNegativeOne("6"));
        assertEquals(5, lexicon.getIdxOrNegativeOne("5"));
        assertEquals(4, lexicon.getIdxOrNegativeOne("4"));
        assertEquals(3, lexicon.getIdxOrNegativeOne("3"));
        assertEquals(55, lexicon.getIdxOrNegativeOne("55"));
        assertEquals(-1, lexicon.getIdxOrNegativeOne("2255"));

    }

    @Test
    public void trim() throws Exception {
        Lexicon lexicon = getLexicon();
        lexicon.trim(30);
        assertEquals(-1, lexicon.getIdxOrNegativeOne("9"));
        assertEquals(-1, lexicon.getIdxOrNegativeOne("8"));
        assertEquals(-1, lexicon.getIdxOrNegativeOne("7"));
        assertEquals(-1, lexicon.getIdxOrNegativeOne("6"));
        assertEquals(-1, lexicon.getIdxOrNegativeOne("5"));
        assertEquals(-1, lexicon.getIdxOrNegativeOne("4"));
        assertEquals(-1, lexicon.getIdxOrNegativeOne("1"));
        assertEquals(-1, lexicon.getIdxOrNegativeOne("2255"));

        assertTrue(lexicon.getIdxOrNegativeOne("55") > 0);
        assertTrue(lexicon.getIdxOrNegativeOne("55") < 90);

    }


}