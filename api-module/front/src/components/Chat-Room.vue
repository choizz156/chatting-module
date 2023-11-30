<template>
  <h2>chat</h2>
  <div>
    <div class="chat-container" id="chat-page">
      <div class="users-list">
        <div class="users-list-container">
          <h2 @click="connectUsers">Online Users</h2>
          <ul id="connectedUsers">
            <connected-user
              :my-nickname="myNickname"
              :my-user-id="myUserId"
              v-for="(receiver, index) in receivers"
              :key="receiver.userId"
              :index="index"
              :receiver-id="receiver.userId"
              :receiver-nickname="receiver.nickname"
              :length="receivers.length"
              :make-room="true"
              :is-active="active(receiver.userId)"
              :is-nr-msg="nrMsg(receiver.userId)"
              @add-receiver="addMessageEvent"
              @user-clicked="applyActiveEvent"
              @read-msg="readMessageEvent"
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
        :receiver-id="chatReceiverId"
        :receiver-nickname="chatReceiverNickname"
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
      receivers: [],
      isActive: false,
      activeUserId: null,
      myUserId: this.$route.query.userId,
      myEmail: this.$route.query.email,
      myNickname: this.$route.query.nickname,
      selectedRoomId: "",
      chatArea: null,
      stompClient: null,
      chatReceiverId: "",
      chatReceiverNickname: "",
      sendingUserId: null,
    };
  },
  computed: {},
  methods: {
    nrMsg(receiverUserId) {
      return this.sendingUserId === receiverUserId;
    },
    active(receiverUserId) {
      return this.activeUserId === receiverUserId;
    },

    readMessageEvent() {
      this.sendingUserId = null;
    },

    applyActiveEvent(receiverId) {
      this.activeUserId = receiverId;
    },

    addMessageEvent(receiverId, receiverNickname, roomId) {
      this.chatReceiverId = receiverId;
      this.chatReceiverNickname = receiverNickname;
      this.selectedRoomId = roomId;
    },

    async connectUsers() {
      axios
        .get("http://localhost:8083/login-users")
        .then((resp) => {
          let connectedUsers = resp.data;
          this.receivers = connectedUsers.filter(
            (user) => user.email !== this.myEmail
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
        `/user/${this.myUserId}/queue/messages`,
        this.receiveMessage
      );
      this.stompClient.subscribe(`/user/public`, this.receiveMessage);
      this.stompClient.send(
        "/app/login-users",
        {},
        JSON.stringify({
          userId: this.myUserId,
          email: this.myEmail,
          nickname: this.myNickname,
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
      console.log(message.receiverId + " " + message.receiverNickname);
      console.log(this.myUserId + " " + this.myNickname);
      console.log(this.selectedRoomId);
      console.log(message.roomId);
      if (
        this.selectedRoomId == message.roomId &&
        message.receiverId == this.myUserId
      ) {
        console.log("sdfsdfsdfsdf");
        this.$store.commit("addMessage", message);
        return;
      }
      this.sendingUserId = message.senderId;
    },
  },

  mounted() {
    this.connectWebSocket();

    const userInfo = {
      email: this.myEmail,
      nickname: this.myNickname,
      userId: this.myUserId,
    };
    this.$store.commit("setUser", userInfo);
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
  background-color: burlywood;
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

a.logout {
  color: #fff;
  text-decoration: none;
}
</style>
