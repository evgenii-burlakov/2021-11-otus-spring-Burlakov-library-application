package ru.otus.libraryapplication.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.otus.libraryapplication.domain.Comment;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class CommentDto {
    private Long id;

    private String comment;
    private BookDto book;

    public CommentDto() {
    }

    public CommentDto(Long id, String comment, BookDto book) {
        this.id = id;
        this.comment = comment;
        this.book = book;
    }

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .book(BookDto.toDto(comment.getBook()))
                .build();
    }

    public Comment toBean() {
        return new Comment(id, comment, book.toBean());
    }
}
