<template>
  <div class="chat-area">
    <div ref="chatArea" class="chat-area" id="chat-messages">
      <div
        v-for="message in chatMessages"
        :key="message.id"
        :class="{
          message: true,
          sender: message.senderId !== receiverId,
          receiver: message.senderId === receiverId,
        }"
      >
        <p>
          {{ message.content }}
        </p>
      </div>
    </div>
    <form
      :class="{ hidden: this.$store.state.showEl.isShow }"
      @submit.prevent="sendMessage"
      ref="messageForm"
      id="messageForm"
      name="messageForm"
    >
      <div class="message-input">
        <input
          v-model="messageInput"
          autocomplete="off"
          type="text"
          id="message"
          placeholder="Type your message..."
        />
        <button>Send</button>
      </div>
    </form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  props: ["stompClient", "receiverId", "receiverNickname", "roomId"],
  data() {
    return {
      messageForm: null,
      messageInput: "",
      chatArea: null,
      chatMessages: [],
      senderId: null,
      senderNickname: "",
      failMessage: "전송 실패",
    };
  },
  computed: {
    newMessage() {
      return this.$store.state.messageEl.message;
    },
    getMessageList() {
      return this.$store.state.messageList.messages;
    },
  },
  watch: {
    newMessage(message) {
      this.addMessage(message);
    },
    getMessageList(messages) {
      this.chatMessages = messages.filter((m) => m !== null);
    },
    chatMessages() {
      this.$nextTick(() => {
        this.scrollToBottom();
      });
    },
  },
  methods: {
    sendMessage() {
      const messageContent = this.messageInput;
      if (messageContent && this.stompClient) {
        let chatMessage = {
          roomId: this.roomId,
          senderId: this.$store.state.senderEl.userId,
          receiverId: this.receiverId,
          senderNickname: this.$store.state.senderEl.nickname,
          receiverNickname: this.receiverNickname,
          content: messageContent,
        };

        const url = "http://localhost:8083";
        axios
          .post(url + "/messages", chatMessage, { timeout: 5000 })
          .then(() => {
            this.stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
            this.addMessage(chatMessage);
            this.messageInput = "";
            this.scrollToBottom();
          })
          .catch(() => {
            chatMessage.content = this.failMessage;
            alert(this.failMessage);
          });
      }
    },

    addMessage(payload) {
      this.chatMessages.push(payload);
    },

    scrollToBottom() {
      this.$refs.chatArea.scrollTop = this.$refs.chatArea.scrollHeight;
    },
  },
  updated() {
    this.scrollToBottom();
  },
  mounted() {
    this.messageForm = this.$refs.messageForm;
    this.chatArea = this.$refs.chatArea;
  },
};
</script>

<style scoped>
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

.sender {
  background-color: lavender;
  color: black;
  align-self: flex-end;
}

.receiver {
  background-color: lightsteelblue;
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
</style>
