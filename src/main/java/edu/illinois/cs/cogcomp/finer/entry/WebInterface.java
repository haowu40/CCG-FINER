package edu.illinois.cs.cogcomp.finer.entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.illinois.cs.cogcomp.annotation.AnnotatorException;
import edu.illinois.cs.cogcomp.annotation.BasicAnnotatorService;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Relation;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation
        .TextAnnotation;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.View;
import edu.illinois.cs.cogcomp.finer.FinerAnnotator;
import edu.illinois.cs.cogcomp.finer.utils.PipelineUtils;
import net.sf.extjwnl.JWNLException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static edu.illinois.cs.cogcomp.finer.datastructure.BaseTypes.toFineType;
import static edu.illinois.cs.cogcomp.finer.utils.PipelineUtils.getPipeline;
import static spark.Spark.*;

/**
 * Created by haowu4 on 1/15/17.
 */
public class WebInterface {
    private static final Gson GSON = new GsonBuilder().create();
    private BasicAnnotatorService bas;
    private FinerAnnotator finerAnnotator;

    public WebInterface(BasicAnnotatorService bas, FinerAnnotator
            finerAnnotator) {
        this.bas = bas;
        this.finerAnnotator = finerAnnotator;
    }

    private TextAnnotation preprocessText(String d) throws AnnotatorException {
        return bas.createAnnotatedTextAnnotation("", "", d);
    }

    private View annotate(String d) throws AnnotatorException {
        TextAnnotation ta = preprocessText(d);
        View finer = finerAnnotator.annotateByHypernymModel(ta);
        return finer;
    }

    private static WebInterface web;

    public static class AnnotationRequest {
        String text;
    }

    public static class Mention {
        int start;
        int end;
        Map<String, Double> label;
        String info;

        public Mention(int start, int end, Map<String, Double> label) {
            this.start = start;
            this.end = end;
            this.label = label;
        }
    }

    public static class TriggerEdge {
        public Mention trigger;
        public Mention mention;

        public TriggerEdge(Mention trigger, Mention mention) {
            this.trigger = trigger;
            this.mention = mention;
        }
    }

    public static class AnnotationResult {
        String[] tokens;
        List<Mention> wsds;
        List<Mention> mentions;
        List<Mention> triggers;

        List<TriggerEdge> edges;

        public AnnotationResult(String[] tokens, List<Mention> wsds,
                                List<Mention> mentions, List<Mention>
                                        triggers, List<TriggerEdge>
                                        triggerEdges) {
            this.tokens = tokens;
            this.wsds = wsds;
            this.mentions = mentions;
            this.triggers = triggers;
            this.edges = triggerEdges;
        }
    }

    public static AnnotationResult getResult(View view) {
        return getResult(view, false);
    }


    public static AnnotationResult getResult(View view, boolean includeWsd) {
        List<Mention> mentions = new ArrayList<>();
        List<Mention> triggers = new ArrayList<>();
        List<TriggerEdge> triggerEdges = new ArrayList<>();
        for (Constituent c : view.getConstituents()) {
            if (c.getAttribute("type").equals("mention")) {
//                Map<String, Double> lbs = new HashMap<>();
//                try {
//                    for (Map.Entry<String, Double> e : c.getLabelsToScores().entrySet()) {
//                        lbs.put(toFineType(e.getKey()), e.getValue());
//                    }
//                } catch (NullPointerException e) {
//                    System.err.println(c);
//                    lbs.put(toFineType(c.getLabel()), 1.0);
//                }

                Mention m =
                        new Mention(c.getStartSpan(), c.getEndSpan(), null);
                m.label = new HashMap<>();
                m.label.put(toFineType(c.getAttribute("coarse-type")), 1.0);
                mentions.add(m);
            } else {
                triggers.add(new Mention(c.getStartSpan(), c.getEndSpan(), c
                        .getLabelsToScores()));
            }
        }

        for (Relation r : view.getRelations()) {
            Mention from = new Mention(r.getSource().getStartSpan(), r
                    .getSource().getEndSpan(), r.getSource()
                    .getLabelsToScores());
            Mention to = new Mention(r.getTarget().getStartSpan(), r
                    .getTarget().getEndSpan(), r.getTarget()
                    .getLabelsToScores());
            triggerEdges.add(new TriggerEdge(from, to));
        }
        List<Mention> wsds = new ArrayList<>();
        if (includeWsd) {
            wsds = view.getTextAnnotation().getView("SENSE")
                    .getConstituents().stream()
                    .map(c -> new Mention(c.getStartSpan(), c.getEndSpan(), c
                            .getLabelsToScores())).collect(Collectors.toList());

        }

        return new AnnotationResult(view.getTextAnnotation().getTokens(), wsds,
                mentions, triggers, triggerEdges);
    }

    public static final List<String> samples = new ArrayList<>();

    public static void main(String[] args) throws IOException,
            AnnotatorException, JWNLException {
        BasicAnnotatorService processor = getPipeline();

        FinerAnnotator finerAnnotator = new FinerAnnotator(PipelineUtils
                .readFinerTypes("resources/type_to_wordnet_senses.txt"));
        WebInterface webInterface = new WebInterface(processor, finerAnnotator);
        processor.createAnnotatedTextAnnotation("", "", "This is a test " +
                "sentence");
        externalStaticFileLocation("web");

        String sentence = "Not content with bringing Rocky back to cinema " +
                "screens. Another Stallone character Vietnam vet " +
                "John Rambo is coming out of hibernation , 19 years after " +
                "the third film in the series .";

        samples.add(sentence);

        samples.addAll(FileUtils.readLines(new File("sample_data/annotation" +
                ".txt")));

        Random random = new Random();
        get("/random", (request, response) -> {
            int i = random.nextInt(samples.size());
            return samples.get(i);
        });

        get("/rambo", (request, response) -> {
            return sentence;
        });

        post("/annotate", (request, response) -> {
            String body = request.body();
            AnnotationRequest annotationRequest = GSON.fromJson(body,
                    AnnotationRequest.class);
            View v = webInterface.annotate(annotationRequest.text);
            return GSON.toJson(getResult(v));
        });
    }


}
