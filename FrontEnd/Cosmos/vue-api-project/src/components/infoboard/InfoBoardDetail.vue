<template>
  <div class="info-board-detail">
    <div>
      <div class="board-detail-item">
        <h2>{{ infoBoard.ib_title }}</h2><br>
      </div>
      <span style="margin-right: 30px;">조회수: {{ infoBoard.ib_viewCnt }}</span>
      <span>작성일: {{ infoBoard.ib_regDate }}</span>
    </div>
    <br>
    <div>
      <div class="board-detail-item">{{ infoBoard.ib_content }}</div>
    </div>

    <div>
      <button @click="update(infoBoard.ib_num)" v-if="userStore.loginUserId==admin">수정</button>
      <button @click="remove" v-if="userStore.loginUserId==admin">삭제</button>
    </div>
  </div>
  <div class="info-board-comment">
    <InfoBoardCommentVue />
  </div>
</template>

<script setup>
import InfoBoardCommentVue from './InfoBoardComment.vue';

import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router'

import { useInfoBoardStore } from '../../stores/infoBoard';

const route = useRoute();
const router = useRouter();

const store = useInfoBoardStore();

onMounted(() => {
  store.getInfoBoard(route.params.num)
})

const infoBoard = computed(() => store.infoBoard)

</script>

<style scoped>
.info-board-detail,
.info-board-comment {
  max-width: 600px;
  margin: 0 auto;
  margin-bottom: 10px;
  padding: 20px;
  background-color: rgba(255, 255, 255, 0.7);
  border: 1px solid #ddd;
  border-radius: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.board-detail-item {
  margin-bottom: 10px;
}

button {
  background-color: #68a67d;
  color: white;
  padding: 10px 15px;
  margin-right: 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}

button:hover {
  background-color: #45a049;
}
</style>