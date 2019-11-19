const io = require('socket.io');
const {onJoin} = require('./socketListenerFunctions');
const Question = require('../rest-server/schema/questions');
const Answer = require("../rest-server/schema/answers");
const User = require('../rest-server/schema/user');
const {logger} = require("../logger");
const answerHandler = (io, socket, redisClient) => {

    socket.on("joinViewAnswer", async (data) => {
        const {userAnsweringId, questionId} = data; 

        const roomId = `room_${questionId}_${userAnsweringId}`;

        const answerKey = `${questionId}_${userAnsweringId}`
        socket.join(roomId);

        try {
            let curAnswer = await Answer.findOne({key : answerKey});

            if (curAnswer === null) {
                throw new Error("Couldn't Find Answer");
            }

            const curSequence = curAnswer.answer;

            io.to(roomId).emit("message", curSequence);
        
        } catch (error) {
            io.to(roomId).emit("message", "error hehexd");
        }
    });

    socket.on("submitAnswer", async (data) => {
        const {userID, messagetxt, questionId} = data;
        
        try {
            const curAnswer = new Answer({
                answer : messagetxt,
                questionRef : questionId,
                userAnswerId : userID
            });

            await curAnswer.save();
        } catch (error) {
            logger.error(error);
        }
    });

    socket.on("saveAnswer", (data) => {
        const {userId, questionId, messagetxt} = data;
        const key = `${questionId}-${userId}`;

        redisClient.setex(key, 36000000, messagetxt);
    });

    socket.on("joinNavAnswers", async (data) => {
        const {questionId} = data; 

        const room = "room_" + questionId;
        socket.join(room);

        try {
            const curQuestion = await Question.findById(questionId);
            const answerers = curQuestion.answerers;
            console.log(answerers);
            io.to(room).emit("getUserAnswering", {userAnswering : answerers});
        } catch (error) {
            logger.error(error);
        }
    });


};

module.exports = answerHandler; 