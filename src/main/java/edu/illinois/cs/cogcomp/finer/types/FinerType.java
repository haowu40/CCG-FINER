package edu.illinois.cs.cogcomp.finer.types;

import org.apache.commons.lang.NotImplementedException;

import java.util.List;

/**
 * Created by haowu4 on 5/15/17.
 */
public class FinerType {
    private TypingSystem typingSystem;
    private String type;
    private FinerType parent;
    private List<FinerType> children;

    FinerType(TypingSystem typingSystem, String type, FinerType parent, List<FinerType> children) {
        this.typingSystem = typingSystem;
        this.type = type;
        this.parent = parent;
        this.children = children;
    }

    public boolean isParentOf(FinerType t) {
        throw new NotImplementedException();
    }

    public boolean isChildOf(FinerType t) {
        throw new NotImplementedException();
    }

    public boolean isCoarseType(FinerType t) {
        throw new NotImplementedException();
    }

    public List<String> wordNetSenseIds() {
        throw new NotImplementedException();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FinerType finerType = (FinerType) o;

        return type.equals(finerType.type);
    }


    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
