const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const questionSchema = new Schema({
    question : {
        type : String,
        required : true
    }, 
    title : {
        type : String,
        required : true
    },
    course : {
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
    owner : {
        type : mongoose.Schema.Types.ObjectId,
        required: true
    },
    rate : {
        type : Number,
        default: 0
    },
    date : {
        type : Date,
        required : true
    }
}); 

module.exports = mongoose.model('Question', questionSchema);