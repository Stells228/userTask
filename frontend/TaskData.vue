<template>
  <div v-if="task" class="container-md mt-3">
    <div class="row">
      <div class="col-sm-5">
        <form @submit.prevent="handleSubmit" class="form">
          <h4>Задача</h4>
          <div class="form-group mb-3">
            <input type="text" v-model="task.title" placeholder="Заголовок задачи" class="form-control" />
          </div>
          <div class="form-group mb-3">
            <input type="text" v-model="task.description" placeholder="Описание задачи" class="form-control" />
          </div>
          <div class="form-group mb-3">
            <img v-if="task.mime_type && task.mime_type.includes('image')" :src="fileUrl" style="width: 200px;" />
            <a v-else-if="fileUrl" :href="fileUrl" target="_blank">Скачать файл</a>
          </div>
          <div class="form-group mb-3">
            <select v-model="categoryId" class="form-control">
              <option value="">Выберите категорию</option>
              <option v-for="category in categories" :key="category.id" :value="category.id">{{ category.name }}
              </option>
            </select>
          </div>
          <div class="row g-2 mt-1">
            <div class="col-auto">
              <input type="submit" value="Обновить" class="btn btn-success" />
            </div>
            <div class="col-auto">
              <button @click="deleteTask" class="btn btn-danger">Удалить</button>
            </div>
          </div>
        </form>
      </div>
      <div class="col-sm-6 offset-sm-1">
        <div class="comments-wrapper">
          <h4>Комментарии</h4>
          <CommentForm :taskId="id" :userId="currentUserId" @comment-added="refreshComments" />
          <CommentList :taskId="id" :currentUserId="currentUserId" ref="commentList" />
        </div>
      </div>
    </div>
  </div>
  <router-link v-else to="/myTasks" />
</template>

<script>
import http from "../../http-common";
import CommentList from "@/components/CommentList.vue";
import CommentForm from "@/components/CommentForm.vue";

export default {
  name: "task-details",
  components: {
    CommentList,
    CommentForm
  },
  props: ['id'],
  data() {
    return {
      task: null,
      categoryId: "",
      fileUrl: "",
      categories: [],
      submitted: false
    };
  },
  computed: {
    currentUserId() {
      return this.$store.state.auth.user ? this.$store.state.auth.user.id : null;
    }
  },
  methods: {
    getTask() {
      http
        .get(`/taskFile/${this.id}`)
        .then(response => {
          this.task = response.data.task;
          this.fileUrl = response.data.file;
          this.categoryId = response.data.category_id;
        })
        .catch(e => {
          console.log(e);
        });
    },
    getCategories() {
      http
        .get("/categories")
        .then(response => {
          this.categories = response.data;
        })
        .catch(e => {
          console.log(e);
        });
    },
    handleSubmit() {
      const data = {
        title: this.task.title,
        description: this.task.description,
        user_id: this.$store.state.auth.user.id,
        category_id: this.categoryId
      };
      http
        .post(`/updateTask/${this.id}`, data)
        .then(() => {
          this.$router.push('/myTasks');
          this.submitted = true;
        })
        .catch(e => {
          console.log(e);
        });
    },
    deleteTask() {
      http
        .post(`/deleteTask/${this.id}`)
        .then(() => {
          this.$router.push('/myTasks');
          this.submitted = true;
        })
        .catch(e => {
          console.log(e);
        });
    },
    refreshComments() {
      if (this.$refs.commentList) {
        this.$refs.commentList.getComments();
      }
    }
  },
  mounted() {
    this.getTask();
    this.getCategories();
  },
};
</script>

<style scoped>
.comments-wrapper {
  margin-top: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  max-height: 70vh;
  overflow-y: auto;
}

.form {
  margin: 40px;
}

h4 {
  margin-bottom: 20px;
  color: #333;
  border-bottom: 2px solid #42b983;
  padding-bottom: 10px;
}
</style>