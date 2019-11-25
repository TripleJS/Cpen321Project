const User = require("../schema/user");
const Question = require("../schema/questions");
const keywords = require("../utils/lg");
const {logger} = require("../../logger");
const MIN_SEARCH = 3;
const {errorCatch} = require("../utils/errorHandler");

const containsId = (a, obj) => {
    let i;
    for (i = 0; i < a.length; i++) {
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

        let i;
        let allUsers = [];
        
        if (searchStringKeywords.length === 1) {
            allUsers = await User.find({userName : searchString});
        } else {
            for (i = 0; i < allQuestions.length; i++) {
                const curUser = await User.findById(allQuestions[parseInt(i)].owner);
                const curUserObject = curUser.toObject();
                curUserObject.userId = curUser._id;

                if (!containsId(allUsers, curUserObject)) {
                    allUsers.push(curUserObject);
                }
            }
        }

        logger.info("All Questions: " + allQuestions);
        logger.info("All users: " + allUsers);
        logger.info("All users length: " + allUsers.length);
        res.status(200).json({questions : allQuestions, users : allUsers});
        
    } catch (error) {
        errorCatch(error, next);
    }
};


module.exports = search;