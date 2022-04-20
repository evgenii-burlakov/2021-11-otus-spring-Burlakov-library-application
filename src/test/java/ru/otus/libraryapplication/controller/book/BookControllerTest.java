package ru.otus.libraryapplication.controller.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.libraryapplication.dto.AuthorDto;
import ru.otus.libraryapplication.dto.BookDto;
import ru.otus.libraryapplication.dto.CommentDto;
import ru.otus.libraryapplication.dto.GenreDto;
import ru.otus.libraryapplication.service.author.AuthorService;
import ru.otus.libraryapplication.service.book.BookService;
import ru.otus.libraryapplication.service.comment.CommentService;
import ru.otus.libraryapplication.service.genre.GenreService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.otus.libraryapplication.LibraryUnitTestData.*;

@WebMvcTest(BookController.class)
@DisplayName("Контроллер для работы с книгами должен ")
class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;
    @MockBean
    private CommentService commentService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(username = "USER", roles = "ADMIN")
    @DisplayName("корректно возвращать все книги")
    void correctGetAllBooks() throws Exception {
        given(bookService.getAll()).willReturn(List.of(BOOK1, BOOK2, BOOK3));

        List<BookDto> expectedResult = Stream.of(BOOK1, BOOK2, BOOK3)
                .map(BookDto::toDto).collect(Collectors.toList());

        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", expectedResult));
    }

    @Test
    @DisplayName("без аутентификации не возвращать все книги")
    void dontGetAllBooksWithoutAuthentication() throws Exception {
        mvc.perform(get("/books"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "USER", roles = "ADMIN")
    @DisplayName("корректно удалять книгу")
    void correctDeleteBookById() throws Exception {
        mvc.perform(post("/books/delete")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
        Mockito.verify(bookService, times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(username = "USER", roles = "USER")
    @DisplayName("без авторизации не удалять книгу")
    void dontDeleteBookByIdWithoutAuthentication() throws Exception {
        mvc.perform(post("/books/delete")
                        .param("id", "1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "USER", roles = "ADMIN")
    @DisplayName("корректно возвращать страницу редактирования книги")
    void correctReturnEditPage() throws Exception {
        given(bookService.getById(1L)).willReturn(BOOK1);
        BookDto expectedResult = BookDto.toDto(BOOK1);

        given(authorService.getAll()).willReturn(List.of(AUTHOR1, AUTHOR2));
        List<AuthorDto> expectedAuthors = Stream.of(AUTHOR1, AUTHOR2)
                .map(AuthorDto::toDto)
                .collect(Collectors.toList());

        given(genreService.getAll()).willReturn(List.of(GENRE1, GENRE2));
        List<GenreDto> expectedGenres = Stream.of(GENRE1, GENRE2)
                .map(GenreDto::toDto)
                .collect(Collectors.toList());

        mvc.perform(get("/books/edit?id=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editBook"))
                .andExpect(model().size(3))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", expectedResult))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", expectedAuthors))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", expectedGenres));
    }

    @Test
    @DisplayName("без аутентификации не возвращать страницу редактирования книги")
    void dontReturnEditPageWithoutAuthentication() throws Exception {
        mvc.perform(get("/books/edit?id=1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "USER", roles = "ADMIN")
    @DisplayName("корректно редактировать книгу")
    void correctUpdateBook() throws Exception {
        mvc.perform(post("/books/edit")
                        .param("id", "1")
                        .param("name", "We")
                        .param("author.name", "Zamiatin")
                        .param("genre.name", "Fantasy"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
        Mockito.verify(bookService, times(1)).update(1, "We", "Zamiatin", "Fantasy");
    }

    @Test
    @DisplayName("без аутентификации не редактировать книгу")
    void dontUpdateBookWithoutAuthentication() throws Exception {
        mvc.perform(post("/books/edit")
                        .param("id", "1")
                        .param("name", "We")
                        .param("author.name", "Zamiatin")
                        .param("genre.name", "Fantasy"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "USER", roles = "ADMIN")
    @DisplayName("корректно возвращать страницу книги")
    void correctGetBookPage() throws Exception {
        given(bookService.getById(1L)).willReturn(BOOK1);
        given(commentService.getAllByBookId(1L)).willReturn(List.of(COMMENT1, COMMENT2, COMMENT3));

        List<CommentDto> expectedResult = Stream.of(COMMENT1, COMMENT2, COMMENT3)
                .map(CommentDto::toDto).collect(Collectors.toList());

        mvc.perform(get("/books/get/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookPage"))
                .andExpect(model().attributeExists("book", "comments"))
                .andExpect(model().attribute("book", BookDto.toDto(BOOK1)))
                .andExpect(model().attribute("comments", expectedResult));
    }

    @Test
    @DisplayName("без аутентификации не возвращать страницу книги")
    void dontGetBookPageWithoutAuthentication() throws Exception {
        mvc.perform(get("/books/get/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "USER", roles = "ADMIN")
    @DisplayName("корректно возвращать страницу создания книги")
    void correctReturnCreatePage() throws Exception {
        given(authorService.getAll()).willReturn(List.of(AUTHOR1, AUTHOR2));
        List<AuthorDto> expectedAuthors = Stream.of(AUTHOR1, AUTHOR2)
                .map(AuthorDto::toDto)
                .collect(Collectors.toList());

        given(genreService.getAll()).willReturn(List.of(GENRE1, GENRE2));
        List<GenreDto> expectedGenres = Stream.of(GENRE1, GENRE2)
                .map(GenreDto::toDto)
                .collect(Collectors.toList());

        mvc.perform(get("/books/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createBook"))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", expectedAuthors))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", expectedGenres));
    }

    @Test
    @DisplayName("без аутентификации не возвращать страницу создания книги")
    void dontReturnCreatePageWithoutAuthentication() throws Exception {
        mvc.perform(get("/books/create"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "USER", roles = "ADMIN")
    @DisplayName("корректно создавать книгу")
    void correctCreateBook() throws Exception {
        mvc.perform(post("/books/create")
                        .param("name", "We")
                        .param("author.name", "Zamiatin")
                        .param("genre.name", "Fantasy"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
        Mockito.verify(bookService, times(1)).create("We", "Zamiatin", "Fantasy");
    }

    @Test
    @DisplayName("аутентификации не создавать книгу")
    void dontCreateBookWithoutAuthentication() throws Exception {
        mvc.perform(post("/books/create")
                        .param("name", "We")
                        .param("author.name", "Zamiatin")
                        .param("genre.name", "Fantasy"))
                .andExpect(status().is3xxRedirection());
    }
}