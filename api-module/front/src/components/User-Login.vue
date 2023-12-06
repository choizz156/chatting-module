<template>
  <div class="container login-form">
    <h2>Login</h2>
    <div class="form-group">
      <label for="emailInput" class="form-label mt-4">Email address</label>
      <div class="col-lg-12">
        <input
          v-model="email"
          type="email"
          class="form-control"
          placeholder="Email"
          name="username"
          id="emailInput"
        />
      </div>
    </div>
    <div class="form-group">
      <label for="passwordInput" class="form-label mt-4">Password</label>
      <div @keyup.enter.prevent="processLogin" class="col-lg-12">
        <input
          v-model="password"
          type="password"
          name="password"
          class="form-control"
          id="passwordInput"
          placeholder="Password"
          autocomplete="off"
        />
      </div>
    </div>
    <div class="btn-group-vertical mt-3">
      <button
        @keyup.enter.prevent="processLogin"
        @click.prevent="processLogin"
        type="submit"
        class="btn btn-primary"
      >
        login
      </button>
    </div>
  </div>
</template>

<script>
import axios from "axios";
axios.defaults.withCredentials = true;

export default {
  name: "User-Login",
  data() {
    return {
      email: "",
      password: "",
      nickname: "",
      userId: null,
    };
  },
  methods: {
    routing() {
      this.$router.push({
        path: "/",
        query: {
          email: this.email,
          nickname: this.nickname,
          userId: this.userId,
        },
      });
    },
    async processLogin() {
      const host = "http://localhost:8080";
      axios
        .post(host + "/auth/login", {
          email: this.email,
          password: this.password,
        })
        .then((response) => {
          if (response.status === 200) {
            this.nickname = response.data.data.nickname;
            this.userId = response.data.data.userId;

            this.routing();
          }
        })
        .catch((reason) => {
          console.log(reason);
          if (reason.response.status === 409) {
            alert("이미 로그인돼 있습니다.");
            return;
          }
          alert("아이디 혹은 비밀번호를 확인해주세요");
        });
    },
  },
};
</script>

<style>
.login-form {
  height: 500px;
}
</style>
