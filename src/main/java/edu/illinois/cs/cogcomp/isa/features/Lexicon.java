package edu.illinois.cs.cogcomp.isa.features;

import edu.illinois.cs.cogcomp.isa.math.SparseVector;
import edu.illinois.cs.cogcomp.util.SerializationUtil;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.*;

/**
 * Created by hao on 1/19/17.
 */
public class Lexicon implements Serializable {
    private TObjectIntMap<String> lex;
    private int curr;

    private transient TObjectIntMap<String> counter;

    public Lexicon() {
        this.lex = new TObjectIntHashMap<>();
        curr = 0;
        counter = new TObjectIntHashMap<>();
    }

    public void seeFeature(String s) {
        if (!lex.containsKey(s)) {
            lex.put(s, curr);
            curr++;
        }
        if (counter != null) {
            counter.adjustOrPutValue(s, 1, 1);
        }

    }

    public void trim(int minCount) {
        int c = 0;
        TObjectIntMap<String> trimed = new TObjectIntHashMap<>();
        TObjectIntIterator<String> it = counter.iterator();
        while (it.hasNext()) {
            it.advance();
            String idx = it.key();
            int v = it.value();
            if (v >= minCount) {
                trimed.put(idx, c);
                c++;
            }
        }
        this.lex = trimed;
    }

    public int getIdxOrNegativeOne(String s) {
        if (lex.containsKey(s))
            return lex.get(s);
        else {
            return -1;
        }
    }

    public void addToVector(SparseVector sparseVector, String f, float v) {
        int idx = this.getIdxOrNegativeOne(f);
        if (idx == -1) {
            return;
        } else {
            sparseVector.addEntry(idx, v);
        }
    }

    public static Lexicon openExistingLexiconOrNull(String file) throws
            IOException, ClassNotFoundException {
        File input = new File(file);
        if (input.exists()) {
            return (Lexicon) SerializationUtil.deserialize(file);
        } else {
            return null;
        }
    }

    public static void dump(Lexicon lexicon, String file) {
        try {
            SerializationUtil.serialize(lexicon, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
