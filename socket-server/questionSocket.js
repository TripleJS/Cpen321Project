const io = require('socket.io');
const {onJoin} = require('./socketListenerFunctions');
/**
 * 
 * @param {SocketIO} io 
 * @param {*} socket 
 */
const questionHandler = (io, socket) => {

    socket.on("join_question", async (userId, questionId) => {
        socket.join("question_" + questionId);

        try {
            await onJoin(userId, questionId);
            socket.broadcast.emit('userjoinedthechat', userId + " has joined the chat");
        } catch (error) {
            logger.error(error);
        }
    });

    socket.on("message", (questionId, message) => {
        io.broadcast.to("question_"  + questionId).emit(() => {
            
        })
    })
};

module.exports = questionHandler; 