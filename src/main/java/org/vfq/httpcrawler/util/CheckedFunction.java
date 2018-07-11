package org.vfq.httpcrawler.util;

@FunctionalInterface
public interface CheckedFunction<T, R> {

    R apply(T t) throws Exception;
}
