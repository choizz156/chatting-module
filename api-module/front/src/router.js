import { createWebHistory, createRouter } from "vue-router";
import UserJoin from "@/components/User-Join.vue";
import UserLogin from "@/components/User-Login.vue";
import HomePage from "@/components/Home-Page.vue";
import ChatRoom from "@/components/Chat-Room.vue";
import axios from "axios";

const routes = [
  {
    path: "/",
    component: HomePage,
  },
  {
    path: "/join",
    component: UserJoin,
  },
  {
    path: "/login",
    name: "UserLogin",
    component: UserLogin,
  },
  {
    path: "/chat-room",
    component: ChatRoom,
    meta: {
      needAuth: true,
    },
  },
];
const router = createRouter({
  history: createWebHistory(),
  routes,
});
const url = "http://localhost:8080";
router.beforeEach((to, from, next) => {
  if (to.matched.some((record) => record.meta.needAuth)) {
    axios
      .get(url + "/users/auth")
      .then((response) => {
        if (response.data) {
          next();
        }
      })
      .catch(() => {
        next({ name: "UserLogin" });
        alert("로그인이 필요합니다.");
      });
  } else {
    next();
  }
});

export default router;
