package ru.isu.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isu.taskmanager.model.*;
import java.util.List;
import java.util.stream.Collectors;
import ru.isu.taskmanager.repository.CommentRepository;
import ru.isu.taskmanager.repository.TaskRepository;
import ru.isu.taskmanager.repository.UserRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // Получить все комментарии к задаче
    @GetMapping("/comments/task/{taskId}")
    public ResponseEntity<List<CommentResponse>> fetchCommentsByTask(@PathVariable Integer taskId) {
        List<Comment> comments = commentRepository.findCommentsByTaskId(taskId);
        List<CommentResponse> response = comments.stream()
                .map(comment -> new CommentResponse(comment.getId(), comment.getContent(), comment.getTimestamp(),
                        comment.getUser().getUsername(), comment.getUser().getId(), comment.getTask().getId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/comments")
    public ResponseEntity<?> createComment(@RequestBody CommentRequest commentRequest) {
        Task task = taskRepository.findById(commentRequest.getTaskId()).orElse(null);
        if (task == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Задача не найдена"));
        }
        User user = userRepository.findById(commentRequest.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Пользователь не найден"));
        }

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setTask(task);
        comment.setUser(user);
        Comment savedComment = commentRepository.save(comment);

        CommentResponse response = new CommentResponse(savedComment.getId(), savedComment.getContent(),
                savedComment.getTimestamp(), user.getUsername(), user.getId(), task.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer commentId) {
        if (!commentRepository.existsById(commentId)) {
            return ResponseEntity.notFound().build();
        }
        commentRepository.deleteById(commentId);
        return ResponseEntity.ok(new MessageResponse("Комментарий удалён"));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Integer commentId,
            @RequestBody CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }

        comment.setContent(commentRequest.getContent());
        Comment updatedComment = commentRepository.save(comment);

        CommentResponse response = new CommentResponse(updatedComment.getId(), updatedComment.getContent(),
                updatedComment.getTimestamp(), comment.getUser().getUsername(), comment.getUser().getId(),
                comment.getTask().getId());
        return ResponseEntity.ok(response);
    }
}
