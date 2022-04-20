package ru.otus.libraryapplication.domain;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "user")
@ToString(exclude = "user")
@Entity
@Table(name = "ROLES")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ROLE", nullable = false, unique = true)
    private String role;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}
