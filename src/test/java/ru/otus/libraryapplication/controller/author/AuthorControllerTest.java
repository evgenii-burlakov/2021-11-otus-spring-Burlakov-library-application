package ru.otus.libraryapplication.controller.author;

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
import ru.otus.libraryapplication.service.author.AuthorService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.otus.libraryapplication.LibraryUnitTestData.AUTHOR1;
import static ru.otus.libraryapplication.LibraryUnitTestData.AUTHOR2;

@WebMvcTest(AuthorController.class)
@DisplayName("Контроллер для работы с авторами должен ")
class AuthorControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(username = "USER", roles = "ADMIN")
    @DisplayName("корректно возвращать всех авторов")
    void correctGetAllAuthors() throws Exception {
        given(authorService.getAll()).willReturn(List.of(AUTHOR1, AUTHOR2));

        List<AuthorDto> expectedResult = Stream.of(AUTHOR1, AUTHOR2)
                .map(AuthorDto::toDto).collect(Collectors.toList());

        mvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("authors"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", expectedResult));
    }

    @Test
    @DisplayName("без аутентификации не возвращать авторов")
    void dontGetAllAuthorsWithoutAuthentication() throws Exception {
        mvc.perform(get("/authors"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("корректно удалять автора")
    @WithMockUser(username = "USER", roles = "ADMIN")
    void correctDeleteAuthorById() throws Exception {
        mvc.perform(post("/authors/delete")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authors"));
        Mockito.verify(authorService, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("без авторизации не удалять автора")
    @WithMockUser(username = "USER", roles = "USER")
    void dontDeleteAuthorByIdWithoutAuthentication() throws Exception {
        mvc.perform(post("/authors/delete")
                        .param("id", "1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "USER", roles = "ADMIN")
    @DisplayName("корректно возвращать страницу редактирования автора")
    void correctReturnEditPage() throws Exception {
        given(authorService.getById(1L)).willReturn(AUTHOR1);
        AuthorDto expectedResult = AuthorDto.toDto(AUTHOR1);

        mvc.perform(get("/authors/edit?id=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editAuthor"))
                .andExpect(model().attributeExists("author"))
                .andExpect(model().attribute("author", expectedResult));
    }

    @Test
    @WithMockUser(username = "USER", roles = "USER")
    @DisplayName("без авторизации не возвращать страницу редактирования автора")
    void dontReturnEditPageWithoutAuthentication() throws Exception {
        mvc.perform(get("/authors/edit?id=1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "USER", roles = "ADMIN")
    @DisplayName("корректно редактировать автора")
    void correctUpdateAuthor() throws Exception {
        mvc.perform(post("/authors/edit")
                        .param("id", "1")
                        .param("name", "Pushkin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authors"));
        Mockito.verify(authorService, times(1)).update(1, "Pushkin");
    }

    @Test
    @WithMockUser(username = "USER", roles = "USER")
    @DisplayName("без авторизации не редактировать автора")
    void dontUpdateAuthorWithoutAuthentication() throws Exception {
        mvc.perform(post("/authors/edit")
                        .param("id", "1")
                        .param("name", "Pushkin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "USER", roles = "ADMIN")
    @DisplayName("корректно возвращать страницу создания автора")
    void correctReturnCreatePage() throws Exception {
        mvc.perform(get("/authors/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createAuthor"));
    }

    @Test
    @WithMockUser(username = "USER", roles = "USER")
    @DisplayName("без авторизации не возвращать страницу создания автора")
    void dontReturnCreatePageWithoutAuthentication() throws Exception {
        mvc.perform(get("/authors/create"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "USER", roles = "ADMIN")
    @DisplayName("корректно создавать автора")
    void correctCreateAuthor() throws Exception {
        mvc.perform(post("/authors/create")
                        .param("name", "Pushkin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authors"));
        Mockito.verify(authorService, times(1)).create("Pushkin");
    }

    @Test
    @WithMockUser(username = "USER", roles = "USER")
    @DisplayName("без авторизации не создавать автора")
    void dontCreateAuthorWithoutAuthentication() throws Exception {
        mvc.perform(post("/authors/create")
                        .param("name", "Pushkin"))
                .andExpect(status().isForbidden());
    }
}