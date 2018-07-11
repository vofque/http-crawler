package org.vfq.httpcrawler.analyze;

import org.vfq.httpcrawler.util.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.stream.Stream;

/**
 * Data aggregator implementation for gathering top words found on parsed pages.
 */
public class GlossaryDataAggregator implements DataAggregator<Stream<GlossaryItem>, Glossary> {

    private final NavigableSet<GlossaryItem> glossaryItems = new TreeSet<>();

    private final Map<String, GlossaryItem> wordGlossaryItemMap = new HashMap<>();

    private final int capacity;
    private final Environment environment;

    public GlossaryDataAggregator(Environment environment) {
        this.environment = environment;
        capacity = environment.getConfig().getInt("capacity");
    }

    @Override
    public PagePerceiver createPerceiver() {
        return new GlossaryPagePerceiver(this, environment);
    }

    @Override
    public synchronized void accept(Stream<GlossaryItem> glossaryItemStream) {
        // receive glossary items gathered on a single page
        glossaryItemStream.forEach(this::accept);
    }

    private void accept(GlossaryItem glossaryItem) {
        String word = glossaryItem.getWord();
        GlossaryItem existingGlossaryItem = wordGlossaryItemMap.get(word);
        if (existingGlossaryItem != null) {
            // if such word is already present merge their counts
            glossaryItem = new GlossaryItem(word, existingGlossaryItem.getCount() + glossaryItem.getCount());
            glossaryItems.remove(existingGlossaryItem);
        }
        glossaryItems.add(glossaryItem);
        wordGlossaryItemMap.put(word, glossaryItem);
        // we can get rid of any words that are out of capacity bounds
        if (glossaryItems.size() > capacity) {
            glossaryItems.pollLast();
        }
    }

    @Override
    public Glossary getResult() {
        return new Glossary(glossaryItems);
    }
}
