const mongoose = require("mongoose");
const jwt = require("jsonwebtoken");
const {secretKey} = require("../config");
const USER_ID = mongoose.Types.ObjectId();
const QUESTION_ID = mongoose.Types.ObjectId();
const Question = require("../rest-server/schema/questions");
const User = require("../rest-server/schema/user");
const Answer = require("../rest-server/schema/answers");

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

const testJwt = jwt.sign({
    user: USER_ID
},
    secretKey, 
    { expiresIn: "24h" },
);

const initializeDatabase =  async () => {
    let question, question2, question3, question4, question5, question6;
    let user, user2, user3;

    question = new Question(testQuestion);
    question2 = new Question(testQuestion2);
    question3 = new Question(testQuestion3);
    question4 = new Question(testQuestion4);
    question5 = new Question(testQuestion5);
    question6 = new Question(testQuestion6);
    user = new User(testUser);
    user2 = new User(testUser2);
    user3 = new User(testUser3);

    await question.save();
    await question2.save();
    await question3.save();
    await question4.save();
    await question5.save();
    await question6.save();
    await user.save();
    await user2.save();
    await user3.save();
};

module.exports = {
    testUser,
    testUser2,
    testUser3,
    testQuestion,
    testJwt,
    testFacebookUser,
    initializeDatabase
};