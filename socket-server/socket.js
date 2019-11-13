const socketio = require("socket.io");
const {onJoin} = require("./socketListenerFunctions");
const {logger} = require("../logger");
const questionHandler = require('./questionSocket');
class SocketServer {
    
    constructor(server) {
        this.io = socketio(server);
    }

    startServer() {
        this.io.on("connection", (socket) => {

            questionHandler(io, socket);
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