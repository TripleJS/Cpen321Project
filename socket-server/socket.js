const socketio = require("socket.io");
const {logger} = require("../logger");
const questionHandler = require('./questionSocket');
const answerHandler = require("./answerSocket");
class SocketServer {
    
    constructor(server, redisClient) {
        this.io = socketio(server);
        this.redisClient = redisClient;
    }

    startServer() {
        this.io.on("connection", (socket) => {
            questionHandler(this.io, socket, this.redisClient);
            answerHandler(this.io, socket, this.redisClient);
        });
    }
}

module.exports = SocketServer;