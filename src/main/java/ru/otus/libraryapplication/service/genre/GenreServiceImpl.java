package ru.otus.libraryapplication.service.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.libraryapplication.domain.Genre;
import ru.otus.libraryapplication.repositories.genre.GenreRepository;
import ru.otus.libraryapplication.service.string.StringService;
import ru.otus.libraryapplication.util.exeption.ApplicationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final StringService stringService;

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Genre getById(long id) {
        return genreRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Genre getByName(String name) {
        return genreRepository.findByName(name).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        genreRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(long id, String name) {
        String genreName = stringService.beautifyStringName(name);
        if (stringService.verifyNotBlank(genreName)) {
            genreRepository.findById(id).ifPresentOrElse(genre -> {
                genre.setName(genreName);
                genreRepository.save(genre);
            }, () -> {
                throw new ApplicationException("Invalid genre id");
            });
        } else {
            throw new ApplicationException("Invalid genre name");
        }
    }

    @Override
    @Transactional
    public Genre create(String name) {
        String genreName = stringService.beautifyStringName(name);
        if (stringService.verifyNotBlank(genreName)) {
            Genre genre = new Genre(null, genreName);
            return genreRepository.save(genre);
        }

        throw new ApplicationException("Invalid genre name");
    }
}
