const mongoose = require("mongoose");
const jwt = require("jsonwebtoken");
const {secretKey} = require("../config");
const USER_ID = mongoose.Types.ObjectId();
const QUESTION_ID = mongoose.Types.ObjectId();

const testUser = {
    _id : USER_ID,
    method : "local",
    email : "testemail@email.com",
    local : {
        email : "testemail@email.com",
        password : "testpassword"
    },
    userName : "testusername"
};

const testUser2 = {
    _id : mongoose.Types.ObjectId(),
    method : "facebook",
    email : "facebooktest@email.com",
    local : {
        email : "facebooktest@email.com",
        password : "testpassword"
    },
    userName : "testusername2"
};

const testUser3 = {
    _id : mongoose.Types.ObjectId(),
    method : "facebook",
    email : "facebooktest@email.com",
    local : {
        email : "facebooktest@email.com",
        password : "testpassword"
    },
    userName : "testusername2"
};

const testFacebookUser = {
    _id : USER_ID,
    method : "facebook",
    email : "fbemail@email.com",
    facebook : {
        email : "fbemail@email.com",
        id : "somefbid2"
    },
    userName : "testusername3"
};

const testQuestion = {


};

const testJwt = jwt.sign({
    user: USER_ID
},
    secretKey, 
    { expiresIn: "24h" },
);

module.exports = {
    testUser,
    testUser2,
    testUser3,
    testQuestion,
    testJwt,
    testFacebookUser
};