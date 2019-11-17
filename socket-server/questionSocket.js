const io = require('socket.io');
const {onJoin} = require('./socketListenerFunctions');
const Question = require('../rest-server/schema/questions');
const User = require('../rest-server/schema/user');
const {logger} = require("../logger");
/**
 * 
 * @param {SocketIO} io 
 * @param {*} socket 
 */
const questionHandler = (io, socket, redisClient) => {

    socket.on("joinQuestion", async (data) => {

        const {questionId, userId} = data;
        socket.join("question_" + questionId + "_" + userId);

        try {
            await onJoin(userId, questionId);
            socket.broadcast.emit('userjoinedthechat', userId + " has joined the chat");
        } catch (error) {
            logger.error(error);
        }

        
        io.to("question_" + questionId + "_" + userId).emit("create", );
    });

    socket.on("messagedetection", (data) => {

        const {questionId, userId, currentSequence} = data; 

        const key = `${questionId}-${userId}`;

        // Save the sent message to the redis cache
        redisClient.setex(key, 20000, currentSequence);

        io.to(`question_${questionId}_${userId}`).emit("message", currentSequence);
    });

    socket.on("", (data) => {

    });

};

module.exports = questionHandler; 