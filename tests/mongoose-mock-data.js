const mongoose = require("mongoose");

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

const testQuestion = {


};

module.exports = {
    testUser,
    testQuestion
};