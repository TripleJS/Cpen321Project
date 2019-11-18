const User = require("../schema/user");
const bcrypt = require("bcryptjs");
const {errorCatch, errorThrow} = require("../utils/errorHandler");
const { validationResult } = require("express-validator");
const {secretKey} = require("../../config");
const jwt = require("jsonwebtoken");
const {logger} = require("../../logger");
const {getQuestionsByUser, MAX_RETRIEVED_QUESTIONS} = require("../utils/suggestions/questionHelper");

// Controllers for creating new users and getting users 
const addUser = async (req, res, next) => {
    const newUserData = req.body;

    const userEmail = newUserData.email;
    const password = newUserData.password;    
    
    try {
        let hashedPassword = await bcrypt.hash(password, 12);

        const newUser = new User({
            method: "local",
            local: {
                name: newUserName, 
                email: userEmail, 
                passwordHash: hashedPassword
            },
            userName: userEmail,
            email : userEmail
        });
    
        let result = await newUser.save();

        const token = jwt.sign({
            user: result._id 
        },
            secretKey, 
            { expiresIn: "24h" },
        );

        res.status(201).json({
                jwt: token,
                user : result 
            }
        );   
    }
    catch (err) {
        errorCatch(err, next);
    }
};

const loginUser = async (req, res, next) => {
    const userEmail = req.body.email;
    const userPassword = req.body.password; 


    try {

        const curUser = User.find({email : userEmail, passwordHash : passwordHash});
    } catch (error) {

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
    const {_id} = req.user;

    logger.info(req.user);

    const userFcmAccessToken = req.body.fcmAccessToken;
    logger.info("FCM TOKEN: " + userFcmAccessToken);

    const token = jwt.sign({
            user: _id 
        },
            secretKey, 
            { expiresIn: "24h" },
    );

    logger.info("JWT TOKEN: " + token);

    try {
        req.user.fcmAccessToken = userFcmAccessToken;
        let result = await req.user.save();

        logger.info(result);

        res.status(200).json({
            userId : _id,
            jwt : token
        });

    } catch (error) {
        errorCatch(error, next);
    }

};

const rate = async (req, res, next) => {
    const userId = req.body.userId;
    const rating = req.body.rating; 

    try {

    } catch (error) {

    }

    

};

const report = (req, res, next) => {


};


module.exports = {
    addUser,
    oAuthLogin,
    getUser,
    updateUser,
    loginUser
};