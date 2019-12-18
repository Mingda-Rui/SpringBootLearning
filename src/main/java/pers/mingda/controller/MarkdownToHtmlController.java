package pers.mingda.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@RestController
public class MarkdownToHtmlController {

    private final String githubMarkdownRawApi = "https://api.github.com/markdown/raw";
    private final String githubMarkdownApi = "https://api.github.com/markdown";

    @RequestMapping(value = "/markdown-to-html-test-raw", produces = MediaType.TEXT_HTML_VALUE)
    public String markdownToHtmlTestRaw() {

        String exampleMarkdown = getExampleMarkdown();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        HttpEntity<String> request = new HttpEntity<>(exampleMarkdown, headers);

        ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(githubMarkdownRawApi, request, String.class);
        return responseEntityStr.getBody();
    }

    @RequestMapping(value= "/markdown-to-html-test", produces = MediaType.TEXT_HTML_VALUE)
    public String markdownToHtmlTest() {
        String exampleMarkdown = getExampleMarkdown();
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> request = new HttpEntity<>(composeMarkdownText(exampleMarkdown));
        ResponseEntity<String> responseEntityStr = restTemplate.postForEntity(githubMarkdownApi, request, String.class);
        return responseEntityStr.getBody();

    }

    private String getExampleMarkdown() {
        String exampleMarkdown;
        try {
            exampleMarkdown = readInExampleNoteFile();
        } catch (Exception ex) {
            System.err.println("failed to get the example Markdown file!" + ex.getMessage());
            ex.printStackTrace();
            exampleMarkdown = null;
        }
        System.out.println("read in example markdown file: \n" + exampleMarkdown);
        return exampleMarkdown;
    }

    private String readInExampleNoteFile() throws IOException {
        Path path = Paths.get("src/main/resources/Note.md");
        BufferedReader reader = Files.newBufferedReader(path);
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }

    private String composeMarkdownText(String textContent) {
        String jsonVal = "{" +
                "\"text\": \""+textContent+"\"" +
                "}";
        return jsonVal;
    }
}
