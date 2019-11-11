const socketio = require("socket.io");
const {onJoin} = require("./socketFunctions");
const {logger} = require('../logger');

class SocketServer {
    
    constructor(server) {
        this.io = socketio(server);
    }

    startServer() {
        // this.io.on("connection", (socket) => {

        //     socket.on("join", onJoin
        //         .then((userId) => {
        //             socket.broadcast.emit("userjoinedthechat", userId + " has joined the chat");
        //     })
        //     .catch((err) => {
        //         console.error(err);
        //     }));

        //     socket.on("messagedetection", (nickname, messageContent, ) => {

        //         logger.info(nickname + " sent " + messageContent);

        //         let message = {"message" : messageContent, "senderNickname" : nickname};

        //         logger.info(message);
        //         this.io.emit("message", message);
        //     });
        // });
    }
}

module.exports = SocketServer;