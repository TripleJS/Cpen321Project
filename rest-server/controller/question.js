const mongoose = require('mongoose');
const Question = require('../schema/questions');
const User = require('../schema/user');
const errorHandler = require('../utils/errorHandler');
const {validationResult} = require('express-validator');
const {isEmpty} = require('lodash');
const getKeywords = require('../utils/suggestions/keywordExtractor');

const getQuestion = async (req, res, next) => {
    const questionID = mongoose.Types.ObjectId(req.body.id);

    try {
        
        const question = Question.findById(questionID);
        
        if (question == null) {
            errorHandler.errorThrow({}, "Could not find Question", 403);
        }

        res.status(200).json({
            message : 'Question Found', 
            question : question
        });

    } catch (error) {
        errorHandler.errorCatch(error);
    }
    
}

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

        let keywords = await getKeywords(questionString);

        question.set('keywords', keywords);

        question.save();

    } catch (error) {
        errorHandler.errorCatch(error, next);
    }
}



const suggestedQuestions = async (req, res, next) => {
    const date1 = new Date();
    const date2 = new Date();

    res.status(200).json(
        [
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
    );

}

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
}


module.exports = {
    getQuestion,
    postQuestion,
    suggestedQuestions,
    searchQuestion
}



