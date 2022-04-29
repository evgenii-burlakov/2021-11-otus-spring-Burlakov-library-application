package ru.otus.libraryapplication.dto;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.otus.libraryapplication.domain.Book;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class BookDto {
    private Long id;

    private String name;
    private AuthorDto author;
    private GenreDto genre;

    public BookDto() {
    }

    public BookDto(Long id, String name, AuthorDto author, GenreDto genre) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.genre = genre;
    }

    public static BookDto toDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .author(AuthorDto.toDto(book.getAuthor()))
                .genre(GenreDto.toDto(book.getGenre()))
                .build();
    }

    public Book toBean() {
        return new Book(id, name, author.toBean(), genre.toBean());
    }
}
