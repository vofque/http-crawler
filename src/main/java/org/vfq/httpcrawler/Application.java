package org.vfq.httpcrawler;

import org.vfq.httpcrawler.analyze.DataAggregator;
import org.vfq.httpcrawler.analyze.Glossary;
import org.vfq.httpcrawler.analyze.GlossaryDataAggregator;
import org.vfq.httpcrawler.parse.JSoupPageParserSupplier;
import org.vfq.httpcrawler.parse.PageParserSupplier;
import org.vfq.httpcrawler.parse.PageProblem;
import org.vfq.httpcrawler.process.MultiThreadPageProcessor;
import org.vfq.httpcrawler.process.PageProcessor;
import org.vfq.httpcrawler.util.CheckedFunction;
import org.vfq.httpcrawler.util.Config;
import org.vfq.httpcrawler.util.Environment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

public class Application {

    public static void main(String[] args) throws IOException {
        Config config = new Config();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                URL url = read("url", "could not parse url", Application::readUrl, br);
                int depth = read("depth", "could not parse depth", Application::readDepth, br);

                //run the main algorithm
                process(url, depth, config);

                System.out.print("start again? (y/n): ");
                String response = br.readLine().trim().toLowerCase();
                if ("n".equals(response)) break;
            }
        }
    }

    private static void process(URL url, int depth, Config config) {
        Environment environment = new Environment(config);

        // create data aggregator for extracting and collecting top words found on pages
        DataAggregator<?, Glossary> dataAggregator = new GlossaryDataAggregator(environment);

        // create a supplier of page parsers using JSoup library
        PageParserSupplier pageParserSupplier = new JSoupPageParserSupplier(dataAggregator::createPerceiver, environment);

        // create a page processor which will organize crawling process in general
        PageProcessor processor = new MultiThreadPageProcessor(pageParserSupplier, environment);

        // run recursive processing of a web page for the given url and depth
        processor.process(url, depth);

        // wait until all pages are processed and shut down the processor
        processor.await();
        processor.shutdown();

        // print result and errors
        Glossary glossary = dataAggregator.getResult();
        print(glossary, processor.getPageProblems());
    }

    private static void print(Glossary glossary, Collection<PageProblem> pageProblems) {
        System.out.println();
        for (PageProblem pageProblem: pageProblems) {
            System.out.println(pageProblem);
        }
        System.out.println();
        System.out.println(glossary);
    }

    private static <T> T read(String message, String errorMessage, CheckedFunction<String, T> reader, BufferedReader br) {
        while (true) {
            System.out.print(message + ": ");
            try {
                return reader.apply(br.readLine().trim());
            } catch (Exception e) {
                System.out.printf("%s: %s\n", errorMessage, e.getMessage());
            }
        }
    }

    private static final String URL_REGEX = "https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    private static URL readUrl(String string) throws MalformedURLException {
        if (!string.matches(URL_REGEX)) {
            throw new RuntimeException("url should look like 'http://site.ru' or 'https://secured.site.ru'");
        }
        return new URL(string);
    }

    private static final String DEPTH_REGEX = "\\d+";

    private static int readDepth(String string) {
        if (!string.matches(DEPTH_REGEX)) {
            throw new RuntimeException("depth should be a non-negative number");
        }
        return Integer.parseInt(string);
    }
}
