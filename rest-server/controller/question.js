const Question = require("../schema/questions");
const User = require("../schema/user");
const keywords = require("../utils/lg");
const {errorCatch, errorThrow, errorThrowValidator} = require("../utils/errorHandler");
const {validationResult} = require("express-validator");
const {isEmpty} = require("lodash");
const {logger} = require("../../logger");
const { getQuestionsByUser, 
        getKeywordFrequency,
        matchKeywords,
        MAX_RETRIEVED_QUESTIONS,
        getBagOfQuestions,
        MAX_KEYWORDS } = require('../utils/suggestions/questionHelper');





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
    const title = req.body.title;
    const questionString = req.body.question;
    const lowerCaseString = questionString.toLowerCase();

    logger.info(lowerCaseString);
    const creator = req.body.owner; 
    const course = req.body.course;
    course.toLowerCase().replace(" ", "");
    console.log(course);

    try {
        const curUser = await User.findById(creator);

        if (curUser == null) {
            errorThrow({}, "Could not find User", 403);
        }

        const question = new Question({
            title : title,
            question: questionString,
            date : curDate,
            owner : creator,
            course : course
        });

        res.status(203).json(question);

        const questionKeywords = keywords(lowerCaseString);
        question.keywords = questionKeywords;

        await question.save();

    } catch (error) {
        errorCatch(error, next);
    }
};


const suggestedQuestions = async (req, res, next) => {

    logger.info(req.params.userId);

    try {
        let questionList = await Question.find({}).limit(5);

        res.status(200).json(
            questionList
        );    
    } catch (error) {
        errorCatch(error, next);
    }
};

const suggestedQuestionsV2 = async (req, res, next) => {
    try {
        
        const userId = req.params.userId; 
        // Array of Question Objects
        let userQuestions = await getQuestionsByUser(userId, MAX_RETRIEVED_QUESTIONS);
        
        const curUser = await User.findById(userId);
        const {courses} = curUser;

        // Array of all the keywords from all user questions
        let userQuestionKeywords = [];

        for (var i = 0; i < userQuestions.length; i++) {
            userQuestionKeywords.pushValues(userQuestions[parseInt(i)].keywords);
        }

        // Keywords and their frequency 
        const userKeywordFrequency = getKeywordFrequency(userQuestionKeywords, MAX_KEYWORDS);
        logger.info("User Keyword Frequency:");
        logger.info(userKeywordFrequency);
        try {
            // Question Objects for the User 
            let questionsForUser;

            /**
             * Question Objects for the User with the updated keywords including
             * their frequency 
             */
            let questionsWithUpdatedFreq = [];

            if (courses.isEmpty()) {
                questionsForUser = await Question.bySwipedUser(userId);
            } else {
                questionsForUser = await Question.byCourseTag(courses, userId);
            }

            for (var i = 0; i < questionsForUser.length; i++) {
                const updatedKeywords = matchKeywords(questionsForUser[parseInt(i)], userKeywordFrequency);
                questionsWithUpdatedFreq.push({question: questionsForUser[parseInt(i)], keywordsWithFreq : updatedKeywords});
            }
            
            const questionsToReturn = getBagOfQuestions(questionsWithUpdatedFreq, userKeywordFrequency);

            res.status(200).json(questionsToReturn);
        } catch (error) {
            
        }

        res.status(203).json(
            resultingQuestions
        );
    } catch (error) {
        errorCatch(error, next);
    }
};

const searchQuestion = async (req, res, next) => {
    logger.info("In search question");
    logger.info(req.query);

    try {
        const questions = await Question.find({}).limit(5);
        const users = await User.find().limit(2);
        const newUsers = [];

        for (var i = 0 ; i < users.length; i++) {
            const currentUser = users[parseInt(i)].toObject();
            currentUser.userId = currentUser._id;
            newUsers.push(currentUser);
        }

        res.status(200).json({
            questions : questions,
            users : newUsers
        });
        
    } catch (error) {
        errorCatch(error, next);
    }
    
};

const swipedQuestion = async (req, res, next) => {
    const questionId = req.body.questionId;
    const userId = req.body.userId;
    const direction = req.body.direction; 

    logger.info(questionId);
    logger.info(userId);
    logger.info(direction);

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
            direction : direction
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
            errorThrow({}, "Could not find any questions", 403);
        }

        res.status(200).json(latestQuestion);
    } catch (error) {
        errorCatch(error, next);
    }
};


module.exports = {
    getQuestion,
    postQuestion,
    suggestedQuestions,
    searchQuestion,
    swipedQuestion,
    suggestedQuestionsV2,
    getMostRecentQuestion
};



