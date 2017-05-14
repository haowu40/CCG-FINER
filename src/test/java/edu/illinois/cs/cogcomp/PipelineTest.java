package edu.illinois.cs.cogcomp;

import edu.illinois.cs.cogcomp.annotation.AnnotatorException;
import edu.illinois.cs.cogcomp.annotation.AnnotatorServiceConfigurator;
import edu.illinois.cs.cogcomp.annotation.BasicAnnotatorService;
import edu.illinois.cs.cogcomp.core.datastructures.ViewNames;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation
        .TextAnnotation;
import edu.illinois.cs.cogcomp.core.utilities.configuration.Configurator;
import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;
import edu.illinois.cs.cogcomp.pipeline.main.PipelineFactory;
import edu.illinois.cs.cogcomp.wsd.annotators.WordSenseAnnotator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * Created by haowu4 on 1/20/17.
 */
public class PipelineTest {

    public static BasicAnnotatorService getPipeline() throws IOException,
            AnnotatorException {
        Properties props = new Properties();
        props.setProperty("usePos", Configurator.TRUE);
        props.setProperty("useLemma",
                Configurator.FALSE);
        props.setProperty("useShallowParse",
                Configurator.FALSE);

        props.setProperty("useNerConll",
                Configurator.TRUE);
        props.setProperty("useNerOntonotes",
                Configurator.FALSE);
        props.setProperty("useStanfordParse",
                Configurator.FALSE);
        props.setProperty("useStanfordDep",
                Configurator.FALSE);

        props.setProperty("useSrlVerb",
                Configurator.FALSE);
        props.setProperty("useSrlNom",
                Configurator.FALSE);
        props.setProperty(
                "throwExceptionOnFailedLengthCheck",
                Configurator.FALSE);
        props.setProperty(
                "useJson",
                Configurator.FALSE);
        props.setProperty(
                "isLazilyInitialized",
                Configurator.FALSE);
//        props.setProperty(
//                PipelineConfigurator.USE_SRL_INTERNAL_PREPROCESSOR.key,
//                Configurator.FALSE);


        props.setProperty(AnnotatorServiceConfigurator.DISABLE_CACHE.key,
                Configurator.TRUE);
        props.setProperty(AnnotatorServiceConfigurator.CACHE_DIR.key,
                "cache/db");
        props.setProperty(
                AnnotatorServiceConfigurator.THROW_EXCEPTION_IF_NOT_CACHED.key,
                Configurator.FALSE);
        props.setProperty(
                AnnotatorServiceConfigurator.FORCE_CACHE_UPDATE.key,
                Configurator.FALSE);

        ResourceManager resourceManager = new ResourceManager(props);
        BasicAnnotatorService processor = PipelineFactory
                .buildPipeline(new ResourceManager(props));

        return processor;
    }

    public static void main(String[] args) throws IOException,
            AnnotatorException {
        String file = "/home/haowu4/data/1blm/text/train/news" +
                ".en-00001-of-00100";
        int limit = 20000;
        List<String> texts = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int counter = 0;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = br.readLine()) != null) {
                counter++;
                if (counter == limit) break;
                stringBuffer.append(line);
                stringBuffer.append("\n");
                if (counter % 100 == 0) {
                    texts.add(stringBuffer.toString());
                    stringBuffer = new StringBuffer();
                }
            }
        }


        List<TextAnnotation> tas = new ArrayList<>();
        BasicAnnotatorService bas = getPipeline();
        System.out.println("Starting it... ");
        long curr = System.currentTimeMillis();
        for (String l : texts) {
            TextAnnotation ta = bas.createAnnotatedTextAnnotation("", "", l);
            tas.add(ta);
        }
        long end = System.currentTimeMillis();

        System.out.println(end - curr);
        System.out.println(tas.get(tas.size() - 6));
    }
}
