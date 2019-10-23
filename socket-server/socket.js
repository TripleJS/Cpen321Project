const socketio = require('socket.io');
const admin = require("firebase-admin");
const serviceAccount = require("../fcm/ubconnect-ec25e-firebase-adminsdk-4zww0-0d250bdc3b.json");
const Question = require('../rest-server/schema/questions');
const User = require('../rest-server/schema/user');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://ubconnect-ec25e.firebaseio.com"
});

class SocketServer {
    
    constructor(server) {
        this.io = socketio(server);
    }

    startServer() {
        this.io.on('connection', (socket) => {

            socket.on('join', async (userId, questionId) => {
                console.log(userId + ": joined chat and current question id is " + questionId);
                
                let fcmAccessToken; 
                let questionData;

                try {
                    questionData = await Question.findById(questionId);
                    console.log('Question Data: ' + questionData);

                    const questionOwner = questionData.owner;

                    let user = await User.findById(questionOwner);
                    console.log('User is: ' + user);

                    fcmAccessToken = user.fcmAccessToken;
                    
                    admin.messaging().subscribeToTopic(fcmAccessToken, questionId)
                        .then((res) => {
                            console.log('subscribed to topic ' + res);
                        })
                        .catch((err) => {
                            console.err('error subscribing ' + err);
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
                        message : 'Some user is answering your question'
                    },
                    topic: questionId
                };
                  
                  // Send a message to the device corresponding to the provided
                  // registration token.
                admin.messaging().send(message)
                    .then((response) => {
                      // Response is a message ID string.
                      console.log('Successfully sent message:', response);
                    })
                    .catch((error) => {
                      console.log('Error sending message:', error);
                    });

                socket.broadcast.emit('userjoinedthechat', userId + " has joined the chat");
            });

            socket.on('messagedetection', (senderNickname, messageContent, ) => {

                console.log(senderNickname + " sent " + messageContent);

                let message = {'message' : messageContent, 'senderNickname' : senderNickname};

                console.log(message);
                this.io.emit('message', message);
            });

        });
    }
}

module.exports = SocketServer;