const socketio = require("socket.io");
const {onJoin} = require("./socketListenerFunctions");
const {logger} = require("../logger");

class SocketServer {
    
    constructor(server) {
        this.io = socketio(server);
    }

    startServer() {
        this.io.on("connection", (socket) => {

            socket.on("join", async (userId, questionId) => {
                try {
                    await onJoin(userId, questionId);
                    socket.broadcast.emit('userjoinedthechat', userId + " has joined the chat");
                } catch (error) {
                    logger.error(error);
                }
            });
        

            socket.on("messagedetection", (nickname, messageContent, ) => {

                logger.info(nickname + " sent " + messageContent);

                let message = {"message" : messageContent, "senderNickname" : nickname};

                logger.info(message);
                this.io.emit("message", message);
            });
        });
    }
}

module.exports = SocketServer;