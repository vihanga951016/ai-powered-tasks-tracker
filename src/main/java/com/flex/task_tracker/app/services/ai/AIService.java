package com.flex.task_tracker.app.services.ai;

import com.flex.task_tracker.app.entities.user.Skill;
import com.flex.task_tracker.app.entities.projects.requests.AIRequest;
import com.flex.task_tracker.app.entities.projects.requests.ai.GeminiData;
import com.flex.task_tracker.app.entities.projects.requests.ai.GeneratedProject;
import com.flex.task_tracker.app.entities.projects.requests.ai.GeneratedTask;
import com.flex.task_tracker.app.entities.projects.Project;
import com.flex.task_tracker.app.entities.projects.Task;
import com.flex.task_tracker.app.entities.projects.TaskSkill;
import com.flex.task_tracker.app.repositories.project.ProjectRepository;
import com.flex.task_tracker.app.repositories.user.SkillRepository;
import com.flex.task_tracker.app.repositories.project.TaskRepository;
import com.flex.task_tracker.app.repositories.project.TaskSkillRepository;
import com.flex.task_tracker.app.services.ai.helper.AIServiceHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.flex.task_tracker.common.constants.Messages.*;
import static com.flex.task_tracker.common.constants.ProgressConstant.*;
import static com.flex.task_tracker.common.http.ReturnResponse.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class AIService {

    private final AIServiceHelper aiServiceHelper;

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskSkillRepository taskSkillRepository;
    private final SkillRepository skillRepository;

    @SneakyThrows
    public ResponseEntity generatePlan(AIRequest aiRequest, HttpServletRequest request) {
        log.info(request.getRequestURI());

        if (!aiServiceHelper.apiKeyCheck()) {
            return ERROR("AI Connection error: Missing API Key");
        }

        String instruction = STRUCTURE + aiRequest.getPrompt() + END;

        Map<String, Object> payload = aiServiceHelper.createPayload(instruction);

        HttpHeaders httpHeader = aiServiceHelper.createApiHeader();

        GeminiData geminiData = aiServiceHelper.getGoogleApiResponse(payload, httpHeader);

        String text = geminiData.getCandidates().get(0).getContent().getParts().get(0).getText();

        GeneratedProject generatedProject = aiServiceHelper.getGeneratedProjectByGeneratedText(text);

        return DATA(generatedProject != null ? generatedProject : REGENERATE);
    }

    public ResponseEntity saveGeneratedPlan(GeneratedProject generatedProject, HttpServletRequest request) {
        log.info(request.getRequestURI());
        log.info("project " + generatedProject.getProject());

        if (projectRepository.existsByTitle(generatedProject.getProject())) {
            return ERROR("This project plan is already exist");
        }

        Project project = Project.builder()
                .title(generatedProject.getProject())
                .deadline(generatedProject.getDeadline())
                .addedTime(new Date())
                .status(PROJECT_PENDING)
                .build();

        projectRepository.save(project);

        for (GeneratedTask generatedTask: generatedProject.getTasks()) {
            log.info("saving " + generatedTask.getTasksTitle() + " task.");

            Task task = Task.builder()
                    .subject(generatedTask.getTasksTitle())
                    .description(generatedTask.getDescription())
                    .addedTime(new Date())
                    .duration(generatedTask.getDuration())
                    .project(project)
                    .build();

            taskRepository.save(task);

            saveTaskSkills(generatedTask.getSkills(), task);
        }

        return SUCCESS("Plan saved");
    }

    private void saveTaskSkills(List<String> skills, Task task) {
        if (skills.size() > 0) {

            List<TaskSkill> taskSkills = new ArrayList<>();

            for (String skill: skills) {
                log.info("saving " + skill + " skill from " + task.getSubject() + " task.");
                String normalizedSkill = skill.trim().toLowerCase();

                Skill exSkill = skillRepository.checkSkill(normalizedSkill);

                if (exSkill == null) {
                    Skill newSkill = Skill.builder()
                            .skill(skill).build();

                    exSkill = skillRepository.save(newSkill);
                }

                TaskSkill taskSkill = TaskSkill.builder()
                        .skill(exSkill)
                        .task(task)
                        .build();

                taskSkills.add(taskSkill);
            }

            if (taskSkills.size() > 0) {
                taskSkillRepository.saveAll(taskSkills);
            }
        }
    }
}
