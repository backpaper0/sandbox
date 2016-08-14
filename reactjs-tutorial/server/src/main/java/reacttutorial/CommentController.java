package reacttutorial;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    CopyOnWriteArrayList<Comment> data = new CopyOnWriteArrayList<>();

    @GetMapping
    List<Comment> comments() {
        return data;
    }

    @PostMapping
    void post(@RequestParam String author,
            @RequestParam String text) {
        Comment c = new Comment();
        c.author = author;
        c.text = text;
        data.add(c);
    }

    public static class Comment {
        public String author;
        public String text;
    }
}
