package ru.otus.libraryapplication.service.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.otus.libraryapplication.repositories.author.AuthorRepository;
import ru.otus.libraryapplication.repositories.book.BookRepository;
import ru.otus.libraryapplication.repositories.genre.GenreRepository;
import ru.otus.libraryapplication.repositories.user.UserRepository;
import ru.otus.libraryapplication.service.comment.CommentService;
import ru.otus.libraryapplication.service.string.StringService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.security.core.userdetails.User.withUsername;
import static ru.otus.libraryapplication.LibraryUnitTestData.USER;

@SpringBootTest
@DisplayName("Сервис для работы с пользователями должен ")
class UserDetailsServiceImplTest {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private StringService stringService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("корректно возвращать пользователя по его имени")
    void correctLoadUserByUsername() {
        Mockito.when(userRepository.findByUsername("USER")).thenReturn(USER);

        UserDetails expected = withUsername("USER")
                .password("USER")
                .roles("USER")
                .build();

        UserDetails actual = userDetailsService.loadUserByUsername("USER");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("выкидывать ошибку, если пользователя не существует")
    void throwExceptionIfUserNotExist() {
        Mockito.when(userRepository.findByUsername("USER11")).thenReturn(null);

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("USER11")).isInstanceOf(UsernameNotFoundException.class);
    }
}