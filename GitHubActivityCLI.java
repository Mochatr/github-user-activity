import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;


public class GitHubActivityCLI {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide a GitHub username.");
            System.out.println("Usage: java GithubActivity <username>");
            return;
        }

        String username = args[0];
        String apiUrl = "https://api.github.com/users/" + username + "/events";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Accept", "application/vnd.github.v3+json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                if (responseBody.isEmpty() || responseBody.equals("[]")) {
                    System.out.println("No recent activity found for user: " + username);
                } else {
                    // Proceed with parsing the JSON response
                    parseEvents(responseBody);
                }
            } else if (response.statusCode() == 404) {
                System.out.println("Error: User not found.");
            } else if (response.statusCode() == 403) {
                System.out.println("Error: API rate limit exceeded. Please try again later.");
            } else {
                System.out.println("Error: Received status code " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("An error occurred while fetching data from GitHub.");
            e.printStackTrace();
        }
    }

    public static void parseEvents(String responseBody) {
        // Remove the opening and closing brackets of the JSON array
        responseBody = responseBody.substring(1, responseBody.length() - 1);

        // Split the JSON array into individual events
        String[] events = responseBody.split("\\},\\{");

        // Adjust the first and last event strings
        if (events.length > 0) {
            events[0] = events[0] + "}";
            events[events.length - 1] = "{" + events[events.length - 1];
        }

        for (String event : events) {
            parseAndDisplayEvent(event);
        }
    }

    public static void parseAndDisplayEvent(String event) {
        // Replace escaped quotes
        event = event.replace("\\\"", "\"");

        String type = extractValue(event, "\"type\":\"", "\"");
        String repoName = extractValue(event, "\"repo\":\\{\"id\":\\d+,\"name\":\"", "\"");
        String action = "";

        switch (type) {
            case "PushEvent":
                String commitCount = extractValue(event, "\"size\":", ",");
                action = "Pushed " + commitCount.trim() + " commits to " + repoName;
                break;
            case "IssuesEvent":
                String issueAction = extractValue(event, "\"action\":\"", "\"");
                action = capitalize(issueAction) + " an issue in " + repoName;
                break;
            case "WatchEvent":
                action = "Starred " + repoName;
                break;
            default:
                action = "Performed " + type + " on " + repoName;
                break;
        }

        System.out.println("- " + action);
    }

    public static String extractValue(String text, String startDelimiter, String endDelimiter) {
        int startIndex = text.indexOf(startDelimiter);
        if (startIndex == -1) {
            return "";
        }
        startIndex += startDelimiter.length();
        int endIndex = text.indexOf(endDelimiter, startIndex);
        if (endIndex == -1) {
            return "";
        }
        return text.substring(startIndex, endIndex);
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}