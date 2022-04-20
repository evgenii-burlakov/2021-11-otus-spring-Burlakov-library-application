package ru.otus.libraryapplication;

import ru.otus.libraryapplication.domain.*;

import java.util.List;

public final class LibraryUnitTestData {
    public static final Author AUTHOR1 = new Author(1L, "PUSHKIN");
    public static final Author AUTHOR2 = new Author(2L, "MONTGOMERY");

    public static final Genre GENRE1 = new Genre(1L, "POEM");
    public static final Genre GENRE2 = new Genre(2L, "NOVEL");

    public static final Book BOOK1 = new Book(1L, "EVGENII ONEGIN", AUTHOR1, GENRE1);
    public static final Book BOOK2 = new Book(2L, "ANNE OF GREEN GABLES", AUTHOR2, GENRE2);
    public static final Book BOOK3 = new Book(3L, "ANNE OF GREEN GABLES POEM EDITION", AUTHOR2, GENRE1);

    public static final Comment COMMENT1 = new Comment(1L, "ЧИТАЛ ЕЕ В ШКОЛЕ", BOOK1);
    public static final Comment COMMENT2 = new Comment(2L, "Пушкин ван лав", BOOK1);
    public static final Comment COMMENT3 = new Comment(3L, "СкУчНоТиЩа", BOOK1);

    public static final User USER = new User(1L, "USER", "PASSWORD");

    public static final Role ROLE_USER = new Role(1L, "USER", USER);
    static {
        USER.setRoles(List.of(ROLE_USER));
    }
}
