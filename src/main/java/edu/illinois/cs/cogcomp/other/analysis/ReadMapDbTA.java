package edu.illinois.cs.cogcomp.other.analysis;

import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation
        .TextAnnotation;
import edu.illinois.cs.cogcomp.core.utilities.SerializationHelper;
import edu.illinois.cs.cogcomp.finer.distribuational_semantic.Extractor;
import edu.illinois.cs.cogcomp.finer.distribuational_semantic.extractors
        .NearestVerbExtractor;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.illinois.cs.cogcomp.finer.utils.ZipUtils.decompress;

/**
 * Created by haowu4 on 1/18/17.
 */
public class ReadMapDbTA {
    public static class Counter {
        Map<String, Integer> counter;

        public Counter() {
            counter = new HashMap<>();
        }

        public void increment(String v) {
            int nc = counter.getOrDefault(v, 0) + 1;
            counter.put(v, nc);
        }

        public void writeToFile(String output) {
            try {
                PrintWriter writer = new PrintWriter(output, "UTF-8");
                for (Map.Entry<String, Integer> entry : counter.entrySet()) {
                    if (entry.getKey().trim().isEmpty()) {
                        continue;
                    }
                    writer.println(String.format("%s\t%d", entry.getKey(), entry
                            .getValue()));
                }
                writer.close();
            } catch (IOException e) {
                // do something
            }
        }
    }

    public DB db;
    public HTreeMap<String, byte[]> store;
    Map<String, Counter> counters;
    List<Extractor> extractors;

    List<String> dbPaths;

    public ReadMapDbTA() {
        counters = new HashMap<>();
        dbPaths = new ArrayList<>();
        extractors = new ArrayList<>();
        extractors.add(new NearestVerbExtractor());
    }

    public void loadDb(String file) {
        System.out.println(" Loading " + file);
        db = DBMaker
                .fileDB(file)
                .closeOnJvmShutdown()
                .make();

        store = db.hashMap("annotated")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.BYTE_ARRAY)
                .open();
    }

    private int progress = 0;

    public void count() {
        for (Map.Entry<String, byte[]> entry : store.getEntries()) {
            progress++;
            if (progress % 1000 == 0) {
                System.out.printf("Progress %d \r", progress);
            }
            byte[] blob = decompress(entry.getValue());
            TextAnnotation ta = SerializationHelper
                    .deserializeTextAnnotationFromBytes(blob);
            List<Constituent> cs = ta.getView("FINER").getConstituents();
            for (Constituent c : cs) {
                for (Extractor extractor : extractors) {
                    List<String> features = extractor.extract(ta, c);
                    for (String label : c.getLabelsToScores().keySet()) {
                        Counter featureCounter = counters.getOrDefault(label,
                                new Counter());
                        for (String feat : features) {
                            featureCounter.increment(String.format("%s=%s",
                                    extractor.getName(), feat));
                        }
                        counters.put(label, featureCounter);
                    }
                }
            }

        }
    }

    public void startReading() {
        String base = "/home/haowu4/data/finer";
//        dbPaths.add(base);
        File[] fs = new File(base).listFiles();
        int count = 0;
        for (File f : fs) {
            dbPaths.add(f.getAbsolutePath());
            count++;
//            if (count > 3) break;
        }

        for (String s : dbPaths) {
            loadDb(s);
            count();
        }
    }

    public void writeResults() {
        System.out.println("Writing outputs.. ");
        String outputBase = "/home/haowu4/data/finer_dist";
        for (String k : counters.keySet()) {
            String filename = String.format("%s/%s.feat_count.tsv",
                    outputBase, k);
            counters.get(k).writeToFile(filename);
        }
    }

    public static void main(String[] args) {
        ReadMapDbTA reader = new ReadMapDbTA();
        reader.startReading();
        reader.writeResults();
    }
}
