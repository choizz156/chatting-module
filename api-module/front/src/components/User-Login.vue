<template>
  <div class="container login-form">
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
      <div class="col-lg-12">
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

export default {
  name: "User-Login",
  data() {
    return {
      email: "",
      password: "",
      nickname: "",
      userId: 0,
    };
  },
  methods: {
    async processLogin() {
      const host = "http://localhost:8080";
      axios
        .post(host + "/login", {
          email: this.email,
          password: this.password,
        })
        .then((response) => {
          if (response.status === 200) {
            this.nickname = response.data.data.nickname;
            this.userId = response.data.data.userId;
            const userInfo = {
              email: this.email,
              nickname: this.nickname,
              userId: this.userId,
            };
            this.$store.commit("setUser", userInfo);

            this.$router.push({
              path: "/",
              query: {
                email: this.email,
                nickname: this.nickname,
                userId: this.userId,
              },
            });
          }
        })
        .catch((reason) => {
          alert("아이디 혹은 비밀번호를 확인해주세요1" + reason.status);
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
