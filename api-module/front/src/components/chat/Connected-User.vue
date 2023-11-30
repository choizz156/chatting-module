<template>
  <li
    ref="clickedUser"
    @click.prevent="createRoomIfNotExist"
    :class="isSelected"
    class="user-item"
  >
    <span>{{ receiverNickname }}</span>
    <span :class="addNrMsg" class="user-item span"></span>
  </li>
  <hr class="my-1" />
</template>

<script>
import axios from "axios";

export default {
  props: {
    receiverId: {
      type: Number,
    },
    receiverNickname: {
      type: String,
    },
    index: {
      type: Number,
    },
    makeRoom: {
      type: Boolean,
    },
    myUserId: {
      type: Number,
    },
    myNickname: {
      type: String,
    },
    length: {
      type: Number,
    },
    isActive: {
      type: Boolean,
    },
    isNrMsg: {
      type: Boolean,
    },
  },

  data() {
    return {
      isRoomMade: false,
      roomId: -1,
    };
  },

  computed: {
    isSelected() {
      return { active: this.isActive };
    },
    addNrMsg() {
      return { "nr-msg": this.isNrMsg };
    },
  },

  methods: {
    createRoomIfNotExist() {
      if (this.isRoomMade === true) {
        this.userItemClick();
      } else {
        this.createChatRoom();
      }
    },

    createChatRoom() {
      if (this.isRoomMade) {
        return;
      }

      axios
        .post("http://localhost:8080/chatting-rooms", {
          name: this.myNickname + "_" + this.receiverNickname,
          hostId: this.myUserId,
          clientId: this.receiverId,
        })
        .then((resp) => {
          this.isRoomMade = this.makeRoom;
          this.roomId = resp.data.data.roomId;
          this.userItemClick();
        })
        .catch((error) => {
          if (error.response && error.response.status === 400) {
            this.userItemClick();
          } else {
            alert("알 수 없는 오류가 발생했습니다.");
          }
        });
    },

    userItemClick() {
      this.$emit("user-clicked", this.receiverId);
      this.$emit("read-msg", null);

      this.$store.commit("show", false);

      this.fetchAndDisplayUserChat().then();
    },

    async fetchAndDisplayUserChat() {
      axios
        .get(`http://localhost:8083/messages/${this.roomId}`)
        .then((resp) => {
          this.$emit(
            "add-receiver",
            this.receiverId,
            this.receiverNickname,
            this.roomId
          );
          this.$store.commit("loadMessageList", resp.data);
        });
    },
  },
};
</script>

<style scoped>
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

.user-item span.nr-msg {
  margin-left: 10px;
  background-color: royalblue;
  color: white;
  padding: 5px;
  width: 10px;
  border-radius: 50%;
  height: 10px;
}

.user-item span {
  font-weight: bold;
}
</style>
