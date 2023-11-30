<template>
  <li
    ref="clickedUser"
    @click.prevent="createRoomIfNotExist"
    class="user-item"
    :class="isSelected"
  >
    <span>{{ receiverNickname }}</span>
    <!--    <message></message>-->
    <template v-if="separator(index)">
      <li class="separator"></li>
    </template>
  </li>
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
  },

  data() {
    return {
      selected: true,
      selectedRoomId: "",
      isRoomMade: false,
      roomId: -1,
    };
  },
  computed: {
    isSelected() {
      return { active: this.selected };
    },
  },
  methods: {
    separator(index) {
      return index !== this.length - 1;
    },
    createRoomIfNotExist() {
      console.log(this.isRoomMade);
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
      console.log(this.myNickname);
      console.log(this.myUserId);
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
      this.selected = false;
      this.$store.commit("show", false);

      const clickedUser = this.$refs.clickedUser;
      clickedUser.classList.add("active");

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

/*
.user-item img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}
*/

.user-item span {
  font-weight: bold;
}

.separator {
  height: 1px;
  background-color: #ccc;
  margin: 10px 0;
}

.hidden {
  display: none;
}
</style>
