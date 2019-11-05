const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const answerSchema = new Schema({
    answer : {
        type : String,
        required : true
    }, 
    questionRef : {
        type : mongoose.Schema.Types.ObjectId,
        required: true
    },
    userAnswerID : {
        type : mongoose.Schema.Types.ObjectId,
        required: true
    },
    rate : {
        type : Number
    }
});

module.exports = mongoose.model("Answer", answerSchema);