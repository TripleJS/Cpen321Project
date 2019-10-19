const mongoose = require('mongoose');
const Question = require('../schema/questions');
const errorHandler = require('../utils/errorHandler');

const getQuestion = async (req, res, next) => {
    const questionID = mongoose.Types.ObjectId(req.body.id);
    
    try {
        const question = Question.findById(questionID);
        
        if (question == null) {
            errorHandler.errorThrow({}, "Could not find Question", 403);
        }


    } catch (error) {
        errorHandler.errorCatch(error);
    }
    
}

const postQuestion = async (req, res, next) => {
    try {
        const question = new Question({



        });


    } catch (error) {
        
    }

}


const suggestedQuestions = async (req, res, next) => {


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



