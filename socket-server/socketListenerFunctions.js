const Question = require("../rest-server/schema/questions");
const User = require("../rest-server/schema/user");
const {logger} = require("../logger");
const {sendNotification, subscribeToTopic, createNotificationMessage} = require("./utils/fcm");

const onJoin = async (uId, qId) => {
    // let fcmAccessToken;
    let questionData;

    try {
        // logger.info(userId);
        questionData = await Question.findById(qId);
        logger.info("Question Data: " + questionData);

        const notification = createNotificationMessage("Your Question " + questionData.title + " is being answered", "Someone is answering your Question!");
        const inputData = {
            questionId : qId,
            userId : uId
        };

        await sendNotification(qId, notification, inputData);
        
    } catch (error) {
        logger.error("error in onJOin");
        logger.error(error);
        return new Promise.reject(error);
    }
};

module.exports = {
    onJoin
};