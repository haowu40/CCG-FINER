package edu.illinois.cs.cogcomp.finer.types;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;

/**
 * Created by haowu4 on 5/15/17.
 */
public class TypingSystem {
    private Map<String, FinerType> typeCollection;

    public FinerType getType(String name) {
        return typeCollection.get(name);
    }

    public static TypingSystem getFromYaml(String file) {
        throw new NotImplementedException();
    }

}
