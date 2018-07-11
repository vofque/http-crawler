package org.vfq.httpcrawler.parse;

import org.vfq.httpcrawler.util.Environment;
import org.vfq.httpcrawler.analyze.PagePerceiver;
import org.vfq.httpcrawler.process.PageProcessor;

import java.net.URL;
import java.util.function.Supplier;

/**
 * Supplies page parsers using JSoup for different URLs and depths.
 */
public class JSoupPageParserSupplier implements PageParserSupplier {

    private final Supplier<PagePerceiver> pagePerceiverSupplier;

    private final Environment environment;

    public JSoupPageParserSupplier(Supplier<PagePerceiver> pagePerceiverSupplier, Environment environment) {
        this.pagePerceiverSupplier = pagePerceiverSupplier;
        this.environment = environment;
    }

    @Override
    public PageParser get(URL url, int depth, PageProcessor processor) {
        return new JSoupPageParser(url, depth, pagePerceiverSupplier.get(), processor, environment);
    }
}
