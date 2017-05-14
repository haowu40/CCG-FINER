package edu.illinois.cs.cogcomp.other.embeddings;

import java.io.*;
import java.util.*;

/**
 * Created by pavankumar on 1/12/17.
 */
public class Embeddings implements Serializable {
    private static final long serialVersionUID = 42L;
    private List<String> words = null;
    private Map<String, Integer> wordToEmbeddingIndex = null;
    private List<double[]> embeddings = null;

    public Embeddings() {
    }

    /**
     * @param vocabFile      The file containing the list of words
     * @param embeddingsFile The file containing embeddings corresponding the
     *                       words in <code>vocabFile</code>
     *                       with comma separated floats
     */
    public void loadEmbeddings(File vocabFile, File embeddingsFile, int
            embeddingSize) throws IOException {
        List<String> words;
        Map<String, Integer> word2EmbeddingIndex;
        List<double[]> embeddings;

        try (BufferedReader reader = new BufferedReader(new FileReader
                (vocabFile))) {
            words = new ArrayList<>();
            word2EmbeddingIndex = new HashMap<>();
            int curIndex = 0;
            System.out.println("reading words");
            for (String line; (line = reader.readLine()) != null; ) {
                if (line.length() > 0) {
                    words.add(line);
                    word2EmbeddingIndex.put(line, curIndex);
                    curIndex++;
                    if (curIndex % 50000 == 0)
                        System.out.println("read " + curIndex + " words");
                }
            }
            System.out.println("done");
        }

        try (Scanner s = new Scanner(new BufferedReader(new FileReader
                (embeddingsFile)))) {
            embeddings = new ArrayList<>();
            double[] embedding;
            System.out.println("reading embeddings");
            int numRead = 0;
            while (s.hasNext()) {
                embedding = new double[embeddingSize];
                for (int i = 0; i < embeddingSize; i++)
                    embedding[i] = s.nextDouble();
                if (++numRead % 10000 == 0)
                    System.out.println("read " + numRead + " embeddings");
            }
        }

        assert word2EmbeddingIndex.size() == words.size();
        assert word2EmbeddingIndex.size() == embeddings.size();

        this.words = words;
        this.wordToEmbeddingIndex = word2EmbeddingIndex;
        this.embeddings = embeddings;
    }


    public void loadEmbeddings(String vocabFilePath, String
            embeddingsFilePath, int embeddingSize) throws IOException {
        File vocabFile = new File(vocabFilePath);
        File embeddingsFile = new File(embeddingsFilePath);
        loadEmbeddings(vocabFile, embeddingsFile, embeddingSize);
    }


    public void loadEmbeddingsWithKey(File vocabToKeyFile, Scanner
            keyToEmbeddingsReader, int embeddingSize) throws IOException {
        Map<String, Integer> keyToEmbeddingIndex = new HashMap<>();
        List<double[]> embeddings = new ArrayList<>();
        List<String> words;
        Map<String, Integer> wordToEmbeddingIndex = new HashMap<>();

        double[] embedding;
        int curEmbeddingIndex = 0;
        while (keyToEmbeddingsReader.hasNext()) {
            String key = keyToEmbeddingsReader.next();
            keyToEmbeddingIndex.put(key, curEmbeddingIndex);
            curEmbeddingIndex++;

            embedding = new double[embeddingSize];
            for (int i = 0; i < embeddingSize; i++)
                embedding[i] = keyToEmbeddingsReader.nextDouble(); // the
            // reader has to have another double

            embeddings.add(embedding);
            if (curEmbeddingIndex % 10000 == 0)
                System.out.println("Read " + curEmbeddingIndex + " embeddings");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader
                (vocabToKeyFile))) {
            words = new ArrayList<>();
            wordToEmbeddingIndex = new HashMap<>();
            for (String line; (line = reader.readLine()) != null; ) {
                if (line.length() > 0) {
                    String[] tokens = line.split("\\s+");
                    String word = tokens[0];
                    String key = tokens[1];
                    words.add(word);
                    if (!keyToEmbeddingIndex.containsKey(key))
                        throw new IllegalArgumentException("key (" + key + ")" +
                                " not found in key to embeddings map");
                    wordToEmbeddingIndex.put(word, keyToEmbeddingIndex.get
                            (key));
                }
            }
        }

        this.words = words;
        this.wordToEmbeddingIndex = wordToEmbeddingIndex;
        this.embeddings = embeddings;
    }

    public void loadEmbeddingsWithKeyWithMetaInfo(File vocabToKeyFile, File
            keyToEmbeddingsFileWithMetaInfo)
            throws IOException {
        try (Scanner s = new Scanner(new BufferedReader(new FileReader
                (keyToEmbeddingsFileWithMetaInfo)))) {
            String metaInfo = s.nextLine();
            String[] tokens = metaInfo.split("\\s+");
            int embeddingSize = Integer.parseInt(tokens[1]);
            loadEmbeddingsWithKey(vocabToKeyFile, s, embeddingSize);
        }
    }

    public void loadEmbeddingsWithKeyWithMetaInfo(String vocabToKeyFilePath,
                                                  String keyToEmbeddingsFileWithMetaInfoPath) throws IOException {
        File vocabToKeyFile = new File(vocabToKeyFilePath);
        File keyToEmbeddingsFileWithMetaInfo = new File
                (keyToEmbeddingsFileWithMetaInfoPath);
        loadEmbeddingsWithKeyWithMetaInfo(vocabToKeyFile,
                keyToEmbeddingsFileWithMetaInfo);
    }

    public void saveEmbeddings(String savePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(savePath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        assert words != null || wordToEmbeddingIndex != null || embeddings !=
                null;
        oos.writeObject(words);
        oos.writeObject(wordToEmbeddingIndex);
        oos.writeObject(embeddings);
        oos.close();
    }

    public void loadSavedEmbeddings(String loadPath) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = new FileInputStream(loadPath);
        ObjectInputStream ois = new ObjectInputStream(fis);
        words = (List<String>) ois.readObject();
        wordToEmbeddingIndex = (Map<String, Integer>) ois.readObject();
        embeddings = (List<double[]>) ois.readObject();
        ois.close();
    }

    public static void main(String[] args) throws IOException,
            ClassNotFoundException {

        String synsetEmbeddingsPath =
                "/home/muddire2/nlpresearch2/Wordnet-Resources/embeddings" +
                        "/synsets.txt";
        String lemmaSynKeyToSynsetEmbeddingsKey =
                "/home/muddire2/nlpresearch2/finer_annotation" +
                        "/lemma_syn_key_to_wn_17_synset_id.txt";
//        Embeddings synsetEmbs = new Embeddings();
//        synsetEmbs.loadEmbeddingsWithKeyWithMetaInfo
// (lemmaSynKeyToSynsetEmbeddingsKey, synsetEmbeddingsPath);
//        synsetEmbs.saveEmbeddings
// ("/home/muddire2/nlpresearch2/figer-annotation/cache/synset_embeddings.ser");
//        System.out.println("done");
//
        String wordEmbeddingsPath =
                "/shared/preprocessed/muddire2/Google/GoogleNews-vectors" +
                        "-negative300.embs_500k.txt";
        String wordsPath = "/shared/preprocessed/muddire2/Google/GoogleNews" +
                "-vectors-negative300.words_500k.txt";
//        Embeddings wordEmbs = new Embeddings();
//        wordEmbs.loadEmbeddings(wordsPath, wordEmbeddingsPath, 300);
//        wordEmbs.saveEmbeddings
// ("/home/muddire2/nlpresearch2/figer-annotation/cache/word_embeddings.ser");
//        wordEmbs.saveEmbeddings
// ("/home/muddire2/nlpresearch/figer-annotation/cache/word_embeddings.ser");
//        System.out.println("done");

        long startTime = System.nanoTime();
        Embeddings synsetEmbeddings = new Embeddings();
        synsetEmbeddings.loadSavedEmbeddings
                ("/home/muddire2/nlpresearch2/figer-annotation/cache" +
                        "/synset_embeddings.ser");

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get
        // milliseconds.
        System.out.println("Time taken = " + duration * 1. / 1000000000 + " s");

        startTime = System.nanoTime();
        Embeddings wordEmbeddings = new Embeddings();
        wordEmbeddings.loadSavedEmbeddings
                ("/home/muddire2/nlpresearch2/figer-annotation/cache" +
                        "/word_embeddings.ser");
        endTime = System.nanoTime();
        duration = (endTime - startTime);  //divide by 1000000 to get
        // milliseconds.
        System.out.println("Time taken = " + duration * 1. / 1000000000 + " s");
    }
}
