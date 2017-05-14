package edu.illinois.cs.cogcomp.finer.datastructure;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by hao on 1/17/17.
 */
public class BaseTypes {

    public static Map<String, Set<String>> maps;

    static {
        maps = new HashMap<>();
        try {
            FileUtils.
                    readLines(
                            new File(
                                    "resources/ontonote_type_to_figer_type"),
                            Charset.defaultCharset()).forEach(s -> {
                s = s.trim();
                if (s.isEmpty()) return;
                String[] parts = s.split("\\t");
                Set<String> set = new HashSet<String>();
                if (parts[1].contains(" ")) {
                    for (String k : parts[1].split(" ")) {
                        set.add(k);
                    }
                } else {
                    set.add(parts[1]);
                }

                maps.put(parts[0], set);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String toFineType(String t) {
        if (maps.containsKey(t)){
            return maps.get(t).iterator().next();
        }else {
            System.err.println("UNK type: " + t);
            return t;
        }

    }

    public static boolean typeMatches(String finerType, String baseTypes) {
        String finebase = finerType;
        if (finerType.contains(".")) {
            finebase = finerType.split("\\.")[0];
        }

        return maps.containsKey(baseTypes) && maps.get(baseTypes).contains(finebase);

    }
}
