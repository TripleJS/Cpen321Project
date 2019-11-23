const Question = require("../rest-server/schema/questions");
const User = require("../rest-server/schema/user");
const {logger} = require("../logger");
const {sendNotification, subscribeToTopic, createNotificationMessage} = require("./utils/fcm");

const onJoin = async (userId, questionId) => {
    let fcmAccessToken;
    let questionData;

    try {
        logger.info(userId);
        questionData = await Question.findById(questionId);
        logger.info("Question Data: " + questionData);

        const questionOwner = questionData.owner;
        let user = await User.findById(questionOwner);
        logger.info("User is: " + user);

        fcmAccessToken = user.fcmAccessToken;

        await subscribeToTopic(questionId, fcmAccessToken);

        const notification = createNotificationMessage("Your Question " + questionData.title + " is being answered", "Someone is answering your Question!");
        const inputData = {
            questionId : questionId,
            userId : userId
        };

        // {
        //     data : {
        //         questionId: SVGAnimatedTransformList,
        //         userId: dfa;lsdfjlas;
        //     },
        //     topic : top,
        //     notif : defaultksjf,
        // }

        await sendNotification(questionId, notification, inputData);
        
    } catch (error) {
        logger.error("error in onJOin");
        logger.error(error);
        return new Promise.reject(error);
    }
};

module.exports = {
    onJoin
};