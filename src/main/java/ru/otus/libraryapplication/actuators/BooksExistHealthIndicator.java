package ru.otus.libraryapplication.actuators;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.libraryapplication.repositories.book.BookRepository;

@Component
@RequiredArgsConstructor
public class BooksExistHealthIndicator implements HealthIndicator {
    private final BookRepository bookRepository;

    @Override
    public Health health() {
        if (bookRepository.existsBy()) {
            return Health.up().withDetail("message", "There are books in the library!").build();
        } else {
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "There is no books in library!")
                    .build();
        }
    }
}
