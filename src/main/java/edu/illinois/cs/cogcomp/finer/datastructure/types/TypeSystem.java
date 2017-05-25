package edu.illinois.cs.cogcomp.finer.datastructure.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    public static TypeSystem getFromYaml(URL url) throws IOException {

        log.info("Loading type system from {}", url.toString());
        Yaml yaml = new Yaml();
        TypeInfos load = null;
        load = yaml.loadAs(url.openStream(), TypeInfos.class);
        Map<String, FinerType> typeCollection = new HashMap<>();
        Set<FinerType> invisiableTypes = new HashSet<>();
        for (Map.Entry<String, TypeInfo> entry : load.typeInfos.entrySet()) {
            String typeName = entry.getKey();
            TypeInfo info = entry.getValue();
            FinerType type = new FinerType(typeName, info.is_figer_type);
            typeCollection.put(typeName, type);
        }

        for (Map.Entry<String, TypeInfo> entry : load.typeInfos.entrySet()) {
            String typeName = entry.getKey();
            TypeInfo info = entry.getValue();

            FinerType currentType = typeCollection.get(typeName);
            if (!currentType.isVisible()) {
                invisiableTypes.add(currentType);
            }
            String parentName = info.parent;
            if (parentName != null) {
                FinerType parentType = typeCollection.get(parentName);
                parentType.addChildren(currentType);
                currentType.setParent(parentType);
            }
        }

        TypeSystem typeSystem = new TypeSystem(typeCollection, invisiableTypes);

        for (FinerType type : typeCollection.values()) {
            type.setTypeSystem(typeSystem);
        }

        return typeSystem;
    }

    public TypeSystem(Map<String, FinerType> typeCollection, Set<FinerType> invisiableTypes) {
        this.typeCollection = typeCollection;
        this.invisiableTypes = invisiableTypes;
    }

    private Map<String, FinerType> typeCollection;
    private Set<FinerType> invisiableTypes;

    public FinerType getType(String name) {
        return typeCollection.get(name);
    }

}