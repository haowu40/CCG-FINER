package edu.illinois.cs.cogcomp;

import edu.illinois.cs.cogcomp.nlp.tokenizer.StatefulTokenizer;
import edu.illinois.cs.cogcomp.nlp.tokenizer.Tokenizer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by haowu4 on 1/25/17.
 */
public class TokenBench {
    public static void main(String[] args) throws IOException {
        Tokenizer tokenizer = new StatefulTokenizer();
        List<String> ss = FileUtils.readLines(new File
                ("/home/haowu4/data/type_to_alias.txt"), Charset
                .defaultCharset()).stream()
                .filter(s -> s.split("\\t").length == 2)
                .map(s -> s.split("\\t")[1])
                .collect(Collectors.toList());

        long start = System.currentTimeMillis();
        List<Tokenizer.Tokenization> tokenss = new ArrayList<>();
        for (String s : ss) {
            tokenss.add(tokenizer.tokenizeTextSpan(s));
        }
        long end = System.currentTimeMillis();
        System.out.println("Spent " + (end - start) + "ms.");

    }
}
