const User = require("../schema/user");
const Question = require("../schema/questions");
const {logger} = require("../../logger");
const MIN_SEARCH = 3;

const search = async (req, res, next) => {

    const searchString = req.params.searchString;
    const searchStringArray = searchString.split("+");

    try {
        const allQuestions = await Question.find();
    } catch (error) {
        
    }
}


module.exports = search;