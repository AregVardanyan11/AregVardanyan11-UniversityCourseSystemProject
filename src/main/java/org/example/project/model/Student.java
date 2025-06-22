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
    @Column(nullable = false)
    private String surname;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Faculty faculty;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private User user;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Set<Takes> taked;


    public void enrollToSection(Section section){
        if(section.getReservedSeats() >= section.getMaximumSeats()){
            throw new IllegalArgumentException("Section is full");
        }
        Takes takes = new Takes();
        takes.setSection(section);
        takes.setStudent(this);
        taked.add(takes);
        section.setReservedSeats(section.getReservedSeats() + 1);
    }

    public void unenrollFromSection(Section section){
        if(section.getReservedSeats() <= 0){
            throw new IllegalArgumentException("Student is not enrolled in this section");
        }
        Takes takes = new Takes();
        takes.setSection(section);
        takes.setStudent(this);
        taked.remove(takes);
        section.setReservedSeats(section.getReservedSeats() - 1);
    }
}
