package edu.illinois.cs.cogcomp.finer.datastructure;

import net.sf.extjwnl.data.Synset;

import java.util.List;

/**
 * Created by haowu4 on 1/15/17.
 */
public class FineNerType {
    String typeName;
    List<Synset> senseDefs;

    public FineNerType(String typeName, List<Synset> senseDefs) {
        this.typeName = typeName;
        this.senseDefs = senseDefs;
    }

    public String getTypeName() {
        return typeName;
    }

    public List<Synset> getSenseDefs() {
        return senseDefs;
    }
}
