package ru.isu.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.isu.taskmanager.model.Comment;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    // Найти все комментарии по ID задачи, сортировка по времени (новые первыми)
    @Query("SELECT c FROM Comment c WHERE c.task.id = :taskId ORDER BY c.timestamp DESC")
    List<Comment> findCommentsByTaskId(@Param("taskId") Integer taskId);
}