const mongoose = require("mongoose");
const jwt = require("jsonwebtoken");
const {secretKey} = require("../config");
const Question = require("../rest-server/schema/questions");
const User = require("../rest-server/schema/user");
const Answer = require("../rest-server/schema/answers");

const testUser = {
    _id : mongoose.Types.ObjectId(),
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
    _id : mongoose.Types.ObjectId(),
    method : "facebook",
    email : "fbemail@email.com",
    facebook : {
        email : "fbemail@email.com",
        id : "somefbid2"
    },
    userName : "testusername3"
};

const testQuestion = {
    _id : mongoose.Types.ObjectId(),
    question : "Some test question for cpen 321",
    title : "test question 1",
    course : "Cpen 321", 
    owner : testUser._id,
    date : new Date()
};

const testQuestion2 = {
    _id : mongoose.Types.ObjectId(),
    question : "What 3 *4 / 57 and some otner words",
    title : "test question 2",
    course : "Cpen 331", 
    owner : testUser2._id,
    date : new Date()
};

const testQuestion3 = {
    _id : mongoose.Types.ObjectId(),
    question : "What 3 *4 / 57 and some otner words",
    title : "test question 2",
    course : "Elec 221", 
    owner : testUser3._id,
    date : new Date()
};

const testQuestion4 = {
    _id : mongoose.Types.ObjectId(),
    question : "What is the z-transform",
    title : "test question 2",
    course : "Cpsc 320", 
    owner : testUser._id,
    date : new Date()
};

const testQuestion5 = {
    _id : mongoose.Types.ObjectId(),
    question : "xd xd xd xd the but or",
    title : "test question 2",
    course : "Cpsc 221", 
    owner : testUser2._id,
    date : new Date()
};

const testQuestion6 = {
    _id : mongoose.Types.ObjectId(),
    question : "Last question big xd",
    title : "test question 2",
    course : "Math 220", 
    owner : testUser3._id,
    date : new Date()
};

const testAnswer = {

};

const testAnswer2 = {

};

const testAnswer3 = {

};

const testAnswer4 = {

};

const testQuestionArray = [testQuestion, testQuestion2, testQuestion3, testQuestion4, testQuestion5, testQuestion6];
const testUserArray = [testUser, testUser2, testUser3, testFacebookUser];
const testAnswerArray= [testAnswer, testAnswer2, testAnswer3, testAnswer4];

const testJwt = jwt.sign({
    user: testUser._id
},
    secretKey, 
    { expiresIn: "24h" },
);

const testFbJwt = jwt.sign({
    user: testFacebookUser._id
},
    secretKey, 
    { expiresIn: "24h" },
);

const initializeDatabase =  async () => {
    for (var i = 0; i < testUserArray.length; i++) {
        let user = new User(testUserArray[parseInt(i)]);
        await user.save();
    }

    for (var i = 0; i < testQuestionArray.length; i++) {
        let question = new Question(testQuestionArray[parseInt(i)]);
        await question.save();
    }
};

module.exports = {
    testUser,
    testUser2,
    testUser3,
    testQuestion,
    testUserArray,
    testQuestionArray,
    testJwt,
    testFbJwt,
    testFacebookUser,
    initializeDatabase
};