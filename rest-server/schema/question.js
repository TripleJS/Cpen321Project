const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const questionSchema = new Schema(
    {
        question : String,
        keywords : [String],
        q_id :  ObjectId,
        answer : [String],
        course : String,
        topic : String,
        owner : ObjectId
    }
);


module.exports = mongoose.model('Question', questionSchema);