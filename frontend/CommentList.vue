<template>
  <div class="task-comments">
    <h3>Обсуждение задачи ({{ items.length }})</h3>
    <div class="comments-empty" v-if="items.length === 0">
      Здесь пока пусто. Начни обсуждение
    </div>
    <div class="comments-feed" v-else>
      <div class="comment" :key="item.id" v-for="item in items">
        <div class="comment-top">
          <strong>{{ item.username }}</strong>
          <span class="comment-time">{{ formatTime(item.timestamp) }}</span>
          <div class="comment-nav" v-if="isOwner(item.userId)">
            <button class="btn-edit" @click="handleEdit(item)">Изменить</button>
            <button class="btn-delete" @click="handleDelete(item.id)">Удалить</button>
          </div>
        </div>
        <div class="comment-editing-block" v-if="activeEditId === item.id">
          <textarea class="textarea-field" v-model="editText"></textarea>
          <div class="editing-buttons">
            <button class="btn-save" @click="saveChanges">Сохранить</button>
            <button class="btn-cancel" @click="closeEdit">Отмена</button>
          </div>
        </div>
        <div class="comment-text" v-else>
          {{ item.content }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import CommentService from '@/services/commentService';

export default {
  name: 'CommentList',
  data() {
    return {
      items: [],
      activeEditId: null,
      editText: ''
    };
  },

  props: {
    taskId: {
      type: Number,
      required: true
    },
    currentUserId: {
      type: Number,
      required: true
    }
  },

  methods: {
    getComments() {
      CommentService.fetchCommentsByTask(this.taskId)
        .then(res => {
          this.items = res.data;
        })
        .catch(err => {
          console.error('Ошибка загрузки комментариев:', err);
        });
    },

    formatTime(dateStr) {
      const d = new Date(dateStr);
      return d.toLocaleString('ru-RU');
    },

    isOwner(userId) {
      return userId === this.currentUserId;
    },

    handleEdit(item) {
      this.activeEditId = item.id;
      this.editText = item.content;
    },

    closeEdit() {
      this.activeEditId = null;
      this.editText = '';
    },

    saveChanges() {
      CommentService.updateComment(
        this.activeEditId,
        this.editText,
        this.taskId,
        this.currentUserId
      ).then(() => {
        this.getComments();
        this.closeEdit();
      }).catch(err => {
        console.error('Ошибка обновления комментария:', err);
      });
    },

    handleDelete(commentId) {
      if (confirm('Удалить комментарий?')) {
        CommentService.removeComment(commentId)
          .then(() => {
            this.getComments();
          })
          .catch(err => {
            console.error('Ошибка удаления комментария:', err);
          });
      }
    },

    appendComment(newCommentItem) {
      this.items.unshift(newCommentItem);
    }
  },

  mounted() {
    this.getComments();
  }
};
</script>

<style scoped>
.task-comments {
  margin-top: 25px;
  padding: 20px;
  background: #ffffff;
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.task-comments h3 {
  font-size: 17px;
  margin: 0 0 18px 0;
  padding-bottom: 10px;
  border-bottom: 1px solid #eef2f5;
}

.comments-empty {
  text-align: center;
  color: #a0aec0;
  padding: 30px 15px;
  font-size: 14px;
}

.comments-feed {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.comment {
  background: #fdfdfd;
  padding: 12px 15px;
  border-radius: 6px;
  border: 1px solid #edf2f7;
  border-left: 4px solid #38a169;
}

.comment-top {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
  font-size: 13px;
}

.comment-top strong {
  color: #2f855a;
  font-size: 13.5px;
}

.comment-time {
  color: #a0aec0;
  font-size: 11px;
}

.comment-nav {
  margin-left: auto;
  display: flex;
  gap: 8px;
}

.btn-edit,
.btn-delete {
  background: transparent;
  border: none;
  cursor: pointer;
  font-size: 13px;
  padding: 3px 6px;
  border-radius: 4px;
  transition: background-color 0.15s ease;
}

.btn-edit {
  color: #4a5568;
}

.btn-edit:hover {
  background: #edf2f7;
  color: #2b6cb0;
}

.btn-delete {
  color: #e53e3e;
}

.btn-delete:hover {
  background: #fff5f5;
  color: #c53030;
}

.comment-text {
  color: #2d3748;
  font-size: 14px;
  line-height: 1.5;
  word-wrap: break-word;
}

.textarea-field {
  width: 100%;
  padding: 10px;
  border: 1px solid #cbd5e0;
  border-radius: 6px;
  font-family: inherit;
  font-size: 13px;
  resize: vertical;
  box-sizing: border-box;
}

.textarea-field:focus {
  outline: none;
  border-color: #38a169;
  box-shadow: 0 0 0 2px rgba(56, 161, 105, 0.1);
}

.comment-editing-block {
  margin-top: 10px;
}

.editing-buttons {
  margin-top: 10px;
  display: flex;
  gap: 10px;
}

.btn-save,
.btn-cancel {
  padding: 5px 14px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  font-weight: 500;
}

.btn-save {
  background: #38a169;
  color: white;
}

.btn-save:hover {
  background: #2f855a;
}

.btn-cancel {
  background: #e2e8f0;
  color: #4a5568;
}

.btn-cancel:hover {
  background: #cbd5e0;
}
</style>