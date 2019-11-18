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
        const answerKey = `${questionId}-${userId}`;

        try {
            const curQuestion = await Question.findByIdAndUpdate(questionId, {$push : {answerers : userId}});
            
            let curAnswer = await Answer.find({key : key});

            if (curAnswer === null) {
                curAnswer = new Answer({
                    answer : "",
                    questionRef : questionId,
                    userAnswerID : userId,
                    date : Date.now(),
                    key : answerKey
                });

                await curAnswer.save();
            }

            await onJoin(userId, questionId);
            await curQuestion.save();

        } catch (error) {
            logger.error(error);
        }

        redisClient.getAsync(answerKey).then((result) => {

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

        const answerKey = `${questionId}-${userId}`;
        logger.info(answerKey);
        logger.info(currentSequence);

        // Save the sent message to the redis cache
        redisClient.setex(answerKey, 36000000, currentSequence);

        io.to(`room_${questionId}_${userId}`).emit("message", currentSequence);

        try {
            const curAnswer = await Answer.findOne({key : answerKey});
            if (curAnswer === null) {
                // TODO: fill this later 
            }

           await curAnswer.save();
        } catch (error) {
            logger.error("error in message detection");
            logger.error(error);
        }
    });

    socket.on("", (data) => {

    });

};

module.exports = questionHandler; 