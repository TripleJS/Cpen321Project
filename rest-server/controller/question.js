const Question = require("../schema/questions");
const errorHandler = require("../utils/errorHandler");
const {validationResult} = require("express-validator");
const {isEmpty} = require("lodash");
const getKeywords = require("../utils/suggestions/keywordExtractor");
const getBagOfQuestions = require("../utils/suggestions/cosineSimilarity");

const getQuestion = async (req, res, next) => {
    const questionID = req.params.questionId; 

    console.log("current question id" + questionID);
    try {
        
        let question = await Question.findById(questionID);
        
        if (question == null) {
            errorHandler.errorThrow({}, "Could not find Question", 403);
        }

        res.status(200).json(question);

    } catch (error) {
        errorHandler.errorCatch(error, next);
    }  
};

const postQuestion = async (req, res, next) => {

    const curDate = new Date();
    const title = req.body.title;
    const questionString = req.body.question;
    const lowerCaseString = questionString.toLowerCase();

    console.log(lowerCaseString);
    const creator = req.body.owner; 
    const course = req.body.course;

    const errors = validationResult(req);

    try {
        if (!isEmpty(errors)) {
            errorHandler.errorThrowValidator(errors, "Couldn't Find User", 403);
        }
        const question = new Question({
            title : title,
            question: questionString,
            date : curDate,
            owner : creator,
            course : course
        });

        res.status(203).json(question);
        const document = {documents:[
            {language:"en", id:"1", text: questionString}
        ]};

        let keywords = await getKeywords(document);

        question.set("keywords", keywords);

        question.save();

    } catch (error) {
        errorHandler.errorCatch(error, next);
    }
};


const suggestedQuestions = async (req, res, next) => {

    console.log(req.params.userId);

    try {
        let question = await Question.findOne();

        let questionList = await Question.find({}).limit(5);

        let returnedQuestions = getBagOfQuestions(questionList, question);

        res.status(200).json(
            returnedQuestions
        );

        
    } catch (error) {
        errorHandler.errorCatch(error);
    }
};

const suggestedQuestionsV2 = (req, res, next) => {
    try {
        let randomQuestion = await Question.findOne();

        let questionList = await Question.find({}).limit(5);
        let resultingQuestions = [randomQuestion];

        for (i of questionList) {
            if (randomQuestion.course == questionList[i].course) {
                resultingQuestions.push(questionList[i]);
            }
        }

        res.status(203).json(
            resultingQuestions
        )
    } catch (error) {
        errorHandler.errorCatch(error, next);
    }
};

const searchQuestion = async (req, res, next) => {

    const date1 = new Date();
    const date2 = new Date();

    res.status(200).json({
        questions : [
            {
                questionTitle: "FUCK",
                question: "CPEN321",
                date: date1,
                owner: "John"
            },
            {
                questionTitle: "FUCK",
                question: "CPEN331",
                date: date2,
                owner: "DJFASKDFJSAF"
            }
        ]
    });
};

const swipedQuestion = (req, res, next) => {
    const questionId = req.body.questionId;
    const userId = req.body.userId;
    const direction = req.body.direction; 

    console.log(questionId);
    console.log(userId);
    console.log(direction);

    res.status(200).json({
        user: userId,
        direction : direction
    });
};

module.exports = {
    getQuestion,
    postQuestion,
    suggestedQuestions,
    searchQuestion,
    swipedQuestion
};



