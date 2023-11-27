const app = Vue.createApp({
  data() {
    return {
      email: "",
      password: "",
      nickname: "",
    };
  },
  methods: {
    joinUs() {
      const userInfo = JSON.stringify({
        email: this.email,
        password: this.password,
        nickname: this.nickname,
      });
      fetchJoin(userInfo).then();
      this.password = "";
    },
  },
});

async function fetchJoin(userInfo) {
  const host = "http://localhost:8080";
  const response = await fetch(host + "/users", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: userInfo,
  });
  let promise = await response.json();
  if (response.status === 201) {
    window.location.href = "../loginForm.html";
  } else {
    const errorField = promise.data.customFieldErrors[0].field;
    alert("회원가입에 실패하였습니다." + errorField + "를 확인해주세요");
  }
}

app.mount("#signUp");
