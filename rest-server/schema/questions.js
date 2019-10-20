const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const questionSchema = new Schema({
    question : {
        type : String,
        required : true
    }, 
    keywords : {
        type : [String],
        required : true,
        default: []
    }, 
    answers : {
        type : [mongoose.Schema.Types.ObjectId],
        required : true,
        default: []
    }, 
    userPosterID : {
        type : mongoose.Schema.Types.ObjectId,
        required: true
    },
    rate : {
        type : Number
    },
    date : {
        type : Date,
        required : true
    }
}); 

module.exports = mongoose.model('Question', questionSchema);