const Answer = require("../schema/answers");
const Question = require("../schema/questions");
const {errorCatch} = require("../utils/errorHandler");
const {logger} = require("../../logger");

const getMostRecentAnswerId = async (req, res, next) => {
    const userId = req.params.userId;

    try {
        const latestAnswer = await Answer.findOne({userAnswerID : userId}).sort({date : -1});
        logger.info(latestAnswer);
        const relatedQuestionId = latestAnswer.questionRef;

        const relatedQuestion = await Question.findById(relatedQuestionId);

        res.status(200).json(relatedQuestion);
    } catch (error) {
        errorCatch(error, next);
    }
};

module.exports = getMostRecentAnswerId;