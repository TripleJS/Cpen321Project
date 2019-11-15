const socketio = require("socket.io");
const {logger} = require("../logger");
const questionHandler = require('./questionSocket');

class SocketServer {
    
    constructor(server, redisClient) {
        this.io = socketio(server);
        this.redisClient = redisClient;
    }

    startServer() {
        this.io.on("connection", (socket) => {

            questionHandler(io, socket, this.redisClient);

            // socket.on("join", async (userId, questionId) => {
            //     try {
            //         await onJoin(userId, questionId);
            //         socket.broadcast.emit('userjoinedthechat', userId + " has joined the chat");
            //     } catch (error) {
            //         logger.error(error);
            //     }
            // });
        
            // socket.on("messagedetection", (nickname, messageContent, ) => {
            //     let message = {"message" : messageContent, "senderNickname" : nickname};
            //     this.io.emit("message", message);
            // });

            

        });
    }
}

module.exports = SocketServer;