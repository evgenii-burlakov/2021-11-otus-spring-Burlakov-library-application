package ru.otus.libraryapplication.controller.comment;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.libraryapplication.dto.CommentDto;
import ru.otus.libraryapplication.service.comment.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/comments/create")
    public String createPage(@RequestParam("bookId") Long bookId, Model model) {
        model.addAttribute("bookId", bookId);
        return "createComment";
    }

    @PostMapping("/comments/create")
    public String createComment(CommentDto comment, RedirectAttributes redirectAttributes) {
        commentService.create(comment.getComment(), comment.getBook().getId());
        redirectAttributes.addAttribute("id", comment.getBook().getId());
        return "redirect:/books/get/{id}";
    }

    @GetMapping("/comments/edit")
    public String editPage(@RequestParam("id") Long id, Model model) {
        CommentDto comment = CommentDto.toDto(commentService.getById(id));
        model.addAttribute("comment", comment);
        return "editComment";
    }

    @PostMapping("/comments/edit")
    public String updateComment(CommentDto comment, RedirectAttributes redirectAttributes) {
        commentService.update(comment.getId(), comment.getComment(), comment.getBook().getId());
        redirectAttributes.addAttribute("id", comment.getBook().getId());
        return "redirect:/books/get/{id}";
    }

    @PostMapping("/comments/delete")
    public String deleteComment(@RequestParam("id") long id, @RequestParam("bookId") long bookId, RedirectAttributes redirectAttributes) {
        commentService.deleteById(id);
        redirectAttributes.addAttribute("id", bookId);
        return "redirect:/books/get/{id}";
    }
}
