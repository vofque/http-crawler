package org.vfq.httpcrawler.process;

import org.vfq.httpcrawler.parse.PageParser;
import org.vfq.httpcrawler.parse.PageParserSupplier;
import org.vfq.httpcrawler.parse.PageProblem;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Implements basic common functionality of a page processor.
 */
abstract class AbstractPageProcessor implements PageProcessor {

    private final DynamicLatch latch = new DynamicLatch();

    private final PageParserSupplier pageParserSupplier;

    private final Collection<PageProblem> pageProblems = new ArrayList<>();

    AbstractPageProcessor(PageParserSupplier pageParserSupplier) {
        this.pageParserSupplier = pageParserSupplier;
    }

    /**
     * Creates a new page parser for the given url and depth.
     * Wraps the page parser for its further execution in a thread pool.
     */
    Runnable createPageAction(URL url, int depth) {
        PageParser pageParser = pageParserSupplier.get(url, depth, this);
        return new PageAction(pageParser, latch);
    }

    @Override
    public synchronized void declare(PageProblem pageProblem) {
        pageProblems.add(pageProblem);
    }

    @Override
    public void await() {
        latch.await();
    }

    @Override
    public Collection<PageProblem> getPageProblems() {
        return Collections.unmodifiableCollection(pageProblems);
    }
}
