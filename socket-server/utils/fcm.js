const admin = require("firebase-admin");
const serviceAccount = require("../../fcm/ubconnect-ec25e-firebase-adminsdk-4zww0-0d250bdc3b.json");
const {logger} = require("../../logger");

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://ubconnect-ec25e.firebaseio.com" // TODO: Add to constants later 
  });

const sendNotification = (topic, notification, inputData) => {

    const message = {
        data : inputData,
        topic, 
        notification
    };

    return new Promise((resolve, reject) => {
        admin.messaging().send(message)
        .then((response) => {
          // Response is a message ID string.
          logger.info("Successfully sent message: " + response);
          resolve("Successfully sent message: " + response);
        })
        .catch((err) => {
          logger.error("Error sending message:" + error);
          reject("Error: " + err);
        });
    })
};


const subscribeToTopic = (topic, token) => {
    return new Promise((resolve, reject) => {
        logger.info("Subcribe To Topic: ");
        logger.info(topic);
        logger.info(token);

        admin.messaging().subscribeToTopic(token, topic)
            .then((res) => {
                logger.info("subscribed to topic " + res);
                resolve("Successfully subscribed to topic: " + topic);
            })
            .catch((err) => {
                logger.error("error subscribing " + err);
                reject("Error: " + error);
            });
    })
};

const createNotificationMessage = (notificationBody, notificationTitle) => {
    return {
        title: notificationTitle,
        body: notificationBody
    }
}

module.exports = {
    sendNotification,
    subscribeToTopic,
    createNotificationMessage
};

