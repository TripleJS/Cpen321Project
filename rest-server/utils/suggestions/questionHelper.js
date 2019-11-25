const Question = require("../../schema/questions");
const {logger} = require("../../../logger");
const getCosineSimilarity = require("../suggestions/cosineSimilarity");
const MAX_RETRIEVED_QUESTIONS = 100;
const MAX_KEYWORDS = 8; 
const SIMILARITY_THRESHOLD = () => (0.4);
const MAXIMUM_RETURNED_QUESTIONS = 5;


/**
 * @param {ObjectID} userID MongoDB ObjectID of a user
 * @param {Number} numberOfQuestions Number of Questions to return 
 */
const getQuestionsByUser = async (userID, numberOfQuestions) => {
    try {
        let userQuestions = await Question.find({}).byUserId(userID).limit(numberOfQuestions);
        // logger.info("User Questions Length: " + userQuestions.length);

        if (userQuestions.length === 0) {
            Promise.resolve([]);
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

    let i;

    for (i = 0; i < questionKeywords.length; i++) {
        if (questionKeywords[parseInt(i)] !== prev) {
            wordsArray.push(questionKeywords[parseInt(i)]);
            freqArray.push(1);
        } else {
            freqArray[parseInt(freqArray.length - 1)]++;
        }
        prev = questionKeywords[i];
    }

    for (i = 0; i < wordsArray.length; i++) {
        keywordFreqArray.push(
            {keyword: wordsArray[parseInt(i)], 
                freq: freqArray[parseInt(i)]});
    }

    if (keywordFreqArray.length == 0) {
        return [];
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

    let i;
    for (i = 0; i < keywords.length; i++) {
        const found = keywordList.some(el => el.keyword === keywords[i]);

        if (found) {
            updatedKeywords.push(keywords[i]);
        }
    }

    return updatedKeywords;
}

/**
 * @param {Object} questions Array of question objects with their keywords + frequency
 *                           Note: the keywords + freq matches those of the questionKeywords (ie. {question : questionObject, keywordsWithFreq : [{keyword : qkeyword, freq : qfreq}]})
 * @param {Array} questionKeywords Array of keywords with their frequency
 * @returns {Array} Questions that meet the cosine similarity value threshold
 */
 const getBagOfQuestions = (questions, questionKeywords) => {
    const bagOfQuestions = [];
    const keywordsFreqOnly = questionKeywords.map(({freq}) => freq);
    logger.info(keywordsFreqOnly);
    let i;
    for (i = 0; i < questions.length; i++) {

        // Take only the frequency 
        const freqArray = questions[parseInt(i)].keywordsWithFreq.map(({ freq }) => freq);
        logger.info("frequency array: " + freqArray);
        const cosineSimilarity = getCosineSimilarity(freqArray, keywordsFreqOnly);
        logger.info("Cosine Similarity Value: " + cosineSimilarity);

        if (cosineSimilarity > SIMILARITY_THRESHOLD()) {

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
    MAX_KEYWORDS,
    SIMILARITY_THRESHOLD
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

const keywords = [{keyword : "big", freq : 1}, {keyword : "last", freq : 1}, {keyword : "question", freq : 1}, {keyword : "xd", freq : 1}];
const questionsWithKeywords = [{question : {}, keywordsWithFreq : [{keyword : "big", freq : 1}, {keyword : "last", freq : 1}, {keyword : "question", freq : 1}, {keyword : "xd", freq : 1}]}];

let result = getBagOfQuestions(questionsWithKeywords, keywords);
console.log(result);


