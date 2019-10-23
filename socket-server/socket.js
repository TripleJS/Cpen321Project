const socketio = require('socket.io');
 

const io = socketio()

class SocketServer {
    
    constructor(server) {
        this.io = socketio(server);
    }

    startServer() {
        this.io.on('connection', (socket) => {

            socket.on('join', (userNickname) => {
                console.log(userNickname + ": joined chat");

                socket.broadcast.emit('userjoinedthechat', userNickname + " has joined the chat");
            });

            socket.on('messagedetection', (senderNickname, messageContent) => {

                console.log(senderNickname);

                let message = {'message' : messageContent, 'senderNickname' : senderNickname};

                this.io.emit('message', message);
            });

        });



    }
}

module.exports = SocketServer;