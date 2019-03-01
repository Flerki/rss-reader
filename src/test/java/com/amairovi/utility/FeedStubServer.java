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
    private final String baseUrl;

    private ClientAndServer mockServer;

    public FeedStubServer(int port) {
        this.port = port;
        this.baseUrl = "http://localhost:" + port;

    }

    public void start() {
        mockServer = startClientAndServer(port);
        addEndpoint("/atom", readFile("atom.xml"));
        addEndpoint("/rss",  readFile("rss.xml"));

        addEndpoint("/atom_without_entries", readFile("atom-without-entries.xml"));
        addEndpoint("/rss_without_entries", readFile("rss-without-entries.xml"));
    }

    private void addEndpoint(String path, String response) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath(path)
        )
                .respond(response()
                        .withStatusCode(200)
                        .withBody(response)
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
