package com.amairovi.utility;

import org.mockserver.integration.ClientAndServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.util.stream.Collectors.joining;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class FeedStubServer {

    private final int port;
    private final String atomResponse;
    private final String rssResponse;
    private final String atomWithoutEntries;
    private final String rssWithoutEntries;
    private final String baseUrl;

    private ClientAndServer mockServer;

    public FeedStubServer(int port) {
        this.port = port;
        this.baseUrl = "http://localhost:" + port;

        atomResponse = readFile("atom.xml");
        rssResponse = readFile("rss.xml");
        atomWithoutEntries = readFile("atom-without-entries.xml");
        rssWithoutEntries = readFile("rss-without-entries.xml");

    }

    public void start() {
        mockServer = startClientAndServer(port);
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/atom")
        )
                .respond(response()
                        .withStatusCode(200)
                        .withBody(atomResponse)
                );
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/rss")
        )
                .respond(response()
                        .withStatusCode(200)
                        .withBody(rssResponse)
                );
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/atom_without_entries"))
                .respond(response()
                        .withStatusCode(200)
                        .withBody(atomWithoutEntries)
                );
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/rss_without_entries"))
                .respond(response()
                        .withStatusCode(200)
                        .withBody(rssWithoutEntries)
                );
    }

    public String getAtomUrl() {
        return baseUrl + "/atom";
    }

    public String getAtomWithoutEntries() {
        return baseUrl + "/atom_without_entries";
    }

    public String getRssWithoutEntries() {
        return baseUrl + "/rss_without_entries";
    }

    public String getRssUrl() {
        return baseUrl + "/rss";
    }

    public void stop() {
        mockServer.stop();
    }

    private String readFile(String name) {
        try (InputStream resourceAsStream = Main.class.getResourceAsStream(name)) {
            BufferedReader input = new BufferedReader(new InputStreamReader(resourceAsStream));
            return input.lines().collect(joining());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
