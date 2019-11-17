const User = require("../schema/user");
const Question = require("../schema/questions");
const {logger} = require("../../logger");

const search = (req, res, next) => {

    const searchString = req.params.searchString;
    const searchStringArray = searchString.split("+");

    try {
        
    } catch (error) {
        
    }
}


module.exports = search;