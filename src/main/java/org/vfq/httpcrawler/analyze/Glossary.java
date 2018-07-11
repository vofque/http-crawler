package org.vfq.httpcrawler.analyze;

import java.util.Collection;
import java.util.Iterator;

public class Glossary implements Iterable<GlossaryItem> {

    private final Collection<GlossaryItem> items;

    Glossary(Collection<GlossaryItem> items) {
        this.items = items;
    }

    @Override
    public Iterator<GlossaryItem> iterator() {
        return items.iterator();
    }

    @Override
    public String toString() {
        if (items.size() == 0) return "nothing found :(";
        StringBuilder sb = new StringBuilder();
        sb.append("top words:\n");
        sb.append("--------------------------------\n");
        sb.append(String.format("%8s | %8s |  %s\n", "index", "count", "word"));
        sb.append("--------------------------------\n");
        int i = 0;
        for (GlossaryItem item: items) {
            sb.append(String.format("%8d | %8d |  %s\n", ++i, item.getCount(), item.getWord()));
        }
        sb.append("--------------------------------\n");
        return sb.toString();
    }
}
