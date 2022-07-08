package dev.avyguzov;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Class for API integration tests only. It can send requests to server
 */
public class ApiClientTester {
    private final HttpClient client = HttpClient.newHttpClient();
    protected final ObjectMapper mapper = new ObjectMapper();

    public String checkTask(String url, String id) throws Exception {
        var request = HttpRequest.newBuilder(new URI(url))
                .POST(HttpRequest.BodyPublishers.ofString(id))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<String>() {});
    }

    public boolean computeFactorial(String url, String n) throws Exception {
        var request = HttpRequest.newBuilder(new URI(url))
                .POST(HttpRequest.BodyPublishers.ofString(n))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());
        return true;
    }
}