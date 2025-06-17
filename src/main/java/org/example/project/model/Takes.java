package org.example.project.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.project.model.converter.GradeConverter;
import org.example.project.model.enums.Grade;

import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "section_id"}))
@EqualsAndHashCode(of = {"student", "section"})
public class Takes{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    private Section section;

    @Convert(converter = GradeConverter.class)
    @Column
    private Grade grade;
}
