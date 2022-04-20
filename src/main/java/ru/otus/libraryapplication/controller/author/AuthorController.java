package ru.otus.libraryapplication.controller.author;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.libraryapplication.dto.AuthorDto;
import ru.otus.libraryapplication.service.author.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/authors")
    public String getAllAuthors(Model model) {
        List<AuthorDto> authors = authorService.getAll().stream()
                .map(AuthorDto::toDto)
                .collect(Collectors.toList());
        model.addAttribute("authors", authors);
        return "authors";
    }

    @PostMapping("/authors/delete")
    public String deleteAuthorById(@RequestParam("id") long id) {
        authorService.deleteById(id);
        return "redirect:/authors";
    }

    @GetMapping("/authors/edit")
    public String editPage(@RequestParam("id") Long id, Model model) {
        AuthorDto author = AuthorDto.toDto(authorService.getById(id));
        model.addAttribute("author", author);
        return "editAuthor";
    }

    @PostMapping("/authors/edit")
    public String updateAuthor(AuthorDto author) {
        authorService.update(author.getId(), author.getName());
        return "redirect:/authors";
    }

    @GetMapping("/authors/create")
    public String createPage() {
        return "createAuthor";
    }

    @PostMapping("/authors/create")
    public String createAuthor(AuthorDto author) {
        authorService.create(author.getName());
        return "redirect:/authors";
    }
}
