import {createWebHistory, createRouter} from "vue-router";
import UserJoin from "@/components/User-Join.vue";
import UserLogin from "@/components/User-Login.vue";
import HomePage from "@/components/Home-Page.vue";
import ChatRoom from "@/components/Chat-Room.vue";

const routes = [
    {
      path: '/',
      component: HomePage
    },
    {
        path: '/join',
        component: UserJoin,
    },
    {
        path: '/login',
        component: UserLogin,
    },
    {
        path:'/chat-room',
        component: ChatRoom,
    }
]
const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router;