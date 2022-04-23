package ru.otus.libraryapplication.service.comment;

import ru.otus.libraryapplication.domain.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getAllByBookId(Long bookId);

    Comment getById(Long id);

    void deleteById(long id);

    void update(long id, String comment, long bookId);

    Comment create(String comment, Long bookId);
}
