package org.vfq.httpcrawler.parse;

import org.vfq.httpcrawler.process.PageProcessor;

import java.net.URL;

/**
 * Supplies page parsers for different URLs and depths.
 */
public interface PageParserSupplier {

    PageParser get(URL url, int depth, PageProcessor processor);
}
