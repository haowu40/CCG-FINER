package edu.illinois.cs.cogcomp;

import edu.illinois.cs.cogcomp.annotation.AnnotatorException;
import edu.illinois.cs.cogcomp.annotation.BasicAnnotatorService;
import edu.illinois.cs.cogcomp.annotation.BasicTextAnnotationBuilder;
import edu.illinois.cs.cogcomp.annotation.TextAnnotationBuilder;
import edu.illinois.cs.cogcomp.core.datastructures.ViewNames;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation
        .TextAnnotation;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.View;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static edu.illinois.cs.cogcomp.finer.utils.PipelineUtils.getPipeline;

/**
 * Created by haowu4 on 1/31/17.
 */
public class TestFiger {

    public static void printColumn(TextAnnotation ta) {
        View view = ta.getView(ViewNames.NER_ONTONOTES);
        String[] tokens = ta.getTokens();
        String[] bios = new String[tokens.length];
        String[] types = new String[tokens.length];

        for (int i = 0; i < tokens.length; i++) {
            bios[i] = "O";
            types[i] = null;
        }
        for (Constituent c : view.getConstituents()) {
            for (int j = c.getStartSpan(); j < c.getEndSpan(); j++) {
                bios[j] = "I";
                types[j] = c.getLabel();
            }

            bios[c.getStartSpan()] = "B";
        }

        for (int i = 0; i < tokens.length; i++) {
            String label = "O";
            if (null != types[i]) {
                label = String.format("%s-%s", bios[i], types[i]);
            }
            System.out.println(String.format("%s\t%s", tokens[i], label));
        }
    }

    public static List<List<String>> loadFigers(String file) throws
            IOException, AnnotatorException {
        LineIterator lt = FileUtils.lineIterator(new File(file));
        BasicAnnotatorService bas = getPipeline();
        List<String> sentences = new ArrayList<>();
        List<TextAnnotation> tas = new ArrayList<>();
        while (lt.hasNext()) {
            String line = lt.next().trim();
            if (line.isEmpty()) {
                List<String[]> doc = new ArrayList<>();
                String[] x = new String[sentences.size()];
                for (int i = 0; i < sentences.size(); i++) {
                    x[i] = sentences.get(i);
                }
                doc.add(x);
                TextAnnotation ta = BasicTextAnnotationBuilder
                        .createTextAnnotationFromTokens(doc);
                ta = bas.annotateTextAnnotation(ta, false);
                tas.add(ta);
                printColumn(ta);
                System.out.println("");
                sentences = new ArrayList<>();
            } else {
                String w = line.split("\t")[0];
                sentences.add(w);
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException,
            AnnotatorException {
        loadFigers("/home/haowu4/codes/dataless_finer/eval_figer/data/gold_cleaned_coarse.label");
    }
}
