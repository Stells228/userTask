<template>
  <div class="add-comment-panel">
    <textarea class="text-area-field" rows="3" placeholder="Напишите комментарий..." v-model="commentText"></textarea>

    <button :disabled="!commentText.trim()" @click="sendCommentData" class="btn-send-comment">
      Опубликовать
    </button>
  </div>
</template>

<script>
import CommentService from '@/services/commentService';

export default {
  name: 'CommentForm',

  data() {
    return {
      commentText: ''
    };
  },

  props: {
    taskId: {
      type: Number,
      required: true
    },
    userId: {
      type: Number,
      required: true
    }
  },

  methods: {
    sendCommentData() {
      if (!this.commentText.trim()) return;
      CommentService.createComment(
        this.commentText,
        this.taskId,
        this.userId
      ).then(() => {
        this.commentText = '';
        this.$emit('comment-added'); // Событие оставляем для совместимости
      }).catch(err => {
        console.error('Ошибка добавления комментария:', err);
        alert('Не удалось добавить комментарий');
      });
    }
  }
};
</script>

<style scoped>
.text-area-field {
  width: 100%;
  padding: 10px;
  border: 1px solid #d7d5d5;
  border-radius: 8px;
  font-family: inherit;
  font-size: 14px;
  resize: vertical;
}

.text-area-field:focus {
  outline: none;
  border-color: #3cc386;
}

.add-comment-panel {
  margin: 20px 0;
  padding: 15px;
  background: #faf7f7;
  border-radius: 8px;
}

.btn-send-comment {
  margin-top: 10px;
  padding: 6px 18px;
  background: #42b983;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: background 0.2s;
}

.btn-send-comment:hover:not(:disabled) {
  background: #267d56;
}

.btn-send-comment:disabled {
  background: #ccc;
  cursor: not-allowed;
}
</style>