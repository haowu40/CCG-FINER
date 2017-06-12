package edu.illinois.cs.cogcomp.finer.config;

import java.io.InputStream;

/**
 * Created by haowu4 on 5/14/17.
 */
public class FinerConfiguration {
    private String patternDBPath = "data/patterndb.txt";
    private String KBMentionDBPath = "data/patterndb.txt";
    private String wordSenseDBPath = "data/patterndb.txt";
    private String wordVectorPath = "data/patterndb.txt";
    private String typeSystemDBPath = "data/patterndb.txt";


    private boolean useKBBiasTyper = true;
    private boolean usePatternTyper = true;
    private boolean useHyponymTyper = true;


    public String getPatternDBPath() {
        return patternDBPath;
    }

    public void setPatternDBPath(String patternDBPath) {
        this.patternDBPath = patternDBPath;
    }

    public String getKBMentionDBPath() {
        return KBMentionDBPath;
    }

    public void setKBMentionDBPath(String KBMentionDBPath) {
        this.KBMentionDBPath = KBMentionDBPath;
    }

    public String getWordSenseDBPath() {
        return wordSenseDBPath;
    }

    public void setWordSenseDBPath(String wordSenseDBPath) {
        this.wordSenseDBPath = wordSenseDBPath;
    }

    public String getWordVectorPath() {
        return wordVectorPath;
    }

    public void setWordVectorPath(String wordVectorPath) {
        this.wordVectorPath = wordVectorPath;
    }

    public boolean usingKBBiasTyper() {
        return useKBBiasTyper;
    }

    public void setUseKBBiasTyper(boolean useKBBiasTyper) {
        this.useKBBiasTyper = useKBBiasTyper;
    }

    public boolean usingPatternTyper() {
        return usePatternTyper;
    }

    public void setUsePatternTyper(boolean usePatternTyper) {
        this.usePatternTyper = usePatternTyper;
    }

    public boolean usingHyponymTyper() {
        return useHyponymTyper;
    }

    public void setUseHyponymTyper(boolean useHyponymTyper) {
        this.useHyponymTyper = useHyponymTyper;
    }

    public String getTypeSystemDBPath() {
        return typeSystemDBPath;
    }

    public void setTypeSystemDBPath(InputStream typeSystemDBPath) {
        this.typeSystemDBPath = typeSystemDBPath;
    }
}
