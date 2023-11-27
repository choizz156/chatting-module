const app = Vue.createApp({
    data() {
        return {
            email: "",
            password: "",
            nickname: "",
            userId: 0,
            stompClient: null
        };
    },
    methods: {
        processLogin() {
            const emailPassword = JSON.stringify({
                email: this.email,
                password: this.password,
            });
            this.fetchLogin(emailPassword).then();
            this.password = "";
        },
        async fetchLogin(emailPassword) {
            const host = "http://localhost:8080";
            const response = await fetch(host + "/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: emailPassword,
            });
            const user = await response.json();

            if (response.status === 200) {
                this.nickname = user.data.nickname;
                console.log(user.data.nickname)
                this.userId = user.data.userId;
                console.log(user.data.userId)
                await this.addLogin();
                // window.location.href = "../";
            } else {
                alert("아이디 혹은 비밀번호를 확인해주세요1");
            }
        },
        onError() {
            console.error("Failed to connect to the server: ", error);
        },

        addLogin() {
            const socket = new SockJS("http://localhost:8083/chat");
            this.stompClient = Stomp.over(socket);
            this.stompClient.connect({}, this.onConnected, this.onError);
        },

        onConnected() {
            console.log("Connected to the server.");
            this.stompClient.subscribe(`/user/public`, receivedMessage);
            this.stompClient.send(
                "/app/login-user",
                {},
                JSON.stringify({
                    userId: this.userId,
                    email: this.email,
                    nickname: this.nickname,
                })
            );
            console.log("finish");
        }
    },

});


// async function findAndDisplayConnectedUsers() {
//     const connectedUsersResponse = await fetch('/login-users');
//     let connectedUsers = await connectedUsersResponse.json();
//     console.log(connectedUsers);
// }

function receivedMessage() {
    // await findAndDisplayConnectedUsers();
    // console.log('Message received', payload);
}


// function addLoginUser() {
//     const socket = new SockJS("http://localhost:8083/chat");
//     stompClient = Stomp.over(socket);
//     stompClient.connect({}, onConnected, onError);
// }


app.mount("#login");
