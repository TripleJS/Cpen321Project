const Question = require("../../schema/questions");
const User = require("../../schema/user");
const {logger} = require("../../../logger");

const MAX_RETRIEVED_QUESTIONS = 3;
const MAX_KEYWORDS = 8; 
// const {startServer} = require("../../api");
// const {mongodburl, port} = require("../../../config");

/**
 * @param {ObjectID} userID MongoDB ObjectID of a user
 */
const getQuestionsByUser = async (userID, numberOfQuestions) => {
    try {
        let userQuestions = await Question.find({}).limit(numberOfQuestions).byUserId(userID);
        // logger.info("User Questions Length: " + userQuestions.length);
        return Promise.resolve(userQuestions);

    } catch (err) {
        logger.error("questions error" + err);
        return Promise.reject("Error Getting Questions");
    }
};

/**
 * @param {Array} questionKeywords List of keywords
 * @param {Number} numKeywords Number of keywords to return 
 * @returns Frequency of keywords in descending order with size numKeywords or LESS
 */
const getKeywordFrequency = (questionKeywords, numKeywords) => {

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

    return keywordFreqArray.slice(0, numKeywords);
};


/**
 * 
 * @param {Mongoose.Object} question Question object
 * @param {Array} keywordList 
 */
const matchKeywords = (question, keywordList) => {

    const {keywords} = question; 
    let updatedKeywords = [];
    keywords.sort(); 
    for (var i = 0; i < keywords.length; i++) {
        const found = keywordList.some(el => el.keyword === keywords[i]);

        if (found) {
            updatedKeywords.push(keywords[i]);
        }
    }

    return updatedKeywords;
}


/**
 * 
 * @param {Question Object} questions Array of question objects with their keywords
 * @param {Array} questionKeywords Array of keywords with their frequency
 */
 const getBagOfQuestions = (questions, questionKeywords) => {
    const bagOfQuestions = [];

    for (let i = 0; i < questionKeywords.size; i++) {
    
        const cosineSimilarity = getCosineSimilarity(question, questionKeywords[i]);
        if (cosineSimilarity > MINIMUM_RETURNED_QUESTIONS) {
            bagOfQuestions.push(questionKeywords[i]);
        }

        if (bagOfQuestions > SIMILARITY_THRESHOLD) {
            return bagOfQuestions;
        }
    }

    return bagOfQuestions;
};

module.exports = {
    getQuestionsByUser,
    getKeywordFrequency,
    matchKeywords,
    getBagOfQuestions,
    MAX_RETRIEVED_QUESTIONS,
    MAX_KEYWORDS
}

// const testArray = ["test", "hello", "myname", "test", "nibba", "xdddd", "xdddd", "cmon", "cuh", "yo", "yodawh", "yoyo", "hey", "there", "their", "there", "there", "xddd"];

// console.log(getKeywordFrequency(testArray));

// const test = async() => {
//     try {
//         await startServer(mongodburl, port);
//         let userQuestions = await getQuestionsByUser("5db007f55452070057d550ab", 0);

//         console.log(userQuestions);
//     } catch (err) {

//         console.error(err);
//     }
// };

// test();


