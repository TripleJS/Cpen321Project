const mongoose = require('mongoose');
const Schema = mongoose.Schema;
var userSchema = require('user');

const groupStudySchema = new Schema(
    {
        g_id : ObjectId,
        strat_date : Date,
        end_date : Date,
        users : [userSchema]
    }
);


module.exports = mongoose.model('GroupStudy', groupStudySchema);