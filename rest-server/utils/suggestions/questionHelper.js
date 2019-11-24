const Question = require("../../schema/questions");
const User = require("../../schema/user");
const {logger} = require("../../../logger");
const getCosineSimilarity = require("../suggestions/cosineSimilarity");
const MAX_RETRIEVED_QUESTIONS = 3;
const MAX_KEYWORDS = 8; 
const SIMILARITY_THRESHOLD = 0.4;
const MAXIMUM_RETURNED_QUESTIONS = 5;
// const {startServer} = require("../../api");
// const {mongodburl, port} = require("../../../config");

/**
 * @param {ObjectID} userID MongoDB ObjectID of a user
 * @param {Number} numberOfQuestions Number of Questions to return 
 */
const getQuestionsByUser = async (userID, numberOfQuestions) => {
    try {
        let userQuestions = await Question.find({}).limit(numberOfQuestions).byUserId(userID);
        // logger.info("User Questions Length: " + userQuestions.length);

        if (userQuestions.length === 0) {
            throw new Error("Could not find Question");
        }
        return Promise.resolve(userQuestions);

    } catch (err) {
        logger.error("questions error " + err);
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
 * @param {Array} keywordList List of keywords to match with 
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
 * @param {Object} questions Array of question objects with their keywords + frequency
 *                           Note: the keywords + freq matches those of the questionKeywords
 * @param {Array} questionKeywords Array of keywords with their frequency
 * @returns {Array} Of questions that meet the cosine similarity value threshold
 */
 const getBagOfQuestions = (questions, questionKeywords) => {
    const bagOfQuestions = [];
    const keywordsFreqOnly = questionKeywords.map(({freq}) => freq);
    
    for (let i = 0; i < questions.size; i++) {

        // Take only the frequency 
        const freqArray = questions[parseInt(i)].keywordsWithFreq.map(({ freq }) => freq);
        

        const cosineSimilarity = getCosineSimilarity(freqArray, keywordsFreqOnly);
        logger.info("Current Question:");
        logger.info(questions[parseInt(i)].question);
        logger.info("Cosine Similarity Value: " + cosineSimilarity);

        if (cosineSimilarity > SIMILARITY_THRESHOLD) {

            // Push ONLY THE QUESTION OBJECT
            bagOfQuestions.push(questions[parseInt(i)].question);
        }

        if (bagOfQuestions > MAXIMUM_RETURNED_QUESTIONS) {
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


