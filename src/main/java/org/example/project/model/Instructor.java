package org.example.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "instructorId")
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @NotBlank
    private String bio;

    @Column(unique = true, nullable = false)
    private String instructorId;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private User user;

    @OneToMany(mappedBy = "instructor", fetch = FetchType.LAZY)
    Set<Section> sections;

    public Set<Course> getTeachingCourses() {
        return sections.stream()
                .map(Section::getCourse)
                .collect(Collectors.toSet());
    }

}
