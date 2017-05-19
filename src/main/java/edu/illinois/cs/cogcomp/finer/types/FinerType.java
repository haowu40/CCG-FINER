package edu.illinois.cs.cogcomp.finer.types;

import org.apache.commons.lang.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haowu4 on 5/15/17.
 */
public class FinerType {
    private TypeSystem typeSystem;
    private String type;
    private FinerType parent;
    private List<FinerType> children;
    private List<String> wordnetIds;

    FinerType(String name) {
        this.typeSystem = null;
        this.type = name;
        this.parent = null;
        this.children = new ArrayList<>();
        this.wordnetIds = new ArrayList<>();
    }

    public TypeSystem getTypeSystem() {
        return typeSystem;
    }

    public void setTypeSystem(TypeSystem typeSystem) {
        this.typeSystem = typeSystem;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FinerType getParent() {
        return parent;
    }

    public void setParent(FinerType parent) {
        this.parent = parent;
    }

    public List<FinerType> getChildren() {
        return children;
    }

    public void addChildren(FinerType child) {
        this.children.add(child);
    }

    public boolean isParentOf(FinerType t) {
        throw new NotImplementedException();
    }

    public boolean isChildOf(FinerType t) {
        throw new NotImplementedException();
    }

    public boolean isCoarseType(FinerType t) {
        return this.parent == null;
    }

    public List<String> wordNetSenseIds() {
        return this.wordnetIds;
    }

    public void addWordNetSenseId(String senseId) {
        this.wordnetIds.add(senseId);
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
