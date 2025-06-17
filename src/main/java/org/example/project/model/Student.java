package org.example.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "studentId")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Faculty faculty;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private User user;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Set<Takes> takes;

    public Set<Section> getTakenSections() {
        return takes.stream()
                .map(Takes::getSection)
                .collect(Collectors.toSet());
    }

    public Double calculateGpa(){
        return takes.stream()
                .mapToDouble(t -> t == null ? 0 : t.getGrade().getValue()).sum();
    }

    public Set<Course> getTakenCourses() {
        return takes.stream()
                .map(Takes::getSection).map(Section::getCourse)
                .collect(Collectors.toSet());
    }

    public void enrollToSection(Section section){
        Takes takes = new Takes();
        takes.setSection(section);
        takes.setStudent(this);
    }
}
