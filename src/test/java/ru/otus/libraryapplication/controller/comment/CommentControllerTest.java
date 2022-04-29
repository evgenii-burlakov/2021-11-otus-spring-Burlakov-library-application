package ru.otus.libraryapplication.controller.comment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.libraryapplication.controller.book.BookController;
import ru.otus.libraryapplication.dto.BookDto;
import ru.otus.libraryapplication.dto.CommentDto;
import ru.otus.libraryapplication.service.book.BookService;
import ru.otus.libraryapplication.service.comment.CommentService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.otus.libraryapplication.LibraryUnitTestData.BOOK1;
import static ru.otus.libraryapplication.LibraryUnitTestData.COMMENT1;

@WebMvcTest(CommentController.class)
@DisplayName("Контроллер для работы с комментариями должен ")
class CommentControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(username = "USER", roles = "USER")
    @DisplayName("корректно возвращать страницу создания комментария")
    void correctReturnCreatePage() throws Exception {
        mvc.perform(get("/comments/create?bookId=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("createComment"));
    }

    @Test
    @DisplayName("без аутентификации не возвращать страницу создания комментария")
    void dontReturnCreatePageWithoutAuthentication() throws Exception {
        mvc.perform(get("/comments/create?bookId=1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "USER", roles = "USER")
    @DisplayName("корректно создавать комментарий")
    void correctCreateComment() throws Exception {
        mvc.perform(post("/comments/create")
                        .param("comment", "Nice")
                        .param("book.id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/get/1"));
        Mockito.verify(commentService, times(1)).create("Nice", 1L);
    }

    @Test
    @DisplayName("без аутентификации не создавать комментарий")
    void dontCreateCommentWithoutAuthentication() throws Exception {
        mvc.perform(post("/comments/create")
                        .param("comment", "Nice")
                        .param("book.id", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "USER", roles = "USER")
    @DisplayName("корректно возвращать страницу редактирования комментария")
    void correctReturnEditPage() throws Exception {
        given(commentService.getById(1L)).willReturn(COMMENT1);
        CommentDto expectedResult = CommentDto.toDto(COMMENT1);

        mvc.perform(get("/comments/edit?id=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editComment"))
                .andExpect(model().attributeExists("comment"))
                .andExpect(model().attribute("comment", expectedResult));
    }

    @Test
    @DisplayName("без аутентификации не возвращать страницу редактирования комментария")
    void dontReturnEditPageWithoutAuthentication() throws Exception {
        mvc.perform(get("/comments/edit?id=1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "USER", roles = "USER")
    @DisplayName("корректно редактировать комментарий")
    void correctUpdateComment() throws Exception {
        mvc.perform(post("/comments/edit")
                        .param("id", "1")
                        .param("comment", "Nice")
                        .param("book.id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/get/1"));
        Mockito.verify(commentService, times(1)).update(1, "Nice", 1L);
    }

    @Test
    @DisplayName("без аутентификации не редактировать комментарий")
    void dontUpdateCommentWithoutAuthentication() throws Exception {
        mvc.perform(post("/comments/edit")
                        .param("id", "1")
                        .param("comment", "Nice")
                        .param("book.id", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "USER", roles = "ADMIN")
    @DisplayName("корректно удалять комментарий")
    void correctDeleteBookById() throws Exception {
        mvc.perform(post("/comments/delete")
                        .param("id", "1")
                        .param("bookId", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/get/2"));
        Mockito.verify(commentService, times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(username = "USER", roles = "USER")
    @DisplayName("без аутентификации не удалять комментарий")
    void dontDeleteBookByIdWithoutAuthentication() throws Exception {
        mvc.perform(post("/comments/delete")
                        .param("id", "1")
                        .param("bookId", "2"))
                .andExpect(status().isForbidden());
    }
}