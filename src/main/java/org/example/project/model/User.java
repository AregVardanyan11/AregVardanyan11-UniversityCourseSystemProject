package org.example.project.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.project.model.enums.UserRole;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "userr")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column()
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String imagePath;


    private UserRole role;
}
