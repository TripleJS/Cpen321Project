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
    rating : {
        type : Number
    },
    date : {
        type : Date,
        required : true
    }
});

module.exports = mongoose.model("Answer", answerSchema);