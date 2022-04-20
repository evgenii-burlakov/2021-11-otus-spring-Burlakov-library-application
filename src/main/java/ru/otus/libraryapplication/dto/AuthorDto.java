package ru.otus.libraryapplication.dto;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.otus.libraryapplication.domain.Author;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class AuthorDto {
    private Long id;

    private String name;

    public AuthorDto() {
    }

    public AuthorDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public AuthorDto(String name) {
        this.name = name;
    }

    public static AuthorDto toDto(Author author) {
        return AuthorDto.builder()
                .id(author.getId())
                .name(author.getName())
                .build();
    }

    public Author toBean() {
        return new Author(id, name);
    }
}
