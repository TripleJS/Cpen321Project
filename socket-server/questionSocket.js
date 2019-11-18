const io = require('socket.io');
const {onJoin} = require('./socketListenerFunctions');
const Question = require('../rest-server/schema/questions');
const Answer = require("../rest-server/schema/answers");
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
        const roomId = "room_" + questionId + "_" + userId
        socket.join(roomId);

        try {
            const curQuestion = await Question.findByIdAndUpdate(questionId, {$push : {answerers : userId}});
            
            const key = `${questionId}-${userId}`;

            let curAnswer = await Answer.find({key : key});
            if (curAnswer === null) {
                curAnswer = new Answer({

                });
            }
            await onJoin(userId, questionId);
            await curQuestion.save();

        } catch (error) {
            logger.error(error);
        }
        const key = `${questionId}-${userId}`;

        redisClient.getAsync(key).then((result) => {

            if (result === null) {
                logger.info("value was not cached");
                io.to(roomId).emit("create", {answer : ""});
            } else {
                logger.info("cached");
                io.to(roomId).emit("create", {answer : result});
            }
            
        }).catch((err) => {
            logger.error(err);
            io.to(roomId).emit("create", {answer : ""});
        });

    });

    socket.on("messagedetection", async (data) => {

        const {questionId, userId, currentSequence} = data; 

        const key = `${questionId}-${userId}`;

        // Save the sent message to the redis cache
        redisClient.setex(key, 36000000, currentSequence);

        io.to(`room_${questionId}_${userId}`).emit("message", currentSequence);

        try {

        } catch (error) {

        }
    });

    socket.on("", (data) => {

    });

};

module.exports = questionHandler; 