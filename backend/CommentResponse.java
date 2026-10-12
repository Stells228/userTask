package ru.isu.taskmanager.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentResponse {
    private Integer id;
    private String content;
    private LocalDateTime timestamp;
    private String username;
    private Integer userId;
    private Integer taskId;

    public CommentResponse(Integer id, String content, LocalDateTime timestamp,
                           String username, Integer userId, Integer taskId) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.username = username;
        this.userId = userId;
        this.taskId = taskId;
    }
}