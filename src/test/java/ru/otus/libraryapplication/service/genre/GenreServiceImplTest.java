package ru.otus.libraryapplication.service.genre;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.otus.libraryapplication.domain.Genre;
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
import static ru.otus.libraryapplication.LibraryUnitTestData.GENRE1;
import static ru.otus.libraryapplication.LibraryUnitTestData.GENRE2;

@SpringBootTest
@DisplayName("Сервис для работы с жанрами должен ")
class GenreServiceImplTest {
    @Autowired
    private GenreServiceImpl genreService;

    @MockBean
    private StringService stringService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private BookRepository bookRepository;

    @Test
    @DisplayName("корректно возвращать список всех жанров")
    void shouldCorrectGetAllGenres() {
        List<Genre> expectedGenres = List.of(GENRE1, GENRE2);
        Mockito.when(genreRepository.findAll()).thenReturn(expectedGenres);
        List<Genre> actualGenres = genreService.getAll();
        assertThat(actualGenres).usingRecursiveComparison().isEqualTo(expectedGenres);
    }

    @Test
    @DisplayName("корректно возвращать жанр по ИД")
    void shouldCorrectGetGenreById() {
        Mockito.when(genreRepository.findById(1)).thenReturn(Optional.of(GENRE1));
        Genre actualGenre = genreService.getById(1);
        assertThat(actualGenre).isEqualTo(GENRE1);
    }

    @Test
    @DisplayName("корректно возвращать жанр названию")
    void shouldCorrectGetGenreByName() {
        Mockito.when(genreRepository.findByName("POEM")).thenReturn(Optional.of(GENRE1));
        Genre actualGenre = genreService.getByName("POEM");
        assertThat(actualGenre).isEqualTo(GENRE1);
    }

    @Test
    @DisplayName("корректно удалять жанр по ИД")
    void shouldCorrectDeleteGenreAndUnusedAuthorsById() {
        genreService.deleteById(2);
        Mockito.verify(genreRepository, Mockito.times(1)).deleteById(2);
    }

    @Test
    @DisplayName("корректно обновлять жанр")
    void shouldCorrectUpdateGenre() {
        Mockito.when(stringService.beautifyStringName("detective")).thenReturn("DETECTIVE");
        Mockito.when(stringService.verifyNotBlank("DETECTIVE")).thenReturn(true);
        Mockito.when(genreRepository.findById(1)).thenReturn(Optional.of(GENRE1));
        genreService.update(1, "detective");
        Mockito.verify(stringService, Mockito.times(1)).beautifyStringName("detective");
        Mockito.verify(genreRepository, Mockito.times(1)).save(new Genre(1L, "DETECTIVE"));
    }

    @Test
    @DisplayName("не обновлять жанр при не валидном наименовании")
    void shouldNotUpdateBlankGenreName() {
        Mockito.when(stringService.beautifyStringName("  ")).thenReturn("");
        Mockito.when(stringService.verifyNotBlank("")).thenReturn(false);
        assertThatThrownBy(() -> genreService.update(1, "  ")).isInstanceOf(ApplicationException.class);
        Mockito.verify(stringService, Mockito.times(1)).beautifyStringName("  ");
        Mockito.verify(stringService, Mockito.times(1)).verifyNotBlank("");
        Mockito.verify(genreRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("не обновлять жанр, если его нет в БД")
    void shouldNotUpdateNotExistGenre() {
        Mockito.when(stringService.beautifyStringName("detective")).thenReturn("DETECTIVE");
        Mockito.when(stringService.verifyNotBlank("DETECTIVE")).thenReturn(true);
        Mockito.when(genreRepository.findById(5)).thenReturn(null);

        assertThatThrownBy(() -> genreService.update(5, "lermontov")).isInstanceOf(ApplicationException.class);
        Mockito.verify(stringService, Mockito.times(1)).beautifyStringName("lermontov");
        Mockito.verify(authorRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    @DisplayName("корректно создавать жанр")
    void shouldCorrectCreateGenre() {
        Mockito.when(stringService.beautifyStringName("detective")).thenReturn("DETECTIVE");
        Mockito.when(stringService.verifyNotBlank("DETECTIVE")).thenReturn(true);
        genreService.create("detective");
        Mockito.verify(stringService, Mockito.times(1)).beautifyStringName("detective");
        Mockito.verify(genreRepository, Mockito.times(1)).save(new Genre(null, "DETECTIVE"));
    }

    @Test
    @DisplayName("не создавать не валидный жанр")
    void shouldNotCreateNullGenre() {
        Mockito.when(stringService.beautifyStringName(null)).thenReturn(null);
        Mockito.when(stringService.verifyNotBlank((String) null)).thenReturn(false);
        assertThatThrownBy(() -> genreService.create(null)).isInstanceOf(ApplicationException.class);
        Mockito.verify(stringService, Mockito.times(1)).beautifyStringName(null);
        Mockito.verify(genreRepository, Mockito.never()).save(Mockito.any());
    }
}