const io = require('socket.io');
const {onJoin} = require('./socketListenerFunctions');
const Question = require('../rest-server/schema/questions');
const User = require('../rest-server/schema/user');
/**
 * 
 * @param {SocketIO} io 
 * @param {*} socket 
 */
const questionHandler = (io, socket) => {

    socket.on("join_question", async (userId, questionId) => {
        socket.join("question_" + questionId + "_" + userId);

        try {
            await onJoin(userId, questionId);
            // socket.broadcast.emit('userjoinedthechat', userId + " has joined the chat");
            let curUser = await User.findById(userId);
        


        } catch (error) {
            logger.error(error);
        }

    
        
        io.to("question_" + questionId).emit("create", )
    });

    socket.on("message", (questionId, message) => {
        io.broadcast.to("question_"  + questionId).emit(() => {
            
        })
    });

    
};

module.exports = questionHandler; 