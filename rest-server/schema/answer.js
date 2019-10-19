const mongoose = require('mongoose');
const Schema = mongoose.Schema;


const answerSchema = new Schema(
    {
        a_id : ObjectId,
        answer : String,
        q_id : ObjectId,
        owner : ObjectId
    }
);


module.exports = mongoose.model('Answer', answerSchema);