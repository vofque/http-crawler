package org.vfq.httpcrawler.parse;

import java.net.URL;

public class PageProblem {

    private final URL url;
    private final Exception exception;

    PageProblem(URL url, Exception exception) {
        this.url = url;
        this.exception = exception;
    }

    public URL getUrl() {
        return url;
    }

    public Exception getException() {
        return exception;
    }

    @Override
    public String toString() {
        return String.format("error at %s: %s", url, exception.getMessage());
    }
}
