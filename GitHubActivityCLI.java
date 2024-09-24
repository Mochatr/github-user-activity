public class GitHubActivityCLI {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide a GitHub username as a command-line argument.");
            System.out.println("Usage: java GitHubActivityCLI <username>");
            return;
        }

        String username = args[0];
        // Fetch and display activity.
    }
}
