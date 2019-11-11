const admin = require("firebase-admin");
const serviceAccount = require("../fcm/ubconnect-ec25e-firebase-adminsdk-4zww0-0d250bdc3b.json");
const Question = require("../rest-server/schema/questions");
const User = require("../rest-server/schema/user");
const logger = require('../logger');
const {sendNotification, subscribeToTopic} = require('./utils/fcm');

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
        
    } catch (error) {
        console.error(error);
    }

      
      // Send a message to the device corresponding to the provided
      // registration token.
    
};

module.exports = {
    onJoin
};