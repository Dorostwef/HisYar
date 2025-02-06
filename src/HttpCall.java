import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpCall {
    private final String httpUrl;
    private final String jsonInputString;

    public HttpCall(String httpUrl, String jsonInputString) {
        this.httpUrl = httpUrl;
        this.jsonInputString = jsonInputString;
    }

    public HttpResponse<String> get (String headerKey, String headerValue, String type) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request;
            if (type.equals("local")) {
                request = HttpRequest.newBuilder().
                        uri(new URI(httpUrl))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                        .build();
            } else if (!headerKey.isEmpty()){
                request = HttpRequest.newBuilder().
                        uri(new URI(httpUrl))
                        .header("Content-Type", "application/json")
                        .header(headerKey, headerValue)
                        //.POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                        .build();
            } else {
                request = HttpRequest.newBuilder().
                        uri(URI.create(httpUrl))
                        //.header("Content-Type", "application/json")
                        //.POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                        .build();
            }
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
