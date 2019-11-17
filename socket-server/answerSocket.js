const io = require('socket.io');
const {onJoin} = require('./socketListenerFunctions');
const Question = require('../rest-server/schema/questions');
const Answer = require("../rest-server/schema/answers");
const User = require('../rest-server/schema/user');
const {logger} = require("../logger");

const answerHandler = (io, socket, redisClient) => {
    socket.on("joinViewAnswer", (data) => {

    });

};

module.exports = answerHandler; 