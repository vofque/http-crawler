package org.vfq.httpcrawler.analyze;

/**
 * Represents a word and its count of occurrences
 */
public class GlossaryItem implements Comparable<GlossaryItem> {

    private final String word;
    private final int count;

    GlossaryItem(String word, int count) {
        this.word = word;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(GlossaryItem o) {
        int countComparison = Integer.compare(o.getCount(), this.getCount());
        if (countComparison != 0) {
            return countComparison;
        }
        return this.getWord().compareTo(o.getWord());
    }
}
