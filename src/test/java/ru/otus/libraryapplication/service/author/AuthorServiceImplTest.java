package ru.otus.libraryapplication.service.author;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.otus.libraryapplication.domain.Author;
import ru.otus.libraryapplication.repositories.author.AuthorRepository;
import ru.otus.libraryapplication.repositories.book.BookRepository;
import ru.otus.libraryapplication.repositories.genre.GenreRepository;
import ru.otus.libraryapplication.service.comment.CommentService;
import ru.otus.libraryapplication.service.string.StringService;
import ru.otus.libraryapplication.util.exeption.ApplicationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static ru.otus.libraryapplication.LibraryUnitTestData.AUTHOR1;
import static ru.otus.libraryapplication.LibraryUnitTestData.AUTHOR2;

@SpringBootTest
@DisplayName("Сервис для работы с авторами должен ")
class AuthorServiceImplTest {

    @Autowired
    private AuthorServiceImpl authorService;

    @MockBean
    private StringService stringService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Test
    @DisplayName("корректно возвращать список всех авторов")
    void shouldCorrectGetAllAuthors() {
        List<Author> expectedAuthors = List.of(AUTHOR1, AUTHOR2);
        Mockito.when(authorRepository.findAll()).thenReturn(expectedAuthors);
        List<Author> actualAuthors = authorService.getAll();
        assertThat(actualAuthors).usingRecursiveComparison().isEqualTo(expectedAuthors);
    }

    @Test
    @DisplayName("корректно возвращать автора по ИД")
    void shouldCorrectGetAuthorById() {
        Mockito.when(authorRepository.findById(1)).thenReturn(Optional.of(AUTHOR1));
        Author actualAuthor = authorService.getById(1);
        assertThat(actualAuthor).isEqualTo(AUTHOR1);
    }

    @Test
    @DisplayName("корректно возвращать автора названию")
    void shouldCorrectGetAuthorByName() {
        Mockito.when(authorRepository.findByName("PUSHKIN")).thenReturn(Optional.of(AUTHOR1));
        Author actualAuthor = authorService.getByName("PUSHKIN");
        assertThat(actualAuthor).isEqualTo(AUTHOR1);
    }

    @Test
    @DisplayName("корректно удалять автора по ИД")
    void shouldCorrectDeleteAuthorAndUnusedGenresById() {
        authorService.deleteById(2);
        Mockito.verify(authorRepository, Mockito.times(1)).deleteById(2);
    }

    @Test
    @DisplayName("корректно обновлять автора")
    void shouldCorrectUpdateAuthor() {
        Mockito.when(stringService.beautifyStringName("lermontov")).thenReturn("LERMONTOV");
        Mockito.when(stringService.verifyNotBlank("LERMONTOV")).thenReturn(true);
        Mockito.when(authorRepository.findById(1)).thenReturn(Optional.of(AUTHOR1));
        authorService.update(1, "lermontov");
        Mockito.verify(stringService, Mockito.times(1)).beautifyStringName("lermontov");
        Mockito.verify(authorRepository, Mockito.times(1)).save(new Author(1L, "LERMONTOV"));
    }

    @Test
    @DisplayName("не обновлять автора при не валидном наименовании")
    void shouldNotUpdateBlankAuthorName() {
        Mockito.when(stringService.beautifyStringName("  ")).thenReturn("");
        Mockito.when(stringService.verifyNotBlank("")).thenReturn(false);
        assertThatThrownBy(() -> authorService.update(1, "  ")).isInstanceOf(ApplicationException.class);
        Mockito.verify(stringService, Mockito.times(1)).beautifyStringName("  ");
        Mockito.verify(stringService, Mockito.times(1)).verifyNotBlank("");
        Mockito.verify(authorRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("не обновлять автора, если его нет в БД")
    void shouldNotUpdateNotExistAuthor() {
        Mockito.when(stringService.beautifyStringName("lermontov")).thenReturn("LERMONTOV");
        Mockito.when(stringService.verifyNotBlank("LERMONTOV")).thenReturn(true);
        Mockito.when(authorRepository.findById(5)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.update(5, "lermontov")).isInstanceOf(ApplicationException.class);
        Mockito.verify(stringService, Mockito.times(1)).beautifyStringName("lermontov");
        Mockito.verify(authorRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("корректно создавать автора")
    void shouldCorrectCreateAuthor() {
        Mockito.when(stringService.beautifyStringName("lermontov")).thenReturn("LERMONTOV");
        Mockito.when(stringService.verifyNotBlank("LERMONTOV")).thenReturn(true);
        authorService.create("lermontov");
        Mockito.verify(stringService, Mockito.times(1)).beautifyStringName("lermontov");
        Mockito.verify(authorRepository, Mockito.times(1)).save(new Author(null, "LERMONTOV"));
    }

    @Test
    @DisplayName("выкидывать ошибку при создании не валидного автора")
    void shouldNotCreateNullAuthor() {
        Mockito.when(stringService.beautifyStringName(null)).thenReturn(null);
        Mockito.when(stringService.verifyNotBlank((String) null)).thenReturn(false);
        assertThatThrownBy(() -> authorService.create(null)).isInstanceOf(ApplicationException.class);
        Mockito.verify(stringService, Mockito.times(1)).beautifyStringName(null);
        Mockito.verify(authorRepository, Mockito.never()).save(Mockito.any());
    }
}