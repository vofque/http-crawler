package org.vfq.httpcrawler.parse;

import org.vfq.httpcrawler.analyze.PagePerceiver;
import org.vfq.httpcrawler.process.PageProcessor;
import org.vfq.httpcrawler.util.Environment;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Implements basic functionality of a page parser:
 * - passes extracted text to the page perceiver for further gathering of useful data
 * - passes extracted links to the page processor for further processing
 */
abstract class AbstractPageParser implements PageParser {

    private final URL url;
    private final int depth;

    private final PagePerceiver perceiver;
    private final PageProcessor processor;

    private final Environment environment;

    AbstractPageParser(URL url, int depth, PagePerceiver perceiver, PageProcessor processor, Environment environment) {
        this.url = url;
        this.depth = depth;
        this.perceiver = perceiver;
        this.processor = processor;
        this.environment = environment;
    }

    void handleText(String text) {
        perceiver.perceive(text);
    }

    void handleSubUrl(String subUrlString) {
        // check that we are allowed to dive deeper
        if (depth <= 0) {
            return;
        }
        try {
            URL newUrl = new URL(url, subUrlString);
            // check if this url was already processed
            if (environment.urlWasUsed(newUrl.toString())) {
                return;
            }
            // pass url to the page processor
            processor.process(newUrl, depth - 1);
        } catch (MalformedURLException e) {
            throw new PageParseException(e);
        }
    }

    void declare(PageProblem problem) {
        processor.declare(problem);
    }

    void finish() {
        perceiver.aggregate();
    }

    @Override
    public URL getUrl() {
        return url;
    }
}
