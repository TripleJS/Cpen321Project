const socketio = require('socket.io');
 
const startSocketServer = (server)

const io = socketio()

class SocketServer {
    
    constructor(server) {

        this.io = socketio(server);
    }

    startServert() {
        this.io.on('connection', (socket) => {

            socket.emit('')

        })

    }


}