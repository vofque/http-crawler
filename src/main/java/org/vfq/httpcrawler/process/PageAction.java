package org.vfq.httpcrawler.process;

import org.vfq.httpcrawler.parse.PageParser;

/**
 * Wrapper of a page parser which signals about starting and finishing of the main action.
 */
class PageAction implements Runnable {

    private final PageParser pageParser;

    private final DynamicLatch latch;

    PageAction(PageParser pageParser, DynamicLatch latch) {
        this.pageParser = pageParser;
        this.latch = latch;
        latch.acquire();
    }

    @Override
    public void run() {
        pageParser.run();
        latch.release();
    }
}
