const admin = require("firebase-admin");
const serviceAccount = require("../fcm/ubconnect-ec25e-firebase-adminsdk-4zww0-0d250bdc3b.json");
const Question = require("../rest-server/schema/questions");
const User = require("../rest-server/schema/user");
const logger = require('../logger');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://ubconnect-ec25e.firebaseio.com"
});

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
        
        admin.messaging().subscribeToTopic(fcmAccessToken, questionId)
            .then((res) => {
                logger.info("subscribed to topic " + res);
            })
            .catch((err) => {
                console.err("error subscribing " + err);
            });

    } catch (error) {
        console.error(error);
    }

    const message = {
        notification: {
            title: "Someone is answering your question!",
            body: "Your question " + questionData.title + " is being answered"
        },
        data : {
            message : "Some user is answering your question"
        },
        topic: questionId
    };
      
      // Send a message to the device corresponding to the provided
      // registration token.
    admin.messaging().send(message)
        .then((response) => {
          // Response is a message ID string.
          logger.info("Successfully sent message:", response);
        })
        .catch((error) => {
          logger.info("Error sending message:", error);
        });
};

module.exports = {
    onJoin
};