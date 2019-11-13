const Question = require("../../schema/questions");
const User = require("../../schema/user");
// const {logger} = require("../../../logger");
// const {startServer} = require("../../api");
// const {mongodburl, port} = require("../../../config");
const MAX_RETRIEVED_QUESTIONS = 3;
const MAX_KEYWORDS = 8; 

/**
 * @param {ObjectID} userID MongoDB ObjectID of a user
 */
const getQuestionsByUser = async (userID, numberOfQuestions) => {
    try {
        let userQuestions = await Question.find({}).limit(numberOfQuestions).byUserId(userID);
        // logger.info("User Questions Length: " + userQuestions.length);
        return userQuestions;

    } catch (err) {
        // logger.error("questions error" + err);
    }
};

/**
 * @param {Array} questionKeywords List of keywords
 * @returns Frequency of keywords in descending order with size MAX_KEYWORDS or less
 */
 
const getKeywordFrequency = (questionKeywords) => {

    // Sorts in alphabetical order 
    questionKeywords.sort();
    let prev; 
    let freqArray = [], wordsArray = [];
    let keywordFreqArray = [];

    for (var i = 0; i < questionKeywords.length; i++) {
        if (questionKeywords[parseInt(i)] !== prev) {
            wordsArray.push(questionKeywords[parseInt(i)]);
            freqArray.push(1);
        } else {
            freqArray[parseInt(freqArray.length - 1)]++;
        }
        prev = questionKeywords[i];
    }

    for (var i = 0; i < wordsArray.length; i++) {
        keywordFreqArray.push(
            {keyword: wordsArray[parseInt(i)], 
                freq: freqArray[parseInt(i)]});
    }

    keywordFreqArray.sort((a, b) => {
        return (a.freq < b.freq) ? 1 : -1;
    });

    return keywordFreqArray.slice(0, MAX_KEYWORDS);
};

const testArray = ["test", "hello", "myname", "test", "nibba", "xdddd", "xdddd", "cmon", "cuh", "yo", "yodawh", "yoyo", "hey", "there", "their", "there", "there", "xddd"];

console.log(getKeywordFrequency(testArray));

module.exports = {
    getQuestionsByUser,
    getKeywordFrequency,
    MAX_RETRIEVED_QUESTIONS
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


