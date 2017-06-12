package edu.illinois.cs.cogcomp.finer.entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.illinois.cs.cogcomp.annotation.AnnotatorException;
import edu.illinois.cs.cogcomp.annotation.BasicAnnotatorService;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.TextAnnotation;
import edu.illinois.cs.cogcomp.finer.FinerAnnotator;
import edu.illinois.cs.cogcomp.finer.components.FinerTyperFactory;
import edu.illinois.cs.cogcomp.finer.config.FinerConfiguration;
import edu.illinois.cs.cogcomp.finer.datastructure.FineTypeConstituent;
import org.apache.commons.lang.NotImplementedException;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.get;
import static spark.Spark.init;

/**
 * Created by haowu4 on 1/15/17.
 */
public class WebInterface {

    private static Gson GSON = new GsonBuilder().create();

    public static BasicAnnotatorService anntationService;
    public static FinerAnnotator finerAnnotator;

    public static BasicAnnotatorService getPipeline() {
        throw new NotImplementedException();
    }

    public static FinerConfiguration getConfig() {
        throw new NotImplementedException();
    }

    public static void loadAnnotators() {
        anntationService = getPipeline();
        FinerTyperFactory factory = new FinerTyperFactory(getConfig());
        finerAnnotator = factory.getAnnotator();
    }

    public static class AnnotationTypeResponse {
        String typeName;

        public AnnotationTypeResponse(FineTypeConstituent c) {
            throw new NotImplementedException();
        }
    }

    public static class AnnootationResultResponse {
        String[] tokens;
        List<AnnotationTypeResponse> mentions;

        public AnnootationResultResponse(String[] tokens, List<AnnotationTypeResponse> mentions) {
            this.tokens = tokens;
            this.mentions = mentions;
        }
    }


    public static String annotationToJson(TextAnnotation ta, List<FineTypeConstituent> mentions) {
        String[] tokens = ta.getTokens();

        AnnootationResultResponse arr = new AnnootationResultResponse(tokens, mentions.stream().map(AnnotationTypeResponse::new).collect(Collectors.toList()));
        return GSON.toJson(arr);
    }

    public static String annotate(Request request, Response response) {
        String text = request.params("text");
        TextAnnotation ta = null;
        try {
            ta = anntationService.createAnnotatedTextAnnotation("", "", text);
        } catch (AnnotatorException e) {
            // TODO: return error status.
            e.printStackTrace();
        }

        List<FineTypeConstituent> constituents = finerAnnotator.getAllFineTypeConstituents(ta);
        return annotationToJson(ta, constituents);

    }


    public static void main(String[] args) {
        init();
        get("/annotate", WebInterface::annotate);
    }

}
