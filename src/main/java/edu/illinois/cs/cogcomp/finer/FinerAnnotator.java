package edu.illinois.cs.cogcomp.finer;

import edu.illinois.cs.cogcomp.annotation.Annotator;
import edu.illinois.cs.cogcomp.core.datastructures.Pair;
import edu.illinois.cs.cogcomp.core.datastructures.ViewNames;
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.*;
import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;
import edu.illinois.cs.cogcomp.finer.components.MentionDetecter;
import edu.illinois.cs.cogcomp.finer.components.TriggerWordDetecter;
import edu.illinois.cs.cogcomp.finer.components.TriggerWordFilter;
import edu.illinois.cs.cogcomp.finer.components.filters.MaxDistanceFilter;
import edu.illinois.cs.cogcomp.finer.components.filters.QuotationFilter;
import edu.illinois.cs.cogcomp.finer.components.filters.TypeFilter;
import edu.illinois.cs.cogcomp.finer.components.mention.BasicMentionDetection;
import edu.illinois.cs.cogcomp.finer.components.pruner
        .SingleNearestTriggerPruner;
import edu.illinois.cs.cogcomp.finer.components.trigger
        .BasicTriggerWordDetecter;
import edu.illinois.cs.cogcomp.finer.datastructure.FineNerType;
import net.sf.extjwnl.data.Synset;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by haowu4 on 1/15/17.
 */
public class FinerAnnotator extends Annotator {
    private MentionDetecter mentionDetecter = new BasicMentionDetection();
    private TriggerWordDetecter triggerWordDetecter = new
            BasicTriggerWordDetecter();
    private List<TriggerWordFilter> triggerWordFilters = Arrays.asList(
            new QuotationFilter(),
            new MaxDistanceFilter(),
            new TypeFilter());
    private TriggerMentionPruner pruner = new SingleNearestTriggerPruner();
    private Map<String, List<Synset>> extractTypes;

    public FinerAnnotator(List<FineNerType> extractTypes) {
        super("FINER", new String[]{ViewNames.POS, ViewNames.NER_CONLL});
        this.extractTypes = new HashMap<>();
        for (FineNerType typeName : extractTypes) {
            this.extractTypes.put(typeName.getTypeName(), typeName
                    .getSenseDefs());
        }
    }

    public void setMentionDetecter(MentionDetecter mentionDetecter) {
        this.mentionDetecter = mentionDetecter;
    }

    public void setTriggerWordDetecter(TriggerWordDetecter triggerWordDetecter) {
        this.triggerWordDetecter = triggerWordDetecter;
    }

    public void setTriggerWordFilters(List<TriggerWordFilter> triggerWordFilters) {
        this.triggerWordFilters = triggerWordFilters;
    }

    public void setPruner(TriggerMentionPruner pruner) {
        this.pruner = pruner;
    }

    public void setExtractTypes(Map<String, List<Synset>> extractTypes) {
        this.extractTypes = extractTypes;
    }

    /**
     * @param ta Input TextAnnotation object should have POS, chunker,
     *           NER-conll,
     *           NER-ontonote, and WSD views.
     * @return Fine grain entity annotation.
     */
    public View annotateByHypernymModel(TextAnnotation ta) {

        View finer = new SpanLabelView("FINER-wordnet", ta);

        for (final Sentence s : ta.sentences()) {
            List<Constituent> triggers = triggerWordDetecter.getTriggerWords(s,
                    getExtractTypes());

            for (Constituent c : triggers) {
                c.addAttribute("type", "trigger");
                finer.addConstituent(c);
            }

            List<Constituent> mentions = mentionDetecter.getMentionCandidates
                    (s);

            for (Constituent c : mentions) {
                c.addAttribute("type", "mention");
                finer.addConstituent(c);
            }

            List<Pair<Constituent, Constituent>> relations = new ArrayList<>();

            for (final Constituent mention : mentions) {
                List<Constituent> survivedTriggerWords = triggers.stream()
                        .filter(trigger -> triggerWordFilters.stream()
                                .allMatch(f -> f.filterTriggerWord(s,
                                        trigger, mention)))
                        .collect(Collectors.toList());
                for (Constituent trigger : survivedTriggerWords) {
                    relations.add(new Pair<>(mention, trigger));
                }
            }

            for (Relation r : pruner.pruneRelations(relations)) {
                finer.addRelation(r);
            }
        }
        return finer;
    }

    @Override
    public void initialize(ResourceManager rm) {

    }

    public void addView(TextAnnotation ta) {
        View finer = this.annotateByHypernymModel(ta);
        View finalAnnotation = new SpanLabelView("FINER", ta);
        for (Relation r : finer.getRelations()) {
            Constituent trigger = r.getSource();
            Constituent mention = r.getTarget();
            finalAnnotation.addConstituent(new Constituent(trigger
                    .getLabelsToScores(), "FINER", ta, mention.getStartSpan()
                    , mention.getEndSpan()));
        }
        ta.addView("FINER", finalAnnotation);
    }

    private Map<String, List<Synset>> getExtractTypes() {
        return extractTypes;
    }

}
