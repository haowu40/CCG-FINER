package edu.illinois.cs.cogcomp.finer.eval;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.illinois.cs.cogcomp.annotation.AnnotatorException;
import edu.illinois.cs.cogcomp.annotation.BasicAnnotatorService;
import edu.illinois.cs.cogcomp.annotation.BasicTextAnnotationBuilder;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.*;
import edu.illinois.cs.cogcomp.finer.FinerAnnotator;
import edu.illinois.cs.cogcomp.finer.components.MentionDetecter;
import edu.illinois.cs.cogcomp.finer.entry.WebInterface;
import edu.illinois.cs.cogcomp.finer.utils.PipelineUtils;
import edu.illinois.cs.cogcomp.finer.utils.StringUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static edu.illinois.cs.cogcomp.finer.utils.PipelineUtils.getPipeline;

/**
 * Created by hao on 1/22/17.
 */
public class FigerEval {

    public static class FigerMentions {
        int start;
        int end;
        String predicted;
        String label;
        String[] tokens;
        String[] surface;
    }

    public static List<TextAnnotation> loadFigerData() throws IOException, AnnotatorException {
        Gson gson = new GsonBuilder().create();
        List<String> jsontext = FileUtils.readLines(new File
                ("/home/haowu4/data/figer/ontonote_annotated_line.json"),
                Charset.defaultCharset());
        List<FigerMentions> lfs = jsontext.stream()
                .filter(t -> !t.isEmpty())
                .map(t -> gson.fromJson(t, FigerMentions.class))
                .collect(Collectors.toList());

        BasicAnnotatorService bas = getPipeline();
        Map<String, List<FigerMentions>> ms = new HashMap<>();
        for (FigerMentions fm : lfs) {
            String key = String.join(" ", fm.tokens);
            List<FigerMentions> fms = ms.getOrDefault(key, new ArrayList<>());
            fms.add(fm);
            ms.put(key, fms);
        }
        List<TextAnnotation> tas = new ArrayList<>();
        for (String sentence : ms.keySet()) {
            List<FigerMentions> fms = ms.get(sentence);
            List<String[]> sens = new ArrayList<>();
            sens.add(fms.get(0).tokens);
            TextAnnotation ta = BasicTextAnnotationBuilder.createTextAnnotationFromTokens("", "", sens);
            bas.annotateTextAnnotation(ta, false);
            View tvs = new SpanLabelView("ONTONOTE-TYPE", ta);
            for (FigerMentions fm : fms) {
                tvs.addConstituent(new Constituent(fm.predicted, "ONTONOTE-TYPE", ta, fm.start, fm.end));
            }
            ta.addView("ONTONOTE-TYPE", tvs);
            tas.add(ta);
        }

        return tas;
    }

    public static void main(String[] args) throws Exception {
        List<TextAnnotation> tas = loadFigerData();
        FinerAnnotator finerAnnotator = new FinerAnnotator(PipelineUtils
                .readFinerTypes("resources/type_to_wordnet_senses.txt"));

        finerAnnotator.setMentionDetecter(sentence -> {
            View ner = sentence.getView("ONTONOTE-TYPE");
            List<Constituent> ret = new ArrayList<>();
            for (Constituent c : ner.getConstituents()) {
                Constituent mention = new Constituent("mention", "finer-mention", c
                        .getTextAnnotation
                                (), c.getStartSpan(), c.getEndSpan());
                mention.addAttribute("coarse-type", c.getLabel());
                ret.add(mention);
            }
            return ret;
        });

        Gson gson = new GsonBuilder().create();

        List<WebInterface.AnnotationResult> rs = new ArrayList<>();

        for (TextAnnotation ta : tas) {
            View v = finerAnnotator.annotateByHypernymModel(ta);
            WebInterface.AnnotationResult ret = WebInterface.getResult(v);
            rs.add(ret);
        }

        String o = gson.toJson(rs);
        FileUtils.write(new File("/tmp/output"), o);
    }


}
