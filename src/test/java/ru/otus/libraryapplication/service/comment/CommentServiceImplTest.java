package ru.otus.libraryapplication.service.comment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.otus.libraryapplication.domain.Comment;
import ru.otus.libraryapplication.repositories.author.AuthorRepository;
import ru.otus.libraryapplication.repositories.book.BookRepository;
import ru.otus.libraryapplication.repositories.comment.CommentRepository;
import ru.otus.libraryapplication.repositories.genre.GenreRepository;
import ru.otus.libraryapplication.service.book.BookService;
import ru.otus.libraryapplication.service.string.StringService;
import ru.otus.libraryapplication.util.exeption.ApplicationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static ru.otus.libraryapplication.LibraryUnitTestData.*;

@SpringBootTest
@DisplayName("Сервис для работы с комментариями должен ")
class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    @MockBean
    private StringService stringService;

    @MockBean
    private BookService bookService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Test
    @DisplayName("корректно возвращать список комментариев для книги")
    void shouldCorrectGetAllComments() {
        List<Comment> expectedComments = List.of(COMMENT1, COMMENT2, COMMENT3);
        Mockito.when(bookService.getById(1L)).thenReturn(BOOK1);
        Mockito.when(commentRepository.getAllByBook(BOOK1)).thenReturn(List.of(COMMENT1, COMMENT2, COMMENT3));
        List<Comment> actualComments = commentService.getAllByBookId(1L);
        assertThat(actualComments).usingRecursiveComparison().isEqualTo(expectedComments);
    }

    @Test
    @DisplayName("возвращать ошибку при получении списока комментариев для несуществующей книги")
    void shouldThrowExceptionWhenBookNotExist() {
        Mockito.when(bookService.getById(5L)).thenReturn(null);

        assertThatThrownBy(() -> commentService.getAllByBookId(5L)).isInstanceOf(ApplicationException.class);

        Mockito.verify(commentRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }

    @Test
    @DisplayName("корректно удалять комментарий")
    void shouldCorrectDeleteComment() {
        commentService.deleteById(1L);
        Mockito.verify(commentRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("корректно обновлять комментарий")
    void shouldCorrectUpdateComment() {
        Mockito.when(stringService.verifyNotBlank("Это что за книга?")).thenReturn(true);
        Mockito.when(bookService.getById(1L)).thenReturn(BOOK1);
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(COMMENT1));

        commentService.update(1L, "Это что за книга?", 1L);

        Mockito.verify(commentRepository, Mockito.times(1)).save(new Comment(1L, "Это что за книга?", BOOK1));
    }

    @Test
    @DisplayName("не обновлять комментарий при не валидном значении")
    void shouldNotUpdateBlankComment() {
        Mockito.when(stringService.verifyNotBlank("    ")).thenReturn(false);

        assertThatThrownBy(() -> commentService.update(1, "    ", 1L)).isInstanceOf(ApplicationException.class);

        Mockito.verify(commentRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("не обновлять комментарий при отсутствии книги")
    void shouldNotUpdateCommentNotExistBook() {
        Mockito.when(stringService.verifyNotBlank("Это что за книга?")).thenReturn(true);
        Mockito.when(bookService.getById(5L)).thenReturn(null);

        assertThatThrownBy(() -> commentService.update(1L, "Это что за книга?", 5L)).isInstanceOf(ApplicationException.class);

        Mockito.verify(commentRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("не обновлять не существующий комментарий")
    void shouldNotUpdateNotExistComment() {
        Mockito.when(stringService.verifyNotBlank("Это что за книга?")).thenReturn(true);
        Mockito.when(bookService.getById(1L)).thenReturn(BOOK1);
        Mockito.when(commentRepository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.update(5L, "Это что за книга?", 1L)).isInstanceOf(ApplicationException.class);

        Mockito.verify(commentRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("корректно создавать комментарий")
    void shouldCorrectCreateComment() {
        Mockito.when(stringService.verifyNotBlank("Это что за книга?")).thenReturn(true);
        Mockito.when(bookService.getById(1L)).thenReturn(BOOK1);

        commentService.create( "Это что за книга?", 1L);

        Mockito.verify(commentRepository, Mockito.times(1)).save(new Comment(null, "Это что за книга?", BOOK1));
    }

    @Test
    @DisplayName("не создавать комментарий при не валидном значении")
    void shouldNotCreateBlankComment() {
        Mockito.when(stringService.verifyNotBlank("    ")).thenReturn(false);

        assertThatThrownBy(() -> commentService.create("    ", 1L)).isInstanceOf(ApplicationException.class);

        Mockito.verify(commentRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("не создавать комментарий при отсутствии книги")
    void shouldNotCreateCommentNotExistBook() {
        Mockito.when(stringService.verifyNotBlank("Это что за книга?")).thenReturn(true);
        Mockito.when(bookService.getById(5L)).thenReturn(null);

        assertThatThrownBy(() -> commentService.create("Это что за книга?", 5L)).isInstanceOf(ApplicationException.class);

        Mockito.verify(commentRepository, Mockito.never()).save(Mockito.any());
    }
}