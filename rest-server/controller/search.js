const User = require("../schema/user");
const Question = require("../schema/questions");
const keywords = require("../utils/lg");
const {logger} = require("../../logger");
const MIN_SEARCH = 3;
const {errorCatch} = require("../utils/errorHandler");

const search = async (req, res, next) => {

    const searchString = req.query.question;
    console.log(req.query);
    console.log(searchString);
    searchString.split(" ");

    const searchStringKeywords = keywords(searchString);

    try {
        const allQuestions = await Question.find({keywords : {$all : searchStringKeywords}});
        let allUsers;
        if (searchStringKeywords.length < 1) {
            allUsers = await User.find({userName : searchString});
        } else {
            allUsers = [];
            for (var i = 0; i < allQuestions.length; i++) {
                const curUser = await User.findById(allQuestions[parseInt(i)].owner);
                curUser.toObject();
                curUser.userId = curUser._id;
                allUsers.push(curUser);
            }
        }

        res.send(200).json({question : allQuestions, users : allUsers});
        
    } catch (error) {
        errorCatch(error, next);
    }
}


module.exports = search;