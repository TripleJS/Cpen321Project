const User = require("../schema/user");
const bcrypt = require("bcryptjs");
const aesjs = require("aes-js");
const {errorCatch, errorThrow} = require("../utils/errorHandler");
const { validationResult } = require("express-validator");
const {secretKey} = require("../../config");
const jwt = require("jsonwebtoken");
const {logger} = require("../../logger");
const {getQuestionsByUser, MAX_RETRIEVED_QUESTIONS} = require("../utils/suggestions/questionHelper");

const signTokenAndSignIn = (id, res) => {
    const token = jwt.sign({
        user: id
    },
        secretKey, 
        { expiresIn: "24h" },
    );

    logger.info("JWT TOKEN: " + token);
    logger.info(id);

    res.status(201).json({
            _id : id, 
            jwt : token
        }
    );   
};

// Controllers for creating new users and getting users 
const addUser = async (req, res, next) => {
    const newUserData = req.body;

    const userEmail = newUserData.email;
    const password = newUserData.password;    
    const newUserName = newUserData.userName;
    
    try {

        let curUser = await User.findOne({email : userEmail});
        let checkUserName = await User.findOne({userName : newUserName});
        console.log(curUser);
        console.log(checkUserName);

        if (curUser === null && checkUserName === null) {
            curUser = new User({
                method: "local",
                local: { 
                    email: userEmail, 
                    password: password
                },
                userName: newUserName,
                email : userEmail
            });

            await curUser.save();
        } else {
            errorThrow({}, "User already Exists", 403);
        }
        
        signTokenAndSignIn(curUser._id, res);
    } catch (err) {
        errorCatch(err, next);
    }
};

const loginUser = async (req, res, next) => {
    const userEmail = req.body.email;
    const userPassword = req.body.password; 
    
    try {
        let curUser = await User.findOne({email : userEmail});

        if (curUser == null) {
            errorThrow({}, "User does not exist", 404);
        }

        if (curUser.local.password !== userPassword) {
            errorThrow({}, "Incorrect Password", 403);
        }

        signTokenAndSignIn(curUser._id, res);

    } catch (error) {
        errorCatch(error, next);
    }
}

const getUser = async (req, res, next) => {
    try {

        const id = req.params.userId;
        logger.info("User ID: " + id);

        let user = await User.findById(id);

        if (user == null) {
            errorThrow({}, "User Does Not Exist", 403);
        }

        let userQuestions = await getQuestionsByUser(id, MAX_RETRIEVED_QUESTIONS);
        let newUser = user.toObject();
        newUser.questions = userQuestions;
        newUser.userId = user._id;
        
        logger.info(newUser);

        res.status(200).json(newUser);

    } catch (error) {
        errorCatch(error, next);
    }
};

const updateUser = async (req, res, next) => {

    try {
        const newUserName = req.body.userName;
        const newCourses = req.body.courses; 
        const userId = req.params.userId;
        const newUserEmail = req.body.email;

        let user = await User.findById(userId);

        if (user == null) {
            errorThrow({}, "User Does Not Exist", 403);
        }

        logger.info("new user name " + newUserName);
        logger.info("new user email " + newUserEmail);
        logger.info(newCourses);

        user.userName = newUserName;
        user.email = newUserEmail;
        user.courses = newCourses;

        let result = await user.save();
        let userQuestions = await getQuestionsByUser(userId, MAX_RETRIEVED_QUESTIONS);
        const newResult = result.toObject();
        newResult.questions = userQuestions;
        
        logger.info(newResult);

        res.status(200).json(newResult);

    } catch (error) {
        errorCatch(error, next);
    }
}

const oAuthLogin = async (req, res, next) => {
    logger.info(req.user);

    const userFcmAccessToken = req.body.fcmAccessToken;
    logger.info("FCM TOKEN: " + userFcmAccessToken);


    try {
        req.user.fcmAccessToken = userFcmAccessToken;
        let result = await req.user.save();

        logger.info(result);

        signTokenAndSignIn(result._id, res);

    } catch (error) {
        errorCatch(error, next);
    }

};

const rate = async (req, res, next) => {
    const ratingUserId = req.params.ratingUserId;
    const userId = req.body._id;
    const rating = req.body.rating; 

    console.log(ratingUserId);
    console.log(userId);

    try {
        const ratingUser = await User.findById(ratingUserId)
        const ratedUser = await User.findById(userId);
        
        if (ratingUser == null || ratedUser == null) {
            errorThrow({}, "User doesn't Exist", 404);
        }

        let result = await User.findOneAndUpdate(
            {_id : userId, "usersWhoRated.id" : ratingUserId}, 
            {$set: {"usersWhoRated.$.rating" : rating}
        }, {new : true});

        
        if (!result) {
            console.log("pushing new value into database");
            // Push new value onto array if not yet rated
            result = await User.findByIdAndUpdate(userId, {$push : 
                {usersWhoRated : {id : ratingUserId, rating : rating}}}, 
                {new : true});
        }

        let totalRating = result.usersWhoRated.reduce((a, b) => {
            return {rating : a.rating + b.rating};
        }, {rating : 0});

        totalRating = totalRating.rating;

        const avgRating = totalRating / result.usersWhoRated.length;
        result.rating = avgRating;

        await result.save();

        res.status(200).json(result);

    } catch (error) {
        errorCatch(error, next);
    }
};

const report = async (req, res, next) => {
    const reportingUserId = req.params.reportingUserId;
    const userId = req.body._id;

    console.log("reporting userid: " + reportingUserId);
    console.log("reported userid: " + userId);
    
    try {
        const reportingUser = await User.findById(reportingUserId);
        const reportedUser = await User.findById(userId);

        if (reportingUser == null || reportedUser == null) {
            errorThrow({}, "User doesn't Exist", 404);
        }

        let result = await User.findOne({_id : userId, usersWhoReported: reportingUserId});

        if (!result) {
            result = await User.findByIdAndUpdate(userId, {$push : {usersWhoReported : reportingUserId}}, {new : true});
        }

        res.status(200).json(result);

    } catch (error) {
        errorCatch(error, next);
    }
};

const exportFunctions = {
    addUser,
    oAuthLogin,
    getUser,
    updateUser,
    loginUser,
    rate,
    report,
    signTokenAndSignIn
};

module.exports = exportFunctions;