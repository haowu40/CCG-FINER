package edu.illinois.cs.cogcomp.turk_related;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.illinois.cs.cogcomp.annotation.AnnotatorException;
import edu.illinois.cs.cogcomp.annotation.AnnotatorServiceConfigurator;
import edu.illinois.cs.cogcomp.annotation.BasicAnnotatorService;
import edu.illinois.cs.cogcomp.core.datastructures.ViewNames;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.SpanLabelView;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.TextAnnotation;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.View;
import edu.illinois.cs.cogcomp.core.utilities.configuration.Configurator;
import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;
import edu.illinois.cs.cogcomp.pipeline.common.PipelineConfigurator;
import edu.illinois.cs.cogcomp.pipeline.main.PipelineFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by haowu4 on 4/3/17.
 */
public class AnnotateExtraDoc {

    public static class TypeAndDoc {
        String text;
        String type;
        String url;
    }

    public static class TypeAndDocs {
        List<TypeAndDoc> out;
    }

    public static class IntPair {
        int start;
        int end;
        String type;
    }

    public static IntPair makeIntPair(int s, int e) {
        IntPair ip = new IntPair();
        ip.start = s;
        ip.end = e;
        return ip;
    }

    public static IntPair makeIntPair(int s, int e, String t) {
        IntPair ip = new IntPair();
        ip.start = s;
        ip.end = e;
        ip.type = t;
        return ip;
    }

    public static class TextAndMention {
        String docId;
        String[] tokens;
        List<IntPair> mentions;
        List<IntPair> sentences;
    }

    public static List<IntPair> getMentions(View v) {
        List<IntPair> ret = new ArrayList<>();
        for (Constituent c : v.getConstituents()) {
            IntPair i = new IntPair();
            i.start = c.getStartSpan();
            i.end = c.getEndSpan();
            i.type = c.getLabel();
        }
        return ret;
    }

    public static void main(String[] args) throws IOException,
            AnnotatorException {

        String LOCATION = "/tmp/text_by_type.json";

        String lines = FileUtils.readFileToString(new File(LOCATION), "UTF-8");

        Gson gson = new GsonBuilder().create();

        TypeAndDocs out = gson.fromJson(lines, TypeAndDocs.class);


        // Reading docs

        Properties props = new Properties();
        props.setProperty(PipelineConfigurator.USE_POS.key,
                Configurator.TRUE);
        props.setProperty(PipelineConfigurator.USE_LEMMA.key,
                Configurator.TRUE);
        props.setProperty(PipelineConfigurator.USE_SHALLOW_PARSE.key,
                Configurator.TRUE);

        props.setProperty(PipelineConfigurator.USE_NER_CONLL.key,
                Configurator.TRUE);
        props.setProperty(PipelineConfigurator.USE_NER_ONTONOTES.key,
                Configurator.TRUE);

        props.setProperty(PipelineConfigurator.USE_STANFORD_PARSE.key,
                Configurator.FALSE);
        props.setProperty(PipelineConfigurator.USE_STANFORD_DEP.key,
                Configurator.FALSE);

        props.setProperty(PipelineConfigurator.USE_SRL_VERB.key,
                Configurator.FALSE);
        props.setProperty(PipelineConfigurator.USE_SRL_NOM.key,
                Configurator.FALSE);
        props.setProperty(
                PipelineConfigurator.THROW_EXCEPTION_ON_FAILED_LENGTH_CHECK.key,
                Configurator.FALSE);
        props.setProperty(
                PipelineConfigurator.USE_JSON.key,
                Configurator.FALSE);
        props.setProperty(
                PipelineConfigurator.USE_LAZY_INITIALIZATION.key,
                Configurator.TRUE);
        props.setProperty(
                PipelineConfigurator.USE_SRL_INTERNAL_PREPROCESSOR.key,
                Configurator.FALSE);


        props.setProperty(AnnotatorServiceConfigurator.DISABLE_CACHE.key,
                Configurator.TRUE);
        props.setProperty(AnnotatorServiceConfigurator.CACHE_DIR.key,
                "/tmp/cogcomp");
        props.setProperty(
                AnnotatorServiceConfigurator.THROW_EXCEPTION_IF_NOT_CACHED.key,
                Configurator.FALSE);
        props.setProperty(
                AnnotatorServiceConfigurator.FORCE_CACHE_UPDATE.key,
                Configurator.FALSE);
        BasicAnnotatorService processor = null;
        try {
            processor = PipelineFactory
                    .buildPipeline(new ResourceManager(props));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AnnotatorException e) {
            e.printStackTrace();
        }

        if (processor == null) {
            System.exit(1);
        }

        List<TextAndMention> outj = new ArrayList<>();
        for (TypeAndDoc doc : out.out) {
            TextAnnotation ta = processor.createAnnotatedTextAnnotation("", "", doc.text);
            List<IntPair> ms = new ArrayList<>();
            View pos = ta.getView(ViewNames.POS);
            for (Constituent c : pos.getConstituents()) {
                if (!c.getLabel().equals("PRP")) {
                    continue;
                }
                IntPair i = new IntPair();
                i.start = c.getStartSpan();
                i.end = c.getEndSpan();
                i.type = c.getLabel();
                ms.add(i);
            }
            View conll = ta.getView(ViewNames.NER_CONLL);

            for (Constituent c : conll.getConstituents()) {
                IntPair i = new IntPair();
                i.end = c.getEndSpan();
                i.start = c.getStartSpan();
                i.type = "CONLL-" + c.getLabel();
                ms.add(i);
            }

            View ontonote = ta.getView(ViewNames.NER_ONTONOTES);

            for (Constituent c : ontonote.getConstituents()) {
                IntPair i = new IntPair();
                i.start = c.getStartSpan();
                i.end = c.getEndSpan();
                i.type = "Ontonote-" + c.getLabel();
                ms.add(i);
            }
            TextAndMention tm = new TextAndMention();
            tm.docId = doc.url;
            tm.tokens = ta.getTokens();
            tm.mentions = ms;
            tm.sentences = ta.sentences().stream().map(s -> makeIntPair(s.getStartSpan(), s.getEndSpan(), "Sent")).collect(Collectors.toList());
            outj.add(tm);
        }

        String s = gson.toJson(outj);
        FileUtils.writeStringToFile(new File("/tmp/with_mention.json"), s);

        System.out.println("Done");

    }
}
