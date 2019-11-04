const User = require("../schema/user");
const bcrypt = require("bcryptjs");
const errorHandler = require("../utils/errorHandler");
const { validationResult } = require("express-validator");
const mongoose = require("mongoose");
const {secretKey} = require("../../config");

// Controllers for creating new users and getting users 
const addUser = async (req, res, next) => {
    const newUserData = req.body;

    const userName = newUserData.userName;
    const name = newUserData.name;
    const email = newUserData.email;
    const password = newUserData.password;    
    
    const errors = validationResult(req);

    try {
        errorHandler.errorThrow(errors, "Validation Failed", 403);
        let hashedPassword = await bcrypt.hash(password, 12);

        const newUser = new User({
            method: "local",
            local: 
            {
                name: name, 
                email: email, 
                passwordHash: hashedPassword
            },
            userName: userName,
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
        errorHandler.errorCatch(err, next);
    }
}

const getUser = async (req, res, next) => {
    try {

        const id = req.params.userId;

        let user = await User.findById(id);

        if (user == null) {
            errorHandler.errorThrow({}, "User Does Not Exist", 403);
        }

        console.log(user);

        res.status(200).json({
            userData : user
        });

    } catch (error) {
        errorHandler.errorCatch(error, next);
    }
}

const oAuthLogin = async (req, res, next) => {
    const select = ({_id, userName}) => ({_id, userName});
    const user = select(req.user);

    console.log(req.user);

    const fcmAccessToken = req.body.fcmAccessToken;

    req.user.set({fcmAccessToken : fcmAccessToken});

    const token = jwt.sign({
            user: user._id 
        },
            secretKey, 
            { expiresIn: "24h" },
    );

    try {
        let result = await req.user.save();
        console.log("new user data: " + result);

        res.status(200).json({
            userId : user._id,
            jwt : token
        });

    } catch (error) {
        errorHandler.errorCatch(error, next);
    }
    
}

const signJWT = (req, res, next) => {
    const token = jwt.sign(
        {
            user: user
        },
            cfg.secretKey, 
            { expiresIn: "24h" },
        );
}

module.exports = {
    addUser,
    oAuthLogin,
    getUser,
    signJWT
};