const Question = require("../rest-server/schema/questions");
const User = require("../rest-server/schema/user");
const logger = require("../logger");
const {sendNotification, subscribeToTopic, createNotificationMessage} = require("./utils/fcm");

const onJoin = async (userId, questionId) => {
    logger.info(userId + ": joined chat and current question id is " + questionId);
    
    let fcmAccessToken;
    let questionData;

    try {
        questionData = await Question.findById(questionId);
        logger.info("Question Data: " + questionData);

        const questionOwner = questionData.owner;
        let user = await User.findById(questionOwner);
        logger.info("User is: " + user);

        fcmAccessToken = user.fcmAccessToken;

        await subscribeToTopic(questionId, fcmAccessToken);

        const notification = createNotificationMessage("Your Question " + questionData.title + " is being answered", "Someone is answering your Question!");

        await sendNotification(questionId, notification);

        return new Promise.resolve(userId);
        
    } catch (error) {
        logger.error(error);
        return new Promise.reject(error);
    }
};

module.exports = {
    onJoin
};