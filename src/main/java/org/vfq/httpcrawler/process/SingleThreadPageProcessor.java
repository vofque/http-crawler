package org.vfq.httpcrawler.process;

import org.vfq.httpcrawler.parse.PageParserSupplier;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SingleThreadPageProcessor extends AbstractPageProcessor {

    private final ExecutorService executorService;

    public SingleThreadPageProcessor(PageParserSupplier pageParserSupplier) {
        super(pageParserSupplier);
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public synchronized void process(URL url, int depth) {
        // create a page parser for the given url and depth and pass it to the thread pool
        executorService.submit(super.createPageAction(url, depth));
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) { }
    }
}