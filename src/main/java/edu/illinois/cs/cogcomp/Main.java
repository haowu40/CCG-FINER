package edu.illinois.cs.cogcomp;

import edu.illinois.cs.cogcomp.annotation.AnnotatorException;
import edu.illinois.cs.cogcomp.annotation.AnnotatorServiceConfigurator;
import edu.illinois.cs.cogcomp.annotation.BasicAnnotatorService;
import edu.illinois.cs.cogcomp.core.datastructures.ViewNames;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.SpanLabelView;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation
        .TextAnnotation;
import edu.illinois.cs.cogcomp.core.utilities.configuration.Configurator;
import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;
import edu.illinois.cs.cogcomp.pipeline.common.PipelineConfigurator;
import edu.illinois.cs.cogcomp.pipeline.main.PipelineFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by haowu4 on 1/9/17.
 */
public class Main {

    public static void visualizeView(TextAnnotation ta, String viewName) {
        // prints the contents of a text annotation with bracketed notation for spanlabel views with sorted mutually exclusive spans.
        SpanLabelView view = (SpanLabelView) ta.getView(viewName);
        Iterator<Constituent> viewIterator = view.getConstituents().iterator();
        Constituent constituent = null;
        String[] tokens = ta.getTokens();
        StringBuilder sb = new StringBuilder();
        //System.out.println("entered show view");
        for (int tokenId = 0; tokenId < tokens.length; tokenId++) {
            if (constituent == null && viewIterator.hasNext())
                constituent = viewIterator.next();
            if (constituent != null && constituent.getStartSpan() == tokenId)
                sb.append("[ ");
            sb.append(tokens[tokenId]).append(" ");
            if (constituent != null && constituent.getEndSpan() - 1 == tokenId) {
                sb.append(constituent.getLabel()).append("] ");
                constituent = null;
            }
        }
        //System.out.println("done");
        System.out.println(sb.toString());
    }

    public static void main(String[] args) throws IOException,
            AnnotatorException {
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
                Configurator.FALSE);
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
                Configurator.FALSE);
        props.setProperty(
                PipelineConfigurator.USE_SRL_INTERNAL_PREPROCESSOR.key,
                Configurator.FALSE);


        props.setProperty(AnnotatorServiceConfigurator.DISABLE_CACHE.key,
                Configurator.TRUE);
        props.setProperty(AnnotatorServiceConfigurator.CACHE_DIR.key,
                "/tmp/aswdtgffasdfasd");
        props.setProperty(
                AnnotatorServiceConfigurator.THROW_EXCEPTION_IF_NOT_CACHED.key,
                Configurator.FALSE);
        props.setProperty(
                AnnotatorServiceConfigurator.FORCE_CACHE_UPDATE.key,
                Configurator.TRUE);
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

        String LOCATION = "/tmp/file.txt";

        List<String> lines = FileUtils.readLines(new File(LOCATION));

        for (String line : lines) {
            TextAnnotation ta = processor.createAnnotatedTextAnnotation("",
                    "", line);

            visualizeView(ta, ViewNames.SHALLOW_PARSE);
            visualizeView(ta, ViewNames.NER_CONLL);
            visualizeView(ta, ViewNames.NER_ONTONOTES);
        }


    }
}
