package ru.otus.libraryapplication.repositories.genre;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.libraryapplication.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findAll();

    Optional<Genre> findById(long id);

    Optional<Genre> findByName(String name);

    void deleteById(long id);

    Genre save(Genre genre);
}
