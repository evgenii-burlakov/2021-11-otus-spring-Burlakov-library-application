package ru.otus.libraryapplication.dto;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.otus.libraryapplication.domain.Genre;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class GenreDto {
    private Long id;

    private String name;

    public GenreDto() {
    }

    public GenreDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public GenreDto(String name) {
        this.name = name;
    }

    public static GenreDto toDto(Genre genre) {
        return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

    public Genre toBean() {
        return new Genre(id, name);
    }
}
