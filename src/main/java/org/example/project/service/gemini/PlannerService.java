package org.example.project.service.gemini;

import lombok.RequiredArgsConstructor;
import org.example.project.model.*;
import org.example.project.repository.StudentRepository;
import org.example.project.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final CourseService courseService;
    private final GeminiClient geminiClient;
    private final StudentRepository studentRepository;

    public String generatePlan(User user, String prompt) {
        Student student = studentRepository.getByUserId(user.getId());
        Map<Course, List<Section>> availableCourses = courseService.getAvailableSections(student);
        String fullPrompt = buildPrompt(prompt, availableCourses);
        return geminiClient.getGeminiPlan(fullPrompt);
    }

    private String buildPrompt(String studentPrompt, Map<Course, List<Section>> courseSectionsMap) {
        StringBuilder sb = new StringBuilder();

        sb.append("You are a helpful academic advisor. Based on the student's preferences and the available courses, recommend a semester plan.\n\n");

        sb.append("Student Preferences:\n");
        sb.append(studentPrompt).append("\n\n");

        sb.append("Available Courses and Sections:\n");
        for (Map.Entry<Course, List<Section>> entry : courseSectionsMap.entrySet()) {
            Course course = entry.getKey();
            List<Section> sections = entry.getValue();

            for (Section section : sections) {
                sb.append(String.format(
                        "- Course: %s %s (%d credits)\n",
                        course.getFaculty().getName(),
                        course.getNumCode(),
                        course.getCredit()
                ));
                sb.append(String.format("  Section: %s | Year: %d | Semester: %s\n",
                        section.getLetter(),
                        section.getYear(),
                        section.getSemester()
                ));
                sb.append(String.format("  Instructor: %s %s\n",
                        section.getInstructor().getName(),
                        section.getInstructor().getSurname()
                ));
                sb.append("  Schedule:\n");
                for (TimeSlot timeSlot : section.getTimeSlots()) {
                    sb.append(String.format("    - %s %s–%s in %s\n",
                            timeSlot.getDay(),
                            timeSlot.getStartTime(),
                            timeSlot.getEndTime(),
                            timeSlot.getClassroom()
                    ));
                }
                sb.append("  Topic: ").append(course.getDescription()).append("\n");
                sb.append("  Prerequisites: ").append(formatPrereqs(course.getPrerequisites())).append("\n\n");
            }
        }

        sb.append("Please recommend 3–5 suitable courses for this semester. Explain your choices briefly.");
        return sb.toString();
    }

    private String formatPrereqs(Set<Course> prerequisites) {
        if (prerequisites == null || prerequisites.isEmpty()) {
            return "None";
        }
        return prerequisites.stream()
                .map(c -> c.getFaculty().getName() + c.getNumCode())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }
}
