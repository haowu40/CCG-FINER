package edu.illinois.cs.cogcomp;

import edu.illinois.cs.cogcomp.annotation.AnnotatorException;
import edu.illinois.cs.cogcomp.annotation.BasicAnnotatorService;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation
        .TextAnnotation;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.View;
import edu.illinois.cs.cogcomp.finer.FinerAnnotator;
import edu.illinois.cs.cogcomp.finer.utils.PipelineUtils;
import net.sf.extjwnl.JWNLException;

import java.io.IOException;

import static edu.illinois.cs.cogcomp.finer.utils.PipelineUtils.getPipeline;

/**
 * Created by haowu4 on 1/15/17.
 */
public class FinerSystemTest {

    public static void main(String[] args) throws IOException,
            AnnotatorException, JWNLException {
//        String sentence = "Not content with bringing Rocky back to cinema " +
//                "screens. Another Stallone character Vietnam vet " +
//                "John Rambo is coming out of hibernation , 19 years after " +
//                "the third film in the series .";

        String sentence = "Sheik Salman al-Feraiji , al-Sadr 's chief " +
                "representative in Sadr City , issued a statement with " +
                "demands to quell the discontent , including the release of " +
                "Sadrist detainees , an end to military operations against " +
                "them and al-Maliki 's resignation .";

        FinerAnnotator finerAnnotator = new FinerAnnotator(PipelineUtils
                .readFinerTypes("resources/type_to_wordnet_senses.txt"));

        BasicAnnotatorService processor = getPipeline();

        TextAnnotation ta = processor.createAnnotatedTextAnnotation("", "",
                sentence);


        View v = finerAnnotator.annotateByHypernymModel(ta);

        for (Constituent c : v.getConstituents()) {
            System.out.println(c.toString());
        }

    }
}
