const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const groupSchema = new Schema({
    startDate : {
        type: Date,
        required : true
    }, 
    endDate : { 
        type : Date, 
        required : true
    },
    users : {
        type : [mongoose.Types.ObjectId],
        required : true,
        default : []
    }
});

modules.export = questionSchema;