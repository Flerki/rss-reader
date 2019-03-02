package com.amairovi.utility;

import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.Headers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static java.util.stream.Collectors.joining;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class FeedStubServer {

    private final int port;
    private final String baseUrl;

    private ClientAndServer mockServer;

    public FeedStubServer(int port) {
        this.port = port;
        this.baseUrl = "http://localhost:" + port;

    }

    public void start() {
        mockServer = startClientAndServer(port);
        addEndpoint("/atom", readFile("atom.xml"));
        addEndpoint("/rss", readFile("rss.xml"));

        addEndpoint("/atom_without_entries", readFile("atom-without-entries.xml"));
        addEndpoint("/rss_without_entries", readFile("rss-without-entries.xml"));

        addEndpoint("/atom_without_published", readFile("atom-without-published.xml"));
        addEndpoint("/rss_without_published", readFile("rss-without-published.xml"));

        addEndpoint("/single_request", readFile("single_request.xml"));

        addEndpoint("/several_requests", readFile("several_request_1.xml"), readFile("several_request_1.xml"), readFile("several_request_2.xml"), readFile("several_request_3.xml"));
    }

    private void addEndpoint(String path, String response) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath(path)
        )
                .respond(response()
                        .withHeader("Content-type", "application/rss+xml;charset=UTF-8")
                        .withStatusCode(200)
                        .withBody(response)
                );
    }

    private void addEndpoint(String path, String... responses) {
        for (String response : responses) {
            mockServer.when(
                    request()
                            .withMethod("GET")
                            .withPath(path),
                    Times.once()
            )
                    .respond(response()
                            .withHeader("Content-type", "application/rss+xml;charset=UTF-8")
                            .withStatusCode(200)
                            .withBody(response)
                    );
        }

    }

    public String getAtomUrl() {
        return baseUrl + "/atom";
    }

    public String getAtomWithoutEntries() {
        return baseUrl + "/atom_without_entries";
    }

    public String getAtomWithoutPublished() {
        return baseUrl + "/atom_without_published";
    }

    public String getRssWithoutPublished() {
        return baseUrl + "/rss_without_published";
    }

    public String getRssWithoutEntries() {
        return baseUrl + "/rss_without_entries";
    }

    public String getRssUrl() {
        return baseUrl + "/rss";
    }

    public String getUrlForSingleFeedRequest(){
        return baseUrl + "/single_request";
    }

    public String getUrlForSeveralFeedsRequest(){
        return baseUrl + "/several_requests";
    }

    public void stop() {
        mockServer.stop();
    }

    private String readFile(String name) {
        try (InputStream resourceAsStream = FeedStubServer.class.getResourceAsStream(name)) {
            BufferedReader input = new BufferedReader(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8));
            return input.lines().collect(joining());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
