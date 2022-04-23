package ru.otus.libraryapplication.repositories.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Репозиторий для работы с книгами должен ")
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @DisplayName("возвращать true, если такая книга уже есть в БД")
    @Test
    void shouldReturnTrueIfEqualBookExist() {
        boolean actual = bookRepository.existByBookAuthorAndGenreNames("EVGENII ONEGIN", "PUSHKIN", "POEM");
        assertThat(actual).isTrue();
    }

    @DisplayName("возвращать false, если такой книги нет в БД")
    @Test
    void shouldReturnFalseIfEqualBookNotExist() {
        boolean actual = bookRepository.existByBookAuthorAndGenreNames("EVGENII ONEGIN", "LERMONTOV", "POEM");
        assertThat(actual).isFalse();
    }
}