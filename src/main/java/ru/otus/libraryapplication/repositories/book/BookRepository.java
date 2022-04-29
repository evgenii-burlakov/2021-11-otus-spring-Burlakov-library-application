package ru.otus.libraryapplication.repositories.book;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.libraryapplication.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = {"author", "genre"})
    List<Book> findAll();

    boolean existsBy();

    Optional<Book> findById(long id);

    void deleteById(long id);

    Book save(Book book);

    @Query("select count(*) > 0 from Book b " +
            "inner join Author a on b.author=a.id " +
            "inner join Genre g on b.genre=g.id " +
            "where b.name = :bookName " +
            "and a.name = :author " +
            "and g.name = :genre")
    boolean existByBookAuthorAndGenreNames(@Param("bookName") String bookName, @Param("author") String author, @Param("genre") String genre);
}
