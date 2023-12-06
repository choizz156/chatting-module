<template>
  <h2>"채팅서버"</h2>
  <div class="container">
    <div>
      <router-link to="/join">회원가입</router-link>
    </div>
    <div>
      <router-link to="/login">로그인</router-link>
    </div>
    <div>
      <router-link
        :to="{
          path: '/chat-room',
          query: { userId: userId, nickname: nickname, email: email },
        }"
        >채팅룸</router-link
      >
    </div>
  </div>
</template>

<script>
export default {
  name: "Home-Page",
  data() {
    return {
      userId: null,
      email: "",
      nickname: "",
    };
  },
  mounted() {
    this.userId = this.$route.query.userId;
    this.email = this.$route.query.email;
    this.nickname = this.$route.query.nickname;
  },
};
</script>

<style></style>
