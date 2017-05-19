package edu.illinois.cs.cogcomp.finer.components.kb_typer;

import edu.illinois.cs.cogcomp.core.datastructures.Pair;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.finer.components.IFinerTyper;
import edu.illinois.cs.cogcomp.finer.datastructure.FineNerType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by haowu4 on 5/15/17.
 */
public class SimpleKBBiasTyper implements IFinerTyper {
    Map<String, List<FineNerType>> surfaceToTypeDB;

    public SimpleKBBiasTyper(Map<String, List<FineNerType>> surfaceToTypeDB) {
        this.surfaceToTypeDB = surfaceToTypeDB;
    }

    public static SimpleKBBiasTyper constructSimpleKBBiasTyperFromTextInput(BufferedReader reader) {
        throw new NotImplementedException();
    }

    public List<FineNerType> annotateSingleMention(Constituent mention) {
        String surface = mention.getSurfaceForm();
        return surfaceToTypeDB.getOrDefault(surface, new ArrayList<>());
    }

    @Override
    public List<Pair<Constituent, List<FineNerType>>> annotate(List<Constituent> mentions) {
        List<Pair<Constituent, List<FineNerType>>> ret = new ArrayList<>();
        for (Constituent mention : mentions) {
            List<FineNerType> annotated = annotateSingleMention(mention);
            ret.add(new Pair<>(mention, annotated));
        }
        return ret;
    }
}
