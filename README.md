# GitHub Activity CLI

This Project is one of the projects suggested in roadmap.sh to learn Java. You can find the project requirements [here](https://roadmap.sh/projects/github-user-activity).

This is a simple command-line interface (CLI) application written in Java that fetches and displays the recent activity of a GitHub user. The application uses the GitHub API to retrieve the user's activity and displays it in the terminal.

## Features

- Fetches recent activity of a specified GitHub user.
- Displays activity details such as commits pushed, issues opened/closed, and repositories starred.
- Handles errors gracefully, such as invalid usernames or API failures.
- Uses Java's built-in capabilities for making HTTP requests and parsing JSON data.

## Requirements

- Java 21 LTS

## Usage

1. Clone the repository or download the source code.
2. Navigate to the project directory.
3. Compile the Java file:
    ```sh
    javac GitHubActivityCLI.java
    ```
4. Run the application with a GitHub username as an argument:
    ```sh
    java GitHubActivityCLI <username>
    ```

### Example

```sh
$ java GitHubActivityCLI johndoe
```

This will display the recent activity of the user "johndoe".

