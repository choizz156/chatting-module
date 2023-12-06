<template>
  <div class="container join-form">
    <div class="form-group">
      <label class="form-label mt-4">Email</label>
      <div class="col-lg-12">
        <input
          v-model="email"
          type="email"
          class="form-control"
          id="joinEmail"
        />
      </div>
    </div>
    <div class="form-group">
      <label class="form-label mt-4">Password</label>
      <div class="col-lg-12">
        <input
          v-model="password"
          type="password"
          class="form-control"
          id="joinPassword"
          autocomplete="off"
        />
      </div>
    </div>
    <div class="form-group">
      <label class="form-label mt-4">Nickname</label>
      <div class="col-lg-12">
        <input
          v-model="nickname"
          type="text"
          class="form-control"
          id="joinNickname"
          autocomplete="off"
        />
      </div>
    </div>
    <div class="btn-group-vertical mt-3">
      <button @click="joinUs" type="button" class="btn btn-primary">
        Sign Up
      </button>
    </div>
  </div>
</template>

<script>
import axios from "axios";
axios.defaults.withCredentials = true;

export default {
  name: "User-Join",
  data() {
    return {
      email: "",
      password: "",
      nickname: "",
    };
  },
  methods: {
    async joinUs() {
      const host = "http://localhost:8080";
      axios
        .post(host + "/users", {
          email: this.email,
          password: this.password,
          nickname: this.nickname,
        })
        .then((response) => {
          if (response.status === 201) {
            window.location.href = "/login";
          }
        })
        .catch((error) => {
          if (
            error.response &&
            error.response.status === 400 &&
            error.response.data.data.customFieldErrors
          ) {
            this.to400Error(error);
            return;
          }
          this.toServerError(error);
        });
      this.password = "";
    },
    to400Error(error) {
      const errorField = error.response.data.data.customFieldErrors[0].field;
      alert("회원가입에 실패하였습니다." + errorField + "를 확인해주세요");
    },
    toServerError(error) {
      if (error.response) {
        const errorField2 = error.response.data.data.msg;
        alert(errorField2);
      } else {
        alert("알 수 없는 오류가 발생했습니다.");
      }
    },
  },
};
</script>

<style>
.join-form {
  height: 500px;
}
</style>
