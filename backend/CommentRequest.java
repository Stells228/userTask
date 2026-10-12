package ru.isu.taskmanager.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentRequest {
    private String content;
    private Integer taskId;
    private Integer userId;
}
