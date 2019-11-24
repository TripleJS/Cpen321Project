const Answer = require("../schema/answers");
const Question = require("../schema/questions");
const {errorCatch, errorThrow} = require("../utils/errorHandler");
const {logger} = require("../../logger");

const getMostRecentAnswerId = async (req, res, next) => {
    const userId = req.params.userId;

    try {
        logger.info(userId);
        const latestAnswer = await Answer.findOne({userAnswerId : userId}).sort({date : -1});

        if (latestAnswer === null) {
            res.status(200).json({_id : ""});
        }
        
        logger.info(latestAnswer);
        
        const relatedQuestionId = latestAnswer.questionRef;

        const relatedQuestion = await Question.findById(relatedQuestionId);
        
        res.status(200).json(relatedQuestion);
    } catch (error) {
        errorCatch(error, next);
    }
};


module.exports = getMostRecentAnswerId;