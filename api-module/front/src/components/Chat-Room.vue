<template>
  <h2>chat</h2>
  <div>
    <div class="chat-container" id="chat-page">
      <div class="users-list">
        <div class="users-list-container">
          <h2 @click="connectUsers">Online Users</h2>
          <ul id="connectedUsers">
            <connected-user
              :my-nickname="nickname"
              :my-user-id="userId"
              v-for="(user, index) in users"
              :key="user.userId"
              :index="index"
              :receiver-id="user.userId"
              :receiver-nickname="user.nickname"
              :length="users.length"
              :make-room="true"
              @add-receiver="addMessage"
            ></connected-user>
          </ul>
        </div>
        <div>
          <a class="logout" href="javascript:void(0)" id="logout">Logout</a>
        </div>
      </div>
      <chat-area
        :stomp-client="stompClient"
        :room-id="selectedRoomId"
        :receiver-id="receiverId"
        :receiver-nickname="receiverNickname"
      ></chat-area>
    </div>
  </div>
</template>

<script>
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import ConnectedUser from "@/components/chat/Connected-User.vue";
import ChatArea from "@/components/chat/Chat-Area.vue";
import axios from "axios";

export default {
  name: "Chat-Room",
  components: { ChatArea, ConnectedUser },
  data() {
    return {
      userId: this.$route.query.userId,
      email: this.$route.query.email,
      nickname: this.$route.query.nickname,
      selectedRoomId: "",
      chatArea: null,
      stompClient: null,
      users: [],
      receiverId: "",
      receiverNickname: "",
    };
  },
  computed: {},
  methods: {
    addMessage(receiverId, receiverNickname, roomId) {
      this.receiverId = receiverId;
      this.receiverNickname = receiverNickname;
      this.selectedRoomId = roomId;
    },
    async connectUsers() {
      axios
        .get("http://localhost:8083/login-users")
        .then((resp) => {
          let connectedUsers = resp.data;
          this.users = connectedUsers.filter(
            (user) => user.email !== this.email
          );
        })
        .catch(() => alert("로그인된 유저를 가지고 오지 못했습니다."));
    },

    connectWebSocket() {
      const socket = new SockJS("http://localhost:8083/chat");
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect({}, this.onConnected, this.onError);
    },

    onConnected() {
      this.stompClient.subscribe(
        `/user/${this.userId}/queue/messages`,
        this.receiveMessage
      );
      this.stompClient.subscribe(`/user/public`, this.receiveMessage);
      this.stompClient.send(
        "/app/login-users",
        {},
        JSON.stringify({
          userId: this.userId,
          email: this.email,
          nickname: this.nickname,
        })
      );
    },
    onError() {
      alert("소켓 연결 실패");
    },

    async receiveMessage(payload) {
      await this.connectUsers();
      const message = JSON.parse(payload.body);
      console.log(message);

      this.$store.commit("addMessage", message);

      // if (this.selectedRoomId) {
      //   document
      //     .querySelector(`#${this.selectedRoomId}`)
      //     .classList.add("active");
      // } else {
      //   this.messageForm.classList.add("hidden");
      // }

      // const notifiedUser = document.querySelector(
      //   `#${this.message.senderNickname}`
      // );
      // if (notifiedUser && !notifiedUser.classList.contains("active")) {
      //   const nbrMsg = notifiedUser.querySelector(".nbr-msg");
      //   nbrMsg.classList.remove("hidden");
      //   nbrMsg.textContent = "";
      // }
    },
  },

  mounted() {
    this.connectWebSocket();
    this.messageForm = this.$refs.messageForm;
    this.chatArea = this.$refs.chatArea;
  },
};
</script>

<style>
body {
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  margin: 0;
  padding: 0;
  background-color: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  flex-direction: column;
}

.chat-container {
  display: flex;
  max-width: 800px;
  min-width: 800px;
  min-height: 600px;
  max-height: 600px;
  margin: 20px;
  border: 1px solid #ccc;
  background-color: #fff;
  overflow: hidden;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.users-list {
  flex: 1;
  border-right: 1px solid #ccc;
  padding: 20px;
  box-sizing: border-box;
  background-color: #3498db;
  color: #fff;
  border-top-left-radius: 8px;
  border-bottom-left-radius: 8px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.users-list-container {
  height: 100%;
  overflow-y: scroll;
}

.users-list h2 {
  font-size: 1.5rem;
  margin-bottom: 10px;
}

.users-list ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.user-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  cursor: pointer;
}

.user-item.active {
  background-color: #cdebff;
  color: #4f4f4f;
  padding: 10px;
  border-radius: 5px;
}

.user-item img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}

.user-item span {
  font-weight: bold;
}

.separator {
  height: 1px;
  background-color: #ccc;
  margin: 10px 0;
}

.chat-area {
  flex: 3;
  display: flex;
  flex-direction: column;
  padding: 20px;
  box-sizing: border-box;
  border-top-right-radius: 8px;
  border-bottom-right-radius: 8px;
}

.message {
  margin-bottom: 5px;
  border-radius: 5px;
}

#chat-messages {
  display: flex;
  flex-direction: column;
  overflow-y: scroll;
}

.message p {
  padding: 0 12px;
  border-radius: 15px;
  word-wrap: break-word;
}

.user-item span.nbr-msg {
  margin-left: 10px;
  background-color: #f8fa6f;
  color: white;
  padding: 5px;
  width: 10px;
  border-radius: 50%;
  height: 10px;
}

.sender {
  background-color: #3498db;
  color: #fff;
  align-self: flex-end;
}

.receiver {
  background-color: #ecf0f1;
  color: #333;
  align-self: flex-start;
}

.message-input {
  margin-top: auto;
  display: flex;
}

.message-input input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-right: 10px;
}

.message-input button {
  padding: 10px;
  border: none;
  background-color: #3498db;
  color: #fff;
  border-radius: 5px;
  cursor: pointer;
}

.hidden {
  display: none;
}

a.logout {
  color: #fff;
  text-decoration: none;
}
</style>
