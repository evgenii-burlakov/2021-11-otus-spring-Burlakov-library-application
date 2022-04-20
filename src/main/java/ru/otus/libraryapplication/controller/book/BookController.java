package ru.otus.libraryapplication.controller.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.libraryapplication.dto.AuthorDto;
import ru.otus.libraryapplication.dto.BookDto;
import ru.otus.libraryapplication.dto.CommentDto;
import ru.otus.libraryapplication.dto.GenreDto;
import ru.otus.libraryapplication.service.author.AuthorService;
import ru.otus.libraryapplication.service.book.BookService;
import ru.otus.libraryapplication.service.comment.CommentService;
import ru.otus.libraryapplication.service.genre.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final CommentService commentService;

    @GetMapping("/books")
    public String getAllBooks(Model model) {
        List<BookDto> books = bookService.getAll().stream()
                .map(BookDto::toDto)
                .collect(Collectors.toList());
        model.addAttribute("books", books);
        return "books";
    }

    @PostMapping("/books/delete")
    public String deleteBookById(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

    @GetMapping("/books/edit")
    public String editPage(@RequestParam("id") Long id, Model model) {
        BookDto book = BookDto.toDto(bookService.getById(id));
        model.addAttribute("book", book);
        List<AuthorDto> authors = authorService.getAll().stream()
                .map(AuthorDto::toDto)
                .collect(Collectors.toList());
        model.addAttribute("authors", authors);
        List<GenreDto> genres = genreService.getAll().stream()
                .map(GenreDto::toDto)
                .collect(Collectors.toList());
        model.addAttribute("genres", genres);
        return "editBook";
    }

    @PostMapping("/books/edit")
    public String updateBook(BookDto book) {
        bookService.update(book.getId(), book.getName(), book.getAuthor().getName(), book.getGenre().getName());
        return "redirect:/books";
    }

    @GetMapping("/books/get/{id}")
    public String getPage(@PathVariable("id") Long id, Model model) {
        BookDto book = BookDto.toDto(bookService.getById(id));
        model.addAttribute("book", book);
        List<CommentDto> comments = commentService.getAllByBookId(id).stream()
                .map(CommentDto::toDto)
                .collect(Collectors.toList());
        model.addAttribute("comments", comments);
        return "bookPage";
    }

    @GetMapping("/books/create")
    public String createPage(Model model) {
        List<AuthorDto> authors = authorService.getAll().stream()
                .map(AuthorDto::toDto)
                .collect(Collectors.toList());
        model.addAttribute("authors", authors);
        List<GenreDto> genres = genreService.getAll().stream()
                .map(GenreDto::toDto)
                .collect(Collectors.toList());
        model.addAttribute("genres", genres);
        return "createBook";
    }

    @PostMapping("/books/create")
    public String createBook(BookDto book) {
        bookService.create(book.getName(), book.getAuthor().getName(), book.getGenre().getName());
        return "redirect:/books";
    }
}
