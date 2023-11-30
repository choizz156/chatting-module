import { createStore } from "vuex";

const store = createStore({
  state: {
    showEl: {
      isShow: true,
    },

    messageEl: {
      message: "",
    },

    messageList: {
      messages: null,
    },
    senderEl: {
      nickname: "",
      userId: null,
      email: "",
    },
    roomEl: {
      roomId: null,
    },
  },
  mutations: {
    show(state, payload) {
      state.showEl.isShow = payload;
    },
    addMessage(state, payload) {
      state.messageEl.message = payload;
    },
    setUser(state, payload) {
      state.senderEl.nickname = payload.nickname;
      state.senderEl.email = payload.email;
      state.senderEl.userId = payload.userId;
    },
    loadMessageList(state, payload) {
      state.messageList.messages = payload;
    },
    setRoomId(state, payload) {
      state.roomEl.roomId = payload;
    },
  },
});

export default store;
