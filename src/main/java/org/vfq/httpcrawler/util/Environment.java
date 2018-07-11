package org.vfq.httpcrawler.util;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Environment {

    private final Set<String> functionalWords = new HashSet<>();

    private final UsedUrlKeeper usedUrlKeeper = new UsedUrlKeeper();

    private final Config config;

    public Environment(Config config) {
        this.config = config;
        readFunctionalWords();
    }

    private void readFunctionalWords() {
        Scanner scanner = new Scanner(config.get("functional-words"));
        while (scanner.hasNext()) {
            functionalWords.add(scanner.next().trim().toLowerCase());
        }
    }

    /**
     * Checks if the given word is in the list of functional words.
     */
    public boolean isFunctionalWord(String word) {
        return functionalWords.contains(word);
    }

    /**
     * Checks if the given url was already processed.
     */
    public boolean urlWasUsed(String urlString) {
        return usedUrlKeeper.test(urlString);
    }

    public Config getConfig() {
        return config;
    }
}
