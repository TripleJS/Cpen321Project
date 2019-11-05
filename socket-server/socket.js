const socketio = require("socket.io");
const {onJoin} = require("./socketFunctions");

class SocketServer {
    
    constructor(server) {
        this.io = socketio(server);
    }

    startServer() {
        this.io.on("connection", (socket) => {

            socket.on("join", onJoin
                .then((userId) => {
                    socket.broadcast.emit("userjoinedthechat", userId + " has joined the chat");
            })
            .catch((err) => {
                console.error(err);
            }));

            socket.on("messagedetection", (nickname, messageContent, ) => {

                console.log(nickname + " sent " + messageContent);

                let message = {"message" : messageContent, "senderNickname" : nickname};

                console.log(message);
                this.io.emit("message", message);
            });
        });
    }
}

module.exports = SocketServer;