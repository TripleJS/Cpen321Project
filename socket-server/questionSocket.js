const io = require('socket.io');
const {onJoin} = require('./socketListenerFunctions');
const Question = require('../rest-server/schema/questions');
const User = require('../rest-server/schema/user');
/**
 * 
 * @param {SocketIO} io 
 * @param {*} socket 
 */
const questionHandler = (io, socket, redisClient) => {

    socket.on("join_question", async (userId, questionId) => {
        socket.join("question_" + questionId + "_" + userId);

        try {
            await onJoin(userId, questionId);
            // socket.broadcast.emit('userjoinedthechat', userId + " has joined the chat");
        } catch (error) {
            logger.error(error);
        }

    
        
        io.to("question_" + questionId).emit("create", )
    });

    socket.on("message", (questionId, answerId, message) => {

        const key = `${questionId}-${answerId}`;

        // Save the sent message to the redis cache
        redisClient.setex(key, 20000, message);

        io.broadcast.to(`question_${questionId}`).emit('send-message', message);
    });

    
};

module.exports = questionHandler; 