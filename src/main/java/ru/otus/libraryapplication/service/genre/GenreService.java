package ru.otus.libraryapplication.service.genre;

import ru.otus.libraryapplication.domain.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> getAll();

    Genre getById(long id);

    Genre getByName(String name);

    void deleteById(long id);

    void update(long id, String name);

    Genre create(String name);
}
