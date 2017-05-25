package edu.illinois.cs.cogcomp;

import com.beust.jcommander.Parameter;
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

    public static class AppArgs {

    }

    public static void main(String[] args) {
        if (args.length == 0) {
            // Print help statement.
            System.exit(1);
        }


        String[] appArgs = new String[args.length - 1];
        for (int i = 0; i < appArgs.length; i++) {
            appArgs[i] = args[i + 1];
        }

        if (args[0].equals("web")) {

        }

        if (args[0].equals("download")) {

        }
    }
}
