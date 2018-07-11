package org.vfq.httpcrawler.util;

import java.util.HashSet;
import java.util.Set;

class UsedUrlKeeper {

    private final Set<String> urlStrings = new HashSet<>();

    UsedUrlKeeper() { }

    synchronized boolean test(String urlString) {
        if (!urlStrings.contains(urlString)) {
            urlStrings.add(urlString);
            return false;
        }
        return true;
    }
}
