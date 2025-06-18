package org.example.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.example.project.model.enums.Semester;

import java.time.Year;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"course_id", "letter", "year", "semester"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "letter", "year", "semester"}))
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private Character letter;

    @Column(nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Semester semester;

    @Column(nullable = false)
    private boolean finished = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Instructor instructor;

    @Pattern(regexp = "^.+\\.pdf$", message = "Syllabus path must point to a PDF file")
    @Column(nullable = false)
    private String syllabusPath;

    @Min(value = 1, message = "Maximum seats must be at least 1")
    @Column(nullable = false)
    private Integer maximumSeats;

    @OneToMany(mappedBy = "section", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    Set<TimeSlot> timeSlots;

    @OneToMany(mappedBy = "section", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Set<Takes> takes;

    public void enrollTheStudent(Student student){
        Takes takes = new Takes();
        takes.setSection(this);
        takes.setStudent(student);
    }


}
