package ru.otus.libraryapplication.domain;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "book")
@ToString(exclude = "book")
@Entity
@Table(name = "COMMENTS")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "COMMENT", nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID")
    private Book book;
}
