package org.vfq.httpcrawler.process;

import org.vfq.httpcrawler.parse.PageProblem;

import java.net.URL;
import java.util.Collection;

/**
 * Page processor is meant to organize the whole process of crawling.
 * It isn't supposed to parse HTML or analyze extracted text.
 */
public interface PageProcessor {

    /**
     * Runs processing of the given url with the given depth.
     */
    void process(URL url, int depth);

    /**
     * Saves a problem occurred.
     */
    void declare(PageProblem pageProblem);

    /**
     * Blocks the executing thread until processing is completely finished.
     */
    void await();

    void shutdown();

    Collection<PageProblem> getPageProblems();
}
