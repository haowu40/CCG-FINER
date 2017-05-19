package edu.illinois.cs.cogcomp.finer.components;

import edu.illinois.cs.cogcomp.finer.FinerAnnotator;
import edu.illinois.cs.cogcomp.finer.components.pattern_typer.SimplePattern;
import edu.illinois.cs.cogcomp.finer.config.FinerConfiguration;
import edu.illinois.cs.cogcomp.finer.datastructure.FineNerType;
import edu.illinois.cs.cogcomp.finer.types.TypeSystem;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Map;

/**
 * Created by haowu4 on 5/16/17.
 */
public class FinerTyperFactory {
    private FinerConfiguration configuration;

    public FinerTyperFactory(FinerConfiguration configuration, boolean lazyInit) {
        this.configuration = configuration;
    }

    public FinerTyperFactory(FinerConfiguration configuration) {
        this(configuration, false);
    }

    public boolean checkAllResources() {
        throw new NotImplementedException();
    }

    public FinerAnnotator getAnnotator() {
        throw new NotImplementedException();
    }

    private IFinerTyper getKBBiasTyper() {
        throw new NotImplementedException();
    }

    private IFinerTyper getHypTyper() {
        throw new NotImplementedException();
    }


    private TypeSystem typeSystem = null;
    private MentionDetecter mentionDetecter = null;

    Map<String, List<FineNerType>> KBBiasDB;
    Map<SimplePattern, List<FineNerType>> patternDB;

    private List<IFinerTyper> typers = null;


}
