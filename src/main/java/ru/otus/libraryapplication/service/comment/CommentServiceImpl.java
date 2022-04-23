package ru.otus.libraryapplication.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.libraryapplication.domain.Book;
import ru.otus.libraryapplication.domain.Comment;
import ru.otus.libraryapplication.repositories.comment.CommentRepository;
import ru.otus.libraryapplication.service.book.BookService;
import ru.otus.libraryapplication.service.string.StringService;
import ru.otus.libraryapplication.util.exeption.ApplicationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BookService bookService;
    private final StringService stringService;

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getAllByBookId(Long bookId) {
        Book book = bookService.getById(bookId);
        if (book != null) {
            return commentRepository.getAllByBook(book);
        } else {
            throw new ApplicationException("Invalid book id");
        }
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Comment getById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void update(long id, String stringComment, long bookId) {
        if (stringService.verifyNotBlank(stringComment)) {
            Book book = bookService.getById(bookId);
            if (book != null) {
                commentRepository.findById(id).ifPresentOrElse(comment -> {
                    comment.setComment(stringComment);
                    comment.setBook(book);
                    commentRepository.save(comment);
                }, () -> {
                    throw new ApplicationException("Invalid comment id");
                });
            } else {
                throw new ApplicationException("Invalid book id");
            }
        } else {
            throw new ApplicationException("Invalid comment");
        }
    }

    @Override
    @Transactional
    public Comment create(String comment, Long bookId) {
        if (stringService.verifyNotBlank(comment)) {
            Book book = bookService.getById(bookId);
            if (book != null) {
                Comment newComment = new Comment(null, comment, book);
                return commentRepository.save(newComment);
            } else {
                throw new ApplicationException("Invalid book id");
            }
        } else {
            throw new ApplicationException("Invalid comment");
        }
    }
}
