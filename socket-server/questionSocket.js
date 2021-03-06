const io = require("socket.io");
const {onJoin} = require("./socketListenerFunctions");
const Question = require("../rest-server/schema/questions");
const Answer = require("../rest-server/schema/answers");
const User = require("../rest-server/schema/user");
const {logger} = require("../logger");
/**
 * 
 * @param {SocketIO} io 
 * @param {*} socket 
 */
const questionHandler = (io, socket, redisClient) => {

    socket.on("joinQuestion", async (data) => {

        const {questionId, userId} = data;
        const roomId = "room_" + questionId + "_" + userId;
        console.log("question id: " + questionId);
        console.log("userid: " + userId);
        socket.join(roomId);
        const answerKey = `${questionId}-${userId}`;
        console.log("answer key is: " + answerKey);

        try {
            const condition = {
                _id : questionId,
                answerers : userId
            };

            let curQuestion = await Question.findOne(condition);
            console.log(curQuestion);

            if (!curQuestion) {
                curQuestion = await Question.findByIdAndUpdate(questionId, {$push : {answerers : userId}});
                await curQuestion.save();
            }

            let curAnswer = await Answer.findOne({key : answerKey});
            console.log("cur answer is: " + curAnswer);

            if (curAnswer == null) {
                logger.info("No answer exists, creating new answer");
                curAnswer = new Answer({
                    questionRef : questionId,
                    userAnswerId : userId,
                    date : Date.now(),
                    key : answerKey
                });

                await curAnswer.save();
            }

            await onJoin(userId, questionId);
            await curQuestion.save();

            redisClient.getAsync(answerKey).then((result) => {

                if (result === null) {
                    logger.info("value was not cached");
                    const curSequence = curAnswer.answer;
                    io.to(roomId).emit("create", {answer : curSequence});
                } else {
                    logger.info("cached");
                    logger.info(result);
                    io.to(roomId).emit("create", {answer : result});
                }
                
            }).catch((err) => {
                logger.error(err);
                io.to(roomId).emit("create", {answer : " "});
            });

        } catch (error) {
            logger.error("error in joinQuestion");
            logger.error(error);
        }
    });

    socket.on("messagedetection", async (data) => {

        const {questionId, userId, currentSequence} = data; 

        const answerKey = `${questionId}-${userId}`;
        logger.info("answer key is: " +  answerKey);
        logger.info(currentSequence);

        redisClient.setex(answerKey, 36000000, currentSequence);

        io.to(`room_${questionId}_${userId}`).emit("message", {message : currentSequence});

        try {

            if (currentSequence !== null || currentSequence !== "") {
                let curAnswer = await Answer.findOneAndUpdate({key : answerKey}, {answer : currentSequence});
                if (curAnswer !== null) {
                    await curAnswer.save();
                }   
            }
        } catch (error) {
            logger.error("error in message detection");
            logger.error(error);
        }
    });

};

module.exports = questionHandler; 