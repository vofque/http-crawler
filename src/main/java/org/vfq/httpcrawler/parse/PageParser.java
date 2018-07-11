package org.vfq.httpcrawler.parse;

import java.net.URL;

/**
 * Page parser is meant to be responsible for parsing a single HTML only.
 * Page parser is a Runnable which can be executed as a task.
 */
public interface PageParser extends Runnable {

    URL getUrl();
}
