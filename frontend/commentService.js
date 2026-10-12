import axios from "axios";

const API_BASE = "http://localhost:8080/api/";

class CommentService {
  fetchCommentsByTask(taskId) {
    return axios.get(`${API_BASE}comments/task/${taskId}`);
  }
  createComment(content, taskId, userId) {
    return axios.post(API_BASE + "comments", {
      content: content,
      taskId: taskId,
      userId: userId,
    });
  }
  removeComment(commentId) {
    return axios.delete(`${API_BASE}comments/${commentId}`);
  }
  updateComment(commentId, content, taskId, userId) {
    return axios.put(API_BASE + `comments/${commentId}`, {
      content: content,
      taskId: taskId,
      userId: userId,
    });
  }
}

export default new CommentService();
