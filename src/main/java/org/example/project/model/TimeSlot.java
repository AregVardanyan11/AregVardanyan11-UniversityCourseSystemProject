package org.example.project.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.project.model.enums.WeekDay;
import java.time.LocalTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String classroom;

    @Enumerated(EnumType.STRING)
    private WeekDay day;

    private LocalTime startTime;

    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Section section;
}
