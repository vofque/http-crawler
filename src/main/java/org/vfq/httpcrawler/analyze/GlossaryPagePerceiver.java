package org.vfq.httpcrawler.analyze;

import org.vfq.httpcrawler.util.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Page perceiver implementation for gathering top words found on the parsed page.
 */
public class GlossaryPagePerceiver implements PagePerceiver {

    private static final Pattern DELIMITER = Pattern.compile("[^\\p{L}]+");
    private static final int MIN_WORD_LENGTH = 1;

    private final Map<String, Counter> wordCounterMap = new HashMap<>();

    private final GlossaryDataAggregator dataAggregator;
    private final Environment environment;

    GlossaryPagePerceiver(GlossaryDataAggregator dataAggregator, Environment environment) {
        this.dataAggregator = dataAggregator;
        this.environment = environment;
    }

    @Override
    public void perceive(String text) {
        Scanner scanner = new Scanner(text);
        scanner.useDelimiter(DELIMITER);
        while (scanner.hasNext()) {
            String word = scanner.next().toLowerCase();
            // ignore too short words
            if (word.length() <= MIN_WORD_LENGTH) continue;
            // ignore functional words
            if (environment.isFunctionalWord(word)) continue;
            Counter counter = wordCounterMap.get(word);
            if (counter == null) {
                wordCounterMap.put(word, new Counter());
            } else {
                counter.value++;
            }
        }
    }

    @Override
    public void aggregate() {
        Stream<GlossaryItem> glossaryItemStream = wordCounterMap.entrySet()
                .stream()
                .map((entry) -> new GlossaryItem(entry.getKey(), entry.getValue().value));
        // send gathered glossary items to the central data aggregator
        dataAggregator.accept(glossaryItemStream);
    }

    private static class Counter {

        private int value = 1;
    }
}
