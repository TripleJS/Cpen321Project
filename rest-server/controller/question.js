const Question = require("../schema/questions");
const User = require("../schema/user");
const keywords = require("../utils/lg");
const {errorCatch, errorThrow} = require("../utils/errorHandler");
const {logger} = require("../../logger");
const { getQuestionsByUser, 
        getKeywordFrequency,
        matchKeywords,
        MAX_RETRIEVED_QUESTIONS,
        getBagOfQuestions,
        MAX_KEYWORDS } = require("../utils/suggestions/questionHelper");


const getQuestion = async (req, res, next) => {
    const questionID = req.params.questionId; 

    logger.info("current question id " + questionID);
    try {
        
        let question = await Question.findById(questionID);
        
        if (question == null) {
            errorThrow({}, "Could not find Question", 403);
        }

        res.status(200).json(question);

    } catch (error) {
        errorCatch(error, next);
    }  
};

const postQuestion = async (req, res, next) => {

    const curDate = new Date();
    const newTitle = req.body.title;
    const questionString = req.body.question;
    const lowerCaseString = questionString.toLowerCase();

    logger.info(lowerCaseString);
    const creator = req.body.owner; 
    const newCourse = req.body.course;
    newCourse.toLowerCase().replace(" ", "");
    logger.info(newCourse);

    try {
        const curUser = await User.findById(creator);

        if (curUser == null) {
            errorThrow({}, "Could not find User", 403);
        }

        const question = new Question({
            title : newTitle,
            question: questionString,
            date : curDate,
            owner : creator,
            course : newCourse
        });

        res.status(203).json(question);

        const questionKeywords = keywords(lowerCaseString);
        question.keywords = questionKeywords;

        await question.save();

    } catch (error) {
        errorCatch(error, next);
    }
};


const suggestedQuestionsV2 = async (req, res, next) => {
    try {
        
        const userId = req.params.userId; 
        // Array of Question Objects
        let userQuestions = await getQuestionsByUser(userId, MAX_RETRIEVED_QUESTIONS);

        let userQuestionKeywords = [];

        let i;

        for (i = 0; i < userQuestions.length; i++) {
            userQuestionKeywords.push.apply(userQuestionKeywords, userQuestions[parseInt(i, 10)].keywords);
        }

        // Keywords and their frequency 
        const userKeywordFrequency = getKeywordFrequency(userQuestionKeywords, MAX_KEYWORDS);
        logger.info("User Keyword Frequency:");
        logger.info(userKeywordFrequency);
    
        // Question Objects for the User 
        let questionsForUser = await Question.find({}).bySwipedUser(userId);
        logger.info(questionsForUser);
        /**
         * Question Objects for the User with the updated keywords including
         * their frequency 
         */
        let questionsWithUpdatedFreq = [];

        for (i = 0; i < questionsForUser.length; i++) {
            const updatedKeywords = matchKeywords(questionsForUser[parseInt(i, 10)], userKeywordFrequency);
            const updatedKeywordsWithFreq = getKeywordFrequency(updatedKeywords, updatedKeywords.length);
            logger.info(updatedKeywords);
            questionsWithUpdatedFreq.push({question: questionsForUser[parseInt(i, 10)], keywordsWithFreq : updatedKeywordsWithFreq});
        }
        
        const questionsToReturn = getBagOfQuestions(questionsWithUpdatedFreq, userKeywordFrequency);
        logger.info("questions to return: " + questionsToReturn);
        res.status(200).json(questionsToReturn);
 
    } catch (error) {
        errorCatch(error, next);
    }
};

const swipedQuestion = async (req, res, next) => {
    const questionId = req.body.questionId;
    const userId = req.body._id;
    const userDirection = req.body.direction; 

    logger.info(questionId);
    logger.info(userId);
    logger.info(userDirection);

    try {
        let result = await Question.findById(questionId);

        if (result === null) {
            errorThrow({}, "Question Not Found", 403);
        }

        result = await Question.findOneAndUpdate({_id : questionId, swipedUsers : userId}, {$set : {"swipedUsers.$" : userId}},
                    {new : true});

        if (!result) {
            result = await Question.findByIdAndUpdate(questionId, {$push: {swipedUsers : userId}});
        }

        res.status(200).json({
            user: userId,
            direction : userDirection
        });

    } catch (error) {
        errorCatch(error, next);
    }
};

const getMostRecentQuestion = async (req, res, next) => {
    const userId = req.params.userId;

    try {
        const latestQuestion = await Question.findOne({owner : userId}).sort({date : -1});

        if (latestQuestion == null) {
            res.status(200).json([]);
            return;
        }

        res.status(200).json(latestQuestion);
    } catch (error) {
        errorCatch(error, next);
    }
};


module.exports = {
    getQuestion,
    postQuestion,
    swipedQuestion,
    suggestedQuestionsV2,
    getMostRecentQuestion
};
