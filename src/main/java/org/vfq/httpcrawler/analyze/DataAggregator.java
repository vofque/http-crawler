package org.vfq.httpcrawler.analyze;

/**
 * Represents a place for gathering data extracted from all parsed pages.
 * Consumes intermediate results, extracted from every parsed page, then merges intermediate results in a final result.
 * @param <IR> intermediate result
 * @param <R> final result
 */
public interface DataAggregator<IR, R> {

    /**
     * Creates appropriate page perceiver for every page.
     */
    PagePerceiver createPerceiver();

    /**
     * Invoked asynchronously by multiple page perceivers to pass their intermediate results.
     * Received intermediate results are merged into final result.
     */
    void accept(IR intermediateResult);

    /**
     * Produces final result
     */
    R getResult();
}
