package ru.otus.libraryapplication.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.libraryapplication.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}