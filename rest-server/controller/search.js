const User = require("../schema/user");
const Question = require("../schema/questions");
const keywords = require("../utils/lg");
const {logger} = require("../../logger");
const MIN_SEARCH = 3;
const {errorCatch} = require("../utils/errorHandler");

const containsId = (a, obj) => {
    for (var i = 0; i < a.length; i++) {
        if (a[i]._id === obj._id) {
            return true;
        }
    }
    return false;
};

const search = async (req, res, next) => {

    const searchString = req.query.question;
    searchString.toLowerCase();
    console.log(searchString);

    const searchStringKeywords = keywords(searchString);
    console.log(searchStringKeywords);

    try {
        const allQuestions = await Question.find({keywords : { $all : searchStringKeywords}});


        for (var i = 0; i < allQuestions.length; i++) {
            console.log(allQuestions[parseInt(i)].title);
        }

        let allUsers;
        if (searchStringKeywords.length === 1) {
            allUsers = await User.find({userName : searchString});
        } else {
            allUsers = [];
            for (var i = 0; i < allQuestions.length; i++) {
                const curUser = await User.findById(allQuestions[parseInt(i)].owner);
                const curUserObject = curUser.toObject();
                curUserObject.userId = curUser._id;
            
                if (!containsId(allUsers, curUser)) {
                    allUsers.push(curUserObject);
                }
            }
        }

        console.log("All Questions: " + allQuestions);
        console.log("All users: " + allUsers);
        console.log("All users length: " + allUsers.length);
        res.status(200).json({questions : allQuestions, users : allUsers});
        
    } catch (error) {
        errorCatch(error, next);
    }
};




module.exports = search;