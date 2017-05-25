package edu.illinois.cs.cogcomp.lbjava.learn;

import edu.illinois.cs.cogcomp.annotation.AnnotatorException;
import edu.illinois.cs.cogcomp.annotation.AnnotatorServiceConfigurator;
import edu.illinois.cs.cogcomp.annotation.BasicAnnotatorService;
import edu.illinois.cs.cogcomp.annotation.TextAnnotationBuilder;
import edu.illinois.cs.cogcomp.core.datastructures.ViewNames;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation
        .TextAnnotation;
import edu.illinois.cs.cogcomp.core.datastructures.vectors.OVector;
import edu.illinois.cs.cogcomp.core.utilities.configuration.Configurator;
import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;
import edu.illinois.cs.cogcomp.finer.ner_test.NERAnnotatorPub;
import edu.illinois.cs.cogcomp.lbjava.learn.SparseAveragedPerceptron;
import edu.illinois.cs.cogcomp.ner.NERAnnotator;
import edu.illinois.cs.cogcomp.nlp.tokenizer.StatefulTokenizer;
import edu.illinois.cs.cogcomp.nlp.utility.TokenizerTextAnnotationBuilder;
import edu.illinois.cs.cogcomp.pipeline.main.PipelineFactory;
import edu.illinois.cs.cogcomp.wsd.annotators.WordSenseAnnotator;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


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
        TextAnnotationBuilder tab;
        // don't split on hyphens, as NER models are trained this way
        tab = new TokenizerTextAnnotationBuilder(new StatefulTokenizer());
        String file = "/home/haowu4/data/1blm/text/train/news.en-00001-of-00100";


        Scanner s=new Scanner(System.in);

        System.out.println(" Press enter to load model.....");
        s.nextLine();
        System.out.println(" Start load model.....");


//        BasicAnnotatorService bas = getPipeline();
        NERAnnotatorPub co = new NERAnnotatorPub(ViewNames.NER_ONTONOTES);
        co.initialize(new ResourceManager(new Properties()));
        TextAnnotation ta = tab.createTextAnnotation("", "", "this is a example.");
        co.getView(ta);
        System.out.println(" Load model Done.....");

//        OVector ov = co.t1.getNetwork();
//        for (int i = 0; i < ov.size(); i++) {
//            SparseAveragedPerceptron svp = (SparseAveragedPerceptron) ov.get(i);
//            double[] d = svp.awv.averagedWeights.toArray();
//            ;
//            try (PrintWriter out = new PrintWriter("/tmp/vecs/" + i + ".txt")) {
//                for (double v : d) {
//                    out.write(String.valueOf(v));
//                    out.write("\n");
//                }
//            }
//
//
//        }
//        System.out.println(ov.size());

        System.out.println("[L109] Press enter to Annotate Docs .....");
        s.nextLine();
        System.out.println(" Start annotate data.....");


        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int c = 0;
            while ((line = br.readLine()) != null) {
                ta = tab.createTextAnnotation("", "", line);
                co.getView(ta);
                c++;
                if (c == 10000){
                    break;
                }
            }
        }
        System.out.println(" Annotation Done.....");
        System.out.println("[L130] Press enter to Exit .....");
        s.nextLine();



    }
}
