package org.example.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"faculty_id", "num_code"}))
@EqualsAndHashCode(of = {"faculty", "numCode"})
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JoinColumn(name = "faculty_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Faculty faculty;

    @Column(nullable = false)
    @Pattern(regexp = "^[0-9]{3}$", message = "numCode must be exactly 3 digits")
    private String numCode;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @NotBlank
    @Column
    private String description;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer credit;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "course_prerequisite",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "prerequisite_id")
    )
    private Set<Course> prerequisites;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private Set<Section> sections;

    @Override
    public String toString(){
        return faculty.getName() + numCode;
    }


}
