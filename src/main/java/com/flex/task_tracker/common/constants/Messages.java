package com.flex.task_tracker.common.constants;

public class Messages {

    public static final String REGENERATE = "I am sorry, this time I couldn't be able to generate this. Try again please.";
    public static final String STRUCTURE = "You are an AI project planner. Break this prompt into individual development tasks. " +
            "I will send you the task and you should break down the task to sub tasks for allocate those tasks to people like jira. " +
            "Also No need other explanations. Only provide what I am asking. Also response should be in json format. Format is - \n" +
            "\n" +
            "{\n" +
            "  \"project\": \"project title\",\n" +
            "  \"deadline\": \"project deadline\",\n" +
            "  \"tasks\": [\n" +
            "    {\n" +
            "      \"tasksTitle\": \"Tasks title 1\",\n" +
            "      \"description\": \"Sort explanation about how do this\",\n" +
            "      \"duration\": 2, (from hours) \n" +
            "      \"skills\": [\n" +
            "        \"Spring boot\",\n" +
            "        \"Jpa\",\n" +
            "        \"MySql\"\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"tasksTitle\": \"Tasks title 2\",\n" +
            "      \"description\": \"Sort explanation about how do this\",\n" +
            "      \"duration\": 3, (from hours)  \n" +
            "      \"skills\": [\n" +
            "        \"Spring boot\",\n" +
            "        \"Jpa\",\n" +
            "        \"MySql\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}\n" +
            "\n" +
            "Tasks is, ";
    public static final String END = "Please no need to use other skills. Also please consider the deadline if has and " +
            "if not, set the 'deadline' as null.";
}
