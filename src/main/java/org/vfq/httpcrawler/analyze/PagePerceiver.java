package org.vfq.httpcrawler.analyze;

/**
 * Represents a single page text analyzer.
 * Extracts useful information from the text on the page.
 * Makes intermediate results and passes them to the data aggregator.
 */
public interface PagePerceiver {

    /**
     * Extracts useful information from the provided text.
     */
    void perceive(String text);

    /**
     * Forms intermediate results and passes them to the data aggregator.
     */
    void aggregate();
}
