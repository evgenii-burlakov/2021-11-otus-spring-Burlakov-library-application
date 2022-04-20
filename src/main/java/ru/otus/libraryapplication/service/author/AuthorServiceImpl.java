package ru.otus.libraryapplication.service.author;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.libraryapplication.domain.Author;
import ru.otus.libraryapplication.repositories.author.AuthorRepository;
import ru.otus.libraryapplication.service.string.StringService;
import ru.otus.libraryapplication.util.exeption.ApplicationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final StringService stringService;

    @Override
    @Transactional(readOnly = true)
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Author getById(long id) {
        return authorRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Author getByName(String author) {
        return authorRepository.findByName(author).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        authorRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(long id, String name) {
        String authorName = stringService.beautifyStringName(name);
        if (stringService.verifyNotBlank(authorName)) {
            authorRepository.findById(id).ifPresentOrElse(author -> {
                author.setName(authorName);
                authorRepository.save(author);
            }, () -> {
                throw new ApplicationException("Invalid author id");
            });
        } else {
            throw new ApplicationException("Invalid author name");
        }
    }

    @Override
    @Transactional
    public Author create(String name) {
        String authorName = stringService.beautifyStringName(name);
        if (stringService.verifyNotBlank(authorName)) {
            Author author = new Author(null, authorName);
            return authorRepository.save(author);
        }

        throw new ApplicationException("Invalid author name");
    }
}
