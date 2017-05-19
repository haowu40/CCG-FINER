package edu.illinois.cs.cogcomp.finer.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by haowu4 on 5/15/17.
 */
public class TypeSystem {
    // IO related helper class.
    private static class TypeInfo {
        String parent;
        boolean is_figer_type;
    }

    private static class TypeInfos {
        Map<String, TypeInfo> typeInfos;
    }

    private final static Logger log = LoggerFactory.getLogger(TypeSystem.class);

    private TypeSystem(Map<String, FinerType> typeCollection) {
        this.typeCollection = typeCollection;
    }

    private Map<String, FinerType> typeCollection;

    public FinerType getType(String name) {
        return typeCollection.get(name);
    }

    public static TypeSystem getFromYaml(URL url) throws IOException {

        log.info("Loading type system from {}", url.toString());
        Yaml yaml = new Yaml();
        TypeInfos load = null;
        load = yaml.loadAs(url.openStream(), TypeInfos.class);
        Map<String, FinerType> typeCollection = new HashMap<>();

        for (Map.Entry<String, TypeInfo> entry : load.typeInfos.entrySet()) {
            String typeName = entry.getKey();
            TypeInfo info = entry.getValue();
            FinerType type = new FinerType(typeName);
            typeCollection.put(typeName, type);
        }

        for (Map.Entry<String, TypeInfo> entry : load.typeInfos.entrySet()) {
            String typeName = entry.getKey();
            TypeInfo info = entry.getValue();

            FinerType currentType = typeCollection.get(typeName);

            String parentName = info.parent;
            if (parentName != null) {
                FinerType parentType = typeCollection.get(parentName);
                parentType.addChildren(currentType);
                currentType.setParent(parentType);
            }
        }

        TypeSystem typeSystem = new TypeSystem(typeCollection);

        for (FinerType type : typeCollection.values()) {
            type.setTypeSystem(typeSystem);
        }

        return typeSystem;
    }


}
