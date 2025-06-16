package com.flex.task_tracker.app.services.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flex.task_tracker.app.entities.http.TaskPlan;
import com.flex.task_tracker.app.entities.http.requests.AIRequest;
import com.flex.task_tracker.app.entities.http.requests.ai.GeminiData;
import com.flex.task_tracker.app.repositories.TaskPlanRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.flex.task_tracker.common.http.ReturnResponse.DATA;
import static com.flex.task_tracker.common.http.ReturnResponse.ERROR;


@Slf4j
@Component
public class AIService {

    @Value("${google.ai.key}")
    private String googleAiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TaskPlanRepository repository;

    @Autowired
    public AIService(TaskPlanRepository repository) {
        this.repository = repository;
    }

    @SneakyThrows
    public ResponseEntity getAIResponse(AIRequest aiRequest, HttpServletRequest request) {
        log.info(request.getRequestURI());

        if (googleAiKey == null) {
            return ERROR("AI Connection error: Missing API Key");
        }

        String instruction =
                "You are an AI project planner. Break this prompt into individual development tasks.\n" +
                        "For each task, provide:\n" +
                        "- Title\n" +
                        "- Description\n" +
                        "- Estimated hours\n" +
                        "- Required skills (e.g., Java, React, SQL)\n\n" +
                        "Prompt: " + aiRequest.getPrompt() + ".\n\n" +
                        "Don't want unnecessary explanations. Just give anything according to above structure. \n\n" +
                        "Make sure user above words for make understand easy. \n\n" +
                        "On top of all things, create a title called 'project topic-' and add a short meaningful project topic.";

        if (googleAiKey == null) {
            return ERROR("Connection failed");
        }

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> part = new HashMap<>();
        part.put("text", instruction);

        List<Map<String, Object>> parts = new ArrayList<>();
        parts.add(part);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", parts);

        List<Map<String, Object>> contents = new ArrayList<>();
        contents.add(content);

        Map<String, Object> payload = new HashMap<>();
        payload.put("contents", contents);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(googleAiKey, entity, String.class);

        GeminiData geminiData = objectMapper.readValue(response.getBody(), GeminiData.class);

        String text = geminiData.getCandidates().get(0).getContent().getParts().get(0).getText();

        log.info("text: " + text);

        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        String tx = renderer.render(parser.parse(text));

        return DATA(getTasksPlan(tx));
    }

    private List<TaskPlan> getTasksPlan(String text) {
        System.out.println("**********************************************");
        log.info(text);
        System.out.println("**********************************************");

        List<TaskPlan> result = new ArrayList<>();
        Document doc = Jsoup.parse(text);

        String projectTitle = Optional.ofNullable(doc.selectFirst("h2:contains(Project Topic:)"))
                .map(el -> el.text().replace("Project Topic:", "").trim())
                .orElse("Unknown Project");

        log.info("projectTitle: " + projectTitle);

        Elements taskHeaders = doc.select("p:has(strong:matchesOwn(Task \\d+:))");

        log.info("taskHeaders: " + taskHeaders);

        int sectionIndex = 0;
        for (Element header : taskHeaders) {
            // The <ul> immediately following the task header <p>
            Element ul = header.nextElementSibling();
            if (ul != null && ul.tagName().equals("ul")) {
                Elements listItems = ul.select("li");

                TaskPlan plan = new TaskPlan();
                plan.setProject(projectTitle);
                plan.setSection("Section " + (sectionIndex + 1));

                plan.setTaskTitle(getTextAfterLabelFromList(listItems, "Title:"));
                plan.setDescription(getTextAfterLabelFromList(listItems, "Description:"));
                plan.setEstimatedHours(getTextAfterLabelFromList(listItems, "Estimated Hours:"));
                plan.setRequiredSkills(getTextAfterLabelFromList(listItems, "Required Skills:"));

                result.add(plan);
                sectionIndex++;
            }
        }

        return repository.saveAll(result);
    }

    private String getTextAfterLabelFromList(Elements items, String label) {
        for (Element item : items) {
            if (item.text().startsWith(label)) {
                return item.text().replace(label, "").trim();
            }
        }
        return null;
    }
}
