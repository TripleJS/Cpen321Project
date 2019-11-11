const Question = require('../../schema/questions');
const User = require('../../schema/user');
const MAX_RETRIEVED_QUESTIONS = 3;
const {logger} = require('../../../logger');
const {startServer} = require('../../api');
const {mongodburl, port} = require('../../../config');

const getQuestionsByUser = async (userID) => {
    try {
        let userQuestions = await Question.find({}).limit(MAX_RETRIEVED_QUESTIONS).byUserId(userID);
        logger.info("User Questions Length: " + userQuestions.length);
        return userQuestions;

    } catch (err) {
        logger.error("questions error" + err);
    }
};


module.exports = {
    getQuestionsByUser
}

// const test = async() => {
//     try {
//         await startServer(mongodburl, port);
//         await getQuestions("5db007f55452070057d550aa");

//     } catch (err) {
//         logger.error(err);
//     }
// };

// test();


