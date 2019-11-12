const User = require("../schema/user");
const bcrypt = require("bcryptjs");
const {errorCatch, errorThrow} = require("../utils/errorHandler");
const { validationResult } = require("express-validator");
const {secretKey} = require("../../config");
const jwt = require("jsonwebtoken");
const {logger} = require("../../logger");
const {getQuestionsByUser} = require("../utils/suggestions/questionHelper");

// Controllers for creating new users and getting users 
const addUser = async (req, res, next) => {
    const newUserData = req.body;

    const userName = newUserData.userName;
    const newUserName = newUserData.name;
    const userEmail = newUserData.email;
    const password = newUserData.password;    
    
    const errors = validationResult(req);

    try {
        errorThrow(errors, "Validation Failed", 403);
        let hashedPassword = await bcrypt.hash(password, 12);

        const newUser = new User({
            method: "local",
            local: 
            {
                name: newUserName, 
                email: userEmail, 
                passwordHash: hashedPassword
            },
            userName: newUserName,
        });
    
        let result = await newUser.save();

        res.status(201).json(
            {
                message: "Added User",
                user : result 
            }
        );   
    }
    catch (err)
    {
        errorCatch(err, next);
    }
};

const getUser = async (req, res, next) => {
    try {

        const id = req.params.userId;
        logger.info("User ID: " + id);

        let user = await User.findById(id);

        if (user == null) {
            errorThrow({}, "User Does Not Exist", 403);
        }

        let userQuestions = await getQuestionsByUser(id);
        let newUser = user.toObject();
        newUser.questions = userQuestions;

        logger.info(newUser);

        res.status(200).json(newUser);

    } catch (error) {
        errorCatch(error, next);
    }
};

const updateUser = async (req, res, next) => {

    try {
        const newUserName = req.body.username;
        const newCourses = req.body.courses; 
        const userId = req.params.userId;

        let user = await User.findById(userId);

        if (user == null) {
            errorThrow({}, "User Does Not Exist", 403);
        }

        user.userName = newUserName;
        
        for (var i = 0; i < courses.length; i++) {
            user.coures.push(newCourses[parseInt(i)]);
        }

        await user.save();

        res.status(200).json({
            message : "Sucessfully Updated User " + userId
        });

    } catch (error) {
        errorCatch(error, next);
    }
}

const oAuthLogin = async (req, res, next) => {
    const select = ({_id, userName}) => ({_id, userName});
    const user = select(req.user);

    logger.info(req.user);

    const userFcmAccessToken = req.body.fcmAccessToken;

    req.user.set({fcmAccessToken : userFcmAccessToken});

    const token = jwt.sign({
            user: user._id 
        },
            secretKey, 
            { expiresIn: "24h" },
    );

    logger.info("JWT TOKEN: " + token);

    try {
        let result = await req.user.save();
        logger.info("User Info: " + result);

        res.status(200).json({
            userId : user._id,
            jwt : token
        });

    } catch (error) {
        errorCatch(error, next);
    }

};


module.exports = {
    addUser,
    oAuthLogin,
    getUser,
    updateUser
};