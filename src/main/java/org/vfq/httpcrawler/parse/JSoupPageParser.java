package org.vfq.httpcrawler.parse;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.vfq.httpcrawler.util.Environment;
import org.vfq.httpcrawler.analyze.PagePerceiver;
import org.vfq.httpcrawler.process.PageProcessor;

import java.io.IOException;
import java.net.URL;

/**
 * Page parser implementation using JSoup.
 */
class JSoupPageParser extends AbstractPageParser {

    private final int requestTimeout;

    JSoupPageParser(URL url, int depth, PagePerceiver perceiver, PageProcessor processor, Environment environment) {
        super(url, depth, perceiver, processor, environment);
        requestTimeout = environment.getConfig().getInt("request-timeout");
    }

    @Override
    public void run() {
        try {
            Connection conn = Jsoup.connect(getUrl().toString()).timeout(requestTimeout);
            Document doc = conn.get();
            doc.select("*").forEach(this::handleElement);
            super.finish();
        } catch (UnsupportedMimeTypeException e) {
            // ignore unsupported mime types
        } catch (IOException e) {
            // gather all problems to show them all at the end
            super.declare(new PageProblem(getUrl(), e));
        }
    }

    private void handleElement(Element element) {
        if (element.tag().getName().equals("a")) {
            handleAnchorElement(element);
        }
        handleAnyElement(element);
    }

    private void handleAnyElement(Element element) {
        String text = element.ownText().trim();
        if (text.length() == 0) {
            return;
        }
        super.handleText(text);
    }

    private void handleAnchorElement(Element element) {
        if (!element.hasAttr("href")) {
            return;
        }
        String subUrl = element.attr("href");
        if (!subUrl.startsWith("/")) {
            return;
        }
        super.handleSubUrl(subUrl);
    }
}
